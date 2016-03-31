package pt.admedia.simples.model;

import com.google.gson.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mrfreitas on 12/03/2016.
 */
public class PreferenceEntity extends RealmObject {

    @PrimaryKey
    private String seo;
    private String title;
    private String description;
    private boolean isActive = false;

    public PreferenceEntity() {}

    public PreferenceEntity(JsonObject preferency){
        if (!preferency.get("seo").isJsonNull())
            this.seo = preferency.get("seo").getAsString();
        if (!preferency.get("name").isJsonNull())
            this.title = preferency.get("name").getAsString();
        if (!preferency.get("description").isJsonNull())
            this.description = preferency.get("description").getAsString();
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

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}