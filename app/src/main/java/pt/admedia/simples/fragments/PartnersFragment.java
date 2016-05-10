package pt.admedia.simples.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import pt.admedia.simples.PartnerDetails;
import pt.admedia.simples.R;
import pt.admedia.simples.SimplesApplication;
import pt.admedia.simples.adapters.MyRecyclerListAdapter;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.PartnersAPI;
import pt.admedia.simples.lib.GridSpacingItemDecoration;
import pt.admedia.simples.lib.IsOnline;
import pt.admedia.simples.lib.My_Answers;
import pt.admedia.simples.model.My_Realm;
import pt.admedia.simples.model.PartnersEntity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PartnersFragment extends Fragment implements MyRecyclerListAdapter.CardActions{

    private MyRecyclerListAdapter myListAdapter;
    private ArrayList<PartnersEntity> partnersList = new ArrayList<>();
    private RecyclerView partners_list;
    private ProgressBar partnersPbar;
    private String filter;
    private My_Realm my_realm;
    private boolean loadPositionZero;
    private String[] categoriesList;
    private Context mContext;

    // TODO resolve the problem affecting the app wen the android system kills the app for get its resources
    /*
     * The app crash if the main activity is referenced on the get online partner function.
     * With the workaround the app goes to the card fragment and executes the code of the partners fragment
     */

    public PartnersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesList = getActivity().getResources().getStringArray(R.array.partners_categories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partners, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Answers initialization
        setAnswers();

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

        my_realm = new My_Realm(getActivity().getBaseContext());

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                    getSelectedLocalPartner(categoriesList[position]);
                else if (loadPositionZero)
                    getAsyncLocalPartner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initRecyclerView();
        if(((MainActivity) getActivity()).session.getFirstLoad() && IsOnline.isOnline(getActivity())) {
            filter = "";
            getOnlinePartners();
        }
        else
            getAsyncLocalPartner();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
                partnersPbar.setVisibility(View.GONE);
                myListAdapter.notifyDataSetChanged();

                if(getActivity() != null)
                    ((MainActivity) getActivity()).session.setFirstLoad(false);

                // Workaround
                // Set first load to shared prefs
/*                SharedPreferences sharedPRF = getActivity()
                        .getApplication().getApplicationContext()
                        .getSharedPreferences(SimplesPrefs.PREFS.toString(), 0);
                SharedPreferences.Editor editor = sharedPRF.edit();
                editor.putBoolean("isFirstTime", false);
                editor.apply();*/
            }

            @Override
            public void failure(RetrofitError error) {
                partnersPbar.setVisibility(View.GONE);
                if(isAdded() && mContext != null)
                    Toast.makeText(mContext, mContext.getString(R.string.rc_4)
                                    + " " + mContext.getString(R.string.server_error),
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
        partnersList = my_realm.getPartners(selection);
        myListAdapter.filteredLisItems(partnersList);
        partnersPbar.setVisibility(View.GONE);

    }

    private void initRecyclerView()
    {
        int rows = 1;
        if(SimplesApplication.isTablet) {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                rows = 2;
                partners_list.addItemDecoration(new GridSpacingItemDecoration(2, 20, true));
            }
            else {
                rows = 3;
                partners_list.addItemDecoration(new GridSpacingItemDecoration(3, 20, true));
            }
        }
        else{
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rows = 2;
                partners_list.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
            }
        }

        myListAdapter = new MyRecyclerListAdapter(getActivity().getApplicationContext(), partnersList,
                R.layout.t_card, PartnersFragment.this);
        //    myListAdapter.clearData();
        partners_list.setLayoutManager(new GridLayoutManager(getActivity().getBaseContext(), rows));
        partners_list.setAdapter(myListAdapter);
    }

    private RealmChangeListener callback = new RealmChangeListener() {
        @Override
        public void onChange() { // called once the query complete and on every update
            RealmResults<PartnersEntity> partnerAsync = my_realm.getPartnerAsync();
            for (PartnersEntity partner : partnerAsync) {
                partnersList.add(partner);
            }
            partnersPbar.setVisibility(View.GONE);
            myListAdapter.notifyDataSetChanged();
        }
    };

    private void setAnswers()
    {
        My_Answers my_answers = new My_Answers(((MainActivity) getActivity()).userEntity.getEmail());
        my_answers.partnersList();
    }

    @Override
    public void onItemClicked(int position) {
        Intent partnerDetail = new Intent(getActivity().getBaseContext(), PartnerDetails.class);
        SimplesApplication.currentPartner = partnersList.get(position);
        partnerDetail.putExtra("partner", partnersList.get(position).getNiu());
        startActivity(partnerDetail);
        // Animate activity transition
        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    /*    getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.frame_container, new PartnerDetailFragment())
                .addToBackStack("detail")
                .commit();*/
    }
}
