package pt.admedia.simples.model;

import com.google.gson.JsonObject;

import io.realm.RealmObject;

/**
 * Created by mrfreitas on 28/03/2016.
 */
public class AreaEntity extends RealmObject{
    private String name;
    private String seo;
    private String description;

    public AreaEntity() {}

    public AreaEntity(JsonObject area) {
        if (!area.get("name").isJsonNull())
            this.name = area.get("name").getAsString();
        if (!area.get("seo").isJsonNull())
            this.seo = area.get("seo").getAsString();
        if (!area.get("description").isJsonNull())
            this.description = area.get("description").getAsString();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSeo() {
        return seo;
    }
    public void setSeo(String seo) {
        this.seo = seo;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
