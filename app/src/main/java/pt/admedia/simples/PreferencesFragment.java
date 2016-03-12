package pt.admedia.simples;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;




public class PreferencesFragment extends Fragment implements Switch.OnCheckedChangeListener{

    Switch restaurant_switch, switch_peixe, switch_carnes, switch_portuguesa, switch_japones, switch_chines;
    Switch switch_italiano, switch_indiano, switch_contemporanea, switch_fast, switch_vegetariano, switch_petiscos;
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
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spinner categories = (Spinner) getActivity().findViewById(R.id.categories);
        categories.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.preferences);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        restaurant_switch = (Switch) getActivity().findViewById(R.id.restaurant_switch);
        switch_peixe = (Switch) getActivity().findViewById(R.id.switch_peixe);
        switch_carnes = (Switch) getActivity().findViewById(R.id.switch_carnes);
        switch_portuguesa = (Switch) getActivity().findViewById(R.id.switch_portuguesa);
        switch_japones = (Switch) getActivity().findViewById(R.id.switch_japones);
        switch_chines = (Switch) getActivity().findViewById(R.id.switch_chines);
        switch_italiano = (Switch) getActivity().findViewById(R.id.switch_italiano);
        switch_indiano = (Switch) getActivity().findViewById(R.id.switch_indiano);
        switch_contemporanea = (Switch) getActivity().findViewById(R.id.switch_contemporanea);
        switch_fast = (Switch) getActivity().findViewById(R.id.switch_fast);
        switch_vegetariano = (Switch) getActivity().findViewById(R.id.switch_vegetariano);
        switch_petiscos = (Switch) getActivity().findViewById(R.id.switch_petiscos);

        restaurant_switch.setOnCheckedChangeListener(this);
        switch_peixe.setOnCheckedChangeListener(this);
        switch_carnes.setOnCheckedChangeListener(this);
        switch_portuguesa.setOnCheckedChangeListener(this);
        switch_japones.setOnCheckedChangeListener(this);
        switch_chines.setOnCheckedChangeListener(this);
        switch_italiano.setOnCheckedChangeListener(this);
        switch_indiano.setOnCheckedChangeListener(this);
        switch_contemporanea.setOnCheckedChangeListener(this);
        switch_fast.setOnCheckedChangeListener(this);
        switch_vegetariano.setOnCheckedChangeListener(this);
        switch_petiscos.setOnCheckedChangeListener(this);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        switch (id)
        {
            case R.id.restaurant_switch:
                switch_peixe.setChecked(isChecked);
                switch_carnes.setChecked(isChecked);
                switch_portuguesa.setChecked(isChecked);
                switch_japones.setChecked(isChecked);
                switch_chines.setChecked(isChecked);
                switch_italiano.setChecked(isChecked);
                switch_indiano.setChecked(isChecked);
                switch_contemporanea.setChecked(isChecked);
                switch_fast.setChecked(isChecked);
                switch_vegetariano.setChecked(isChecked);
                switch_petiscos.setChecked(isChecked);
                break;
        }
    }
}
