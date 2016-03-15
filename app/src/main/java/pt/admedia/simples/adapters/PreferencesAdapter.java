package pt.admedia.simples.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import io.realm.RealmList;
import pt.admedia.simples.R;
import pt.admedia.simples.SimplesApplication;
import pt.admedia.simples.model.PreferenceCategoryEntity;
import pt.admedia.simples.model.PreferenceEntity;

/**
 * Created by mrfreitas on 06/09/2015.
 */
public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {

    private RealmList elements;
    private int resLayout;
    private PreferenceSelectionListener listener;
    private boolean onBind;

    public PreferencesAdapter(RealmList items, int textViewResourceId, PreferenceSelectionListener listener) {
        this.elements = new RealmList();
        for (Object o : items){
            if (o instanceof PreferenceCategoryEntity){
                this.elements.add((PreferenceCategoryEntity)o);
                for (Object child : ((PreferenceCategoryEntity) o).getChildPreferences()){
                    this.elements.add((PreferenceEntity)child);
                }
            }
        }
        this.resLayout = textViewResourceId;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(resLayout, parent,false);

        // create ViewHolder
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        onBind = true;
        if(elements.get(position) instanceof PreferenceEntity){
            final PreferenceEntity preference = (PreferenceEntity)elements.get(position);
            viewHolder.title.setText(preference.getTitle());
            viewHolder.title.setPadding(25, 0, 0, 0);
            viewHolder.title.setTypeface(SimplesApplication.fontAwesome);
            viewHolder.switchPreferences.setChecked(preference.isActive());
            // TODO: 13/03/2016 extra styling of child preferences
        }else {
            final PreferenceCategoryEntity preferenceCategory = (PreferenceCategoryEntity)elements.get(position);
            viewHolder.title.setText(preferenceCategory.getTitle());
            viewHolder.title.setPadding(5, 0, 0, 0);
            viewHolder.switchPreferences.setChecked(preferenceCategory.isActive());
            viewHolder.title.setTypeface(SimplesApplication.fontAwesome, Typeface.BOLD);
            viewHolder.switchPreferences.setChecked(preferenceCategory.isActive());
            // TODO: 13/03/2016 extra styling of parent preferences (categories)
        }
        onBind = false;
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public interface PreferenceSelectionListener{
        void onParentSelected(PreferenceCategoryEntity parent, boolean state);
        void onChildSelected(PreferenceCategoryEntity parent, PreferenceEntity child, boolean state);
    }

    private PreferenceCategoryEntity getParentCategory(String childId){
        for (Object ro : elements){
            if(ro instanceof PreferenceCategoryEntity){
                for (PreferenceEntity rochildren : ((PreferenceCategoryEntity)ro).getChildPreferences()){
                    if(rochildren.getNiu().equals(childId))
                        return ((PreferenceCategoryEntity)ro);
                }
            }
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        public TextView title;
        public Switch switchPreferences;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView.findViewById(R.id.pref_title);
            switchPreferences = (Switch) itemLayoutView.findViewById(R.id.pref_switch);
            switchPreferences.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(listener != null && !onBind){
                if(elements.get(getAdapterPosition()) instanceof PreferenceCategoryEntity){
                    final PreferenceCategoryEntity pce = ((PreferenceCategoryEntity)elements.get(getAdapterPosition()));
                    listener.onParentSelected(pce,isChecked);
                    pce.setActive(isChecked);
                    notifyDataSetChanged();
                }else{
                    final PreferenceEntity pe = ((PreferenceEntity)elements.get(getAdapterPosition()));
                    listener.onChildSelected(getParentCategory(pe.getNiu()), pe,isChecked);
                    pe.setActive(isChecked);
                    notifyDataSetChanged();
                }
            }
        }
    }
}
