package pt.admedia.simples.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import pt.admedia.simples.MainActivity;
import pt.admedia.simples.R;
import pt.admedia.simples.adapters.MyRecyclerListAdapter;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.PartnersAPI;
import pt.admedia.simples.lib.IsOnline;
import pt.admedia.simples.model.My_Realm;
import pt.admedia.simples.model.PartnersEntity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PartnersFragment extends Fragment {

    private MyRecyclerListAdapter myListAdapter;
    private ArrayList<PartnersEntity> partnersList = new ArrayList<>();
    private RecyclerView partners_list;
    private ProgressBar partnersPbar;
    private String filter;
    private My_Realm my_realm;
    private boolean loadPositionZero;

    public PartnersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partners, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spinner categories = (Spinner) getActivity().findViewById(R.id.categories);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        categories.setVisibility(View.VISIBLE);
        // Spinner reset
        categories.setSelection(0);
        loadPositionZero = false;

        categories.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadPositionZero = true;
                return false;
            }
        });
        partnersPbar = (ProgressBar) getActivity().findViewById(R.id.partnersPbar);
        partners_list = (RecyclerView) getActivity().findViewById(R.id.partners_list);

        my_realm = new My_Realm(getContext());

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if(loadPositionZero)
                            getAsyncLocalPartner();
                        break;
                    case 1:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.restaurantes));
                        break;
                    case 2:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.retalho_alimentar));
                        break;
                    case 3:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.entretenimento_e_lazer));
                        break;
                    case 4:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.reparacoes_automoveis));
                        break;
                    case 5:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.comercio_e_servicos));
                        break;
                    case 6:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.educacao_e_ensino));
                        break;
                    case 7:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.saude_e_bem_estar));
                        break;
                    case 8:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.hoteis_e_turismo));
                        break;
                    case 9:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.moda));
                        break;
                    case 10:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.desporto));
                        break;
                    case 11:
                        getSelectedLocalPartner(getContext().getResources().getString(R.string.casa_e_decoracao));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(((MainActivity) getActivity()).session.getFirstLoad() && IsOnline.isOnline(getContext())) {
            filter = "";
            getOnlinePartners();
        }
        else
            getAsyncLocalPartner();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getOnlinePartners() {
        partnersPbar.setVisibility(View.VISIBLE);
        int start = 0, amount = 200;
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        PartnersAPI api = adapter.create(PartnersAPI.class);
        api.partnerItems(filter, start, amount, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                JsonArray jsonElements = jsonResponse.get("items").getAsJsonArray();
                for (JsonElement partnerJsonE : jsonElements) {
                    JsonObject partnerJson = partnerJsonE.getAsJsonObject();
                    PartnersEntity partner = new PartnersEntity(partnerJson);
                    partnersList.add(partner);
                    my_realm.setPartners(partner);
                }
                // Initialize recycler view
                initRecyclerView();
                ((MainActivity) getActivity()).session.setFirstLoad(false);
            }
            @Override
            public void failure(RetrofitError error) {
                partnersPbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getContext().getString(R.string.rc_4) +
                                " " + getContext().getString(R.string.server_error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAsyncLocalPartner() {
        partnersPbar.setVisibility(View.VISIBLE);
        if(partnersList.size() > 0)
            myListAdapter.clearData();
        my_realm.getAsyncPartners(callback);
    }

    private void getSelectedLocalPartner(String selection) {
        partnersPbar.setVisibility(View.VISIBLE);
        //myListAdapter.clearData();
        partnersList =  my_realm.getPartners(selection);
        myListAdapter.filteredLisItems(partnersList);
        partnersPbar.setVisibility(View.GONE);

    }

    private void initRecyclerView()
    {
        myListAdapter = new MyRecyclerListAdapter(getActivity().getApplicationContext(), partnersList,
                R.layout.t_card);
        //    myListAdapter.clearData();
        partners_list.setLayoutManager(new LinearLayoutManager(getContext()));
        partners_list.setAdapter(myListAdapter);
        partnersPbar.setVisibility(View.GONE);
    }

    private RealmChangeListener callback = new RealmChangeListener() {
        @Override
        public void onChange() { // called once the query complete and on every update
            RealmResults<PartnersEntity> partnerAsync = my_realm.getPartnerAsync();
            for (PartnersEntity partner : partnerAsync) {
                partnersList.add(partner);
            }
            // Initialize recycler view
            initRecyclerView();
        }
    };
}
