package pt.admedia.simples.model;

import com.google.gson.JsonObject;

import io.realm.RealmObject;

/**
 * Created by mrfreitas on 11/03/2016.
 */
public class Discount extends RealmObject {

    private String name, description, type;
    private int value;

    public Discount() {}

    public Discount(JsonObject discount) {
        if (!discount.get("name").isJsonNull())
            this.name = discount.get("name").getAsString();
        if (!discount.get("description").isJsonNull())
            this.description = discount.get("description").getAsString();
        if (!discount.get("type").isJsonNull())
            this.type = discount.get("type").getAsString();
        if (!discount.get("value").isJsonNull())
            try {
                this.value = discount.get("value").getAsInt();
            }
            catch (NumberFormatException e) {
                this.value = 0;
            }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
