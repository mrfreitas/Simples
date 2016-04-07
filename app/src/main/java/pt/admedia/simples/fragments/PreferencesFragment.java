package pt.admedia.simples.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.realm.RealmList;
import pt.admedia.simples.R;
import pt.admedia.simples.adapters.PreferencesAdapter;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.PartnersAPI;
import pt.admedia.simples.model.PreferenceCategoryEntity;
import pt.admedia.simples.model.PreferenceEntity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PreferencesFragment extends Fragment implements PreferencesAdapter.PreferenceSelectionListener {
    private RecyclerView preferencesRV;
    private PreferencesAdapter mAdapter;
    private ProgressBar prefsPbar;
    private RealmList list = new RealmList();
    private View rootview;

    public PreferencesFragment() {
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
        rootview =  inflater.inflate(R.layout.fragment_preferences, container, false);

        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spinner categories = (Spinner) getActivity().findViewById(R.id.categories);
        categories.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.preferences);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        prefsPbar = (ProgressBar) getActivity().findViewById(R.id.prefsPbar);
        prefsPbar.setVisibility(View.VISIBLE);
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        PartnersAPI api = adapter.create(PartnersAPI.class);
        api.partnerCategories("", "", 0, 100, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                JsonArray items = jsonResponse.get("items").getAsJsonArray();
                for (JsonElement categoryItem : items) {
                    JsonObject category = categoryItem.getAsJsonObject();
                    PreferenceCategoryEntity prefCat = new PreferenceCategoryEntity(category);
                    list.add(prefCat);

                }
                //   RealmList<PreferenceEntity> userPrefs = ((MainActivity) getActivity()).userEntity.getPreferences();
                preferencesRV = (RecyclerView) rootview.findViewById(R.id.preferences_rv);
                mAdapter = new PreferencesAdapter(list, R.layout.list_item_preference, PreferencesFragment.this);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                preferencesRV.setLayoutManager(llm);
                preferencesRV.setAdapter(mAdapter);
                prefsPbar.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onParentSelected(PreferenceCategoryEntity parent, boolean state) {
        // TODO: 13/03/2016 save on api
    }

    @Override
    public void onChildSelected(PreferenceCategoryEntity parent, PreferenceEntity child, boolean state) {
        // TODO: 13/03/2016 save on api
    }

}
