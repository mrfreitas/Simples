package pt.admedia.simples.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mrfreitas on 12/03/2016.
 */
public class PreferenceCategoryEntity extends RealmObject {

    @PrimaryKey
    private String niu;
    private String title;
    private RealmList<PreferenceEntity> childPreferences = new RealmList<>();

    @Ignore
    private boolean isActive = false;

    public PreferenceCategoryEntity() {}

    public PreferenceCategoryEntity(String niu, String title){
        this.niu = niu;
        this.title = title;
    }

//    public PreferenceCategoryEntity(JsonObject partner) {
//        if (!partner.get("niu").isJsonNull())
//            this.niu = partner.get("niu").getAsString();
//        if (!partner.get("title").isJsonNull())
//            this.title = partner.get("title").getAsString();
//        if (!partner.get("address").isJsonNull())
//            this.address = partner.get("address").getAsString();
//        if (!partner.get("email").isJsonNull())
//            this.email = partner.get("email").getAsString();
//        if (!partner.get("seo").isJsonNull())
//            this.seo = partner.get("seo").getAsString();
//        if (!partner.get("img").isJsonNull())
//            this.img = partner.get("img").getAsString();
//        if (!partner.get("postal_long").isJsonNull())
//            this.postal_long = partner.get("postal_long").getAsInt();
//        if (!partner.get("postal_code").isJsonNull())
//            this.postal_code = partner.get("postal_code").getAsInt();
//        if (!partner.get("phone").isJsonNull())
//            this.phone = partner.get("phone").getAsInt();
//
//        if (!partner.get("discounts").isJsonNull()) {
//            JsonArray discounts = partner.get("discounts").getAsJsonArray();
//            for (JsonElement d : discounts) {
//                JsonObject discount = d.getAsJsonObject();
//                this.discounts.add(new Discount(discount));
//            }
//        }
//    }


    public String getNiu() {
        return niu;
    }

    public void setNiu(String niu) {
        this.niu = niu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
