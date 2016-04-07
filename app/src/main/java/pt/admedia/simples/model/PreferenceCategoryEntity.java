package pt.admedia.simples.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mrfreitas on 12/03/2016.
 */
public class PreferenceCategoryEntity extends RealmObject {

    @PrimaryKey
    private String seo;

    private String title;
    private String description;
    private RealmList<PreferenceEntity> childPreferences = new RealmList<>();

    @Ignore
    private boolean isActive = false;

    public PreferenceCategoryEntity() {}

    public PreferenceCategoryEntity(JsonObject category){
        if (!category.get("seo").isJsonNull())
            this.seo = category.get("seo").getAsString();
        if (!category.get("name").isJsonNull())
            this.title = category.get("name").getAsString();
        if (!category.get("description").isJsonNull())
            this.description = category.get("description").getAsString();

        // Preferences
        if (!category.get("items").isJsonNull()) {
            JsonArray items = category.get("items").getAsJsonArray();
            for (JsonElement item : items) {
                JsonObject json = item.getAsJsonObject();
                PreferenceEntity pref = new PreferenceEntity(json);
                childPreferences.add(pref);
            }
        }
    }

    public String getSeo() {
        return seo;
    }
    public void setSeo(String seo) {
        this.seo = seo;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<PreferenceEntity> getChildPreferences() {
        return childPreferences;
    }

    public void setChildPreferences(RealmList<PreferenceEntity> childPreferences) {
        this.childPreferences = childPreferences;
    }

    public boolean isActive(){
        isActive = true;
        for (PreferenceEntity pe : childPreferences){
            if(!pe.isActive())
                isActive = false;
        }

        return isActive;
    }

    public void setActive(boolean state){
        for (PreferenceEntity pe : childPreferences){
            pe.setActive(state);
        }
    }
}