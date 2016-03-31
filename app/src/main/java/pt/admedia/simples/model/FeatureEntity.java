package pt.admedia.simples.model;

import com.google.gson.JsonObject;

import io.realm.RealmObject;

/**
 * Created by mrfreitas on 28/03/2016.
 */
public class FeatureEntity  extends RealmObject{

    private String name;
    private String description;
    private String imgUrl;

    public FeatureEntity() {
    }

    public FeatureEntity(JsonObject feature) {
        if (!feature.get("name").isJsonNull())
            this.name = feature.get("name").getAsString();
        if (!feature.get("description").isJsonNull())
            this.description = feature.get("description").getAsString();
        if (!feature.get("img").isJsonNull())
            this.imgUrl = feature.get("img").getAsString();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
