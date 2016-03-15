package pt.admedia.simples.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mrfreitas on 11/03/2016.
 */
public class PartnersEntity extends RealmObject{

    @PrimaryKey
    private String niu;
    private String title, address, email, seo, img;
    private int postal_long, postal_code, phone;

    private RealmList<Discount> discounts = new RealmList<>();

    public PartnersEntity() {}

    public PartnersEntity(JsonObject partner) {
        if (!partner.get("niu").isJsonNull())
            this.niu = partner.get("niu").getAsString();
        if (!partner.get("title").isJsonNull())
            this.title = partner.get("title").getAsString();
        if (!partner.get("address").isJsonNull())
            this.address = partner.get("address").getAsString();
        if (!partner.get("email").isJsonNull())
            this.email = partner.get("email").getAsString();
        if (!partner.get("seo").isJsonNull())
            this.seo = partner.get("seo").getAsString();
        if (!partner.get("img").isJsonNull())
            this.img = partner.get("img").getAsString();
        if (!partner.get("postal_long").isJsonNull())
            this.postal_long = partner.get("postal_long").getAsInt();
        if (!partner.get("postal_code").isJsonNull())
            this.postal_code = partner.get("postal_code").getAsInt();
        if (!partner.get("phone").isJsonNull())
            this.phone = partner.get("phone").getAsInt();

        if (!partner.get("discounts").isJsonNull()) {
            JsonArray discounts = partner.get("discounts").getAsJsonArray();
            for (JsonElement d : discounts) {
                JsonObject discount = d.getAsJsonObject();
                this.discounts.add(new Discount(discount));
            }
        }
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPostal_long() {
        return postal_long;
    }

    public void setPostal_long(int postal_long) {
        this.postal_long = postal_long;
    }

    public int getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(int postal_code) {
        this.postal_code = postal_code;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public RealmList<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(RealmList<Discount> discount) {
        this.discounts = discount;
    }
}
