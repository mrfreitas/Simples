package pt.admedia.simples;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.PartnersAPI;
import pt.admedia.simples.model.PartnersEntity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PartnersFragment extends Fragment {

    private MyRecyclerListAdapter myListAdapter;
    private ArrayList<PartnersEntity> partnersList = new ArrayList<PartnersEntity>();
    private RecyclerView react_list;
    private ProgressBar partnersPbar;
    private Typeface fontAwesome;
    private String filter;
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
/*        Spinner categories = (Spinner) getActivity().findViewById(R.id.categories);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        categories.setVisibility(View.VISIBLE);*/
        Spinner categories = (Spinner) getActivity().findViewById(R.id.categories);
        categories.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.partners);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        partnersPbar = (ProgressBar) getActivity().findViewById(R.id.partnersPbar);
        react_list = (RecyclerView) getActivity().findViewById(R.id.partners_list);
        if(partnersList.size()<= 0) {
            filter = "";
            getPartners();
        }

     /*   categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        filter = getContext().getResources().getString(R.string.restaurantes);
                        getPartners();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getPartners() {
        partnersPbar.setVisibility(View.VISIBLE);
        int start = 0, amount = 100;
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
                }
                // Initialize recycler view
                myListAdapter = new MyRecyclerListAdapter(getActivity().getApplicationContext(), partnersList, R.layout.t_card, fontAwesome);
                //    myListAdapter.clearData();
                react_list.setLayoutManager(new LinearLayoutManager(getContext()));
                react_list.setAdapter(myListAdapter);
                partnersPbar.setVisibility(View.GONE);
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

}
