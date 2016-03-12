package pt.admedia.simples.model;

import com.google.gson.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mrfreitas on 08/03/2016.
 */
public class UserEntity extends RealmObject implements Imodel {
    private String firstName, lastName, address, postal_long, postal_short, region;
    private String faceUserId, profileImage;
    private int mobile;
    private long cardNumber;
    private String cardValDate, birthDay;

    @PrimaryKey
    private String email;

    public UserEntity() {}

    public UserEntity(JsonObject userJason) {

        JsonObject user = null;
        if(userJason.has("user"))
           user = userJason.get("user").getAsJsonObject();
        if(user != null) {
            // User data
            if (!user.get("firstname").isJsonNull())
                this.firstName = user.get("firstname").getAsString();
            if (!user.get("lastname").isJsonNull())
                this.lastName = user.get("lastname").getAsString();
            if (!user.get("email").isJsonNull())
                this.email = user.get("email").getAsString();
            if (!user.get("mobile").isJsonNull())
                this.mobile = user.get("mobile").getAsInt();
            if (!user.get("address").isJsonNull())
                this.address = user.get("address").getAsString();
            if (!user.get("postal_long").isJsonNull())
                this.postal_long = user.get("postal_long").getAsString();
            if (!user.get("postal_code").isJsonNull())
                this.postal_short = user.get("postal_code").getAsString();
            if (!user.get("region").isJsonNull())
                this.region = user.get("region").getAsString();
            if (!user.get("facebookuser").isJsonNull())
                this.faceUserId = user.get("facebookuser").getAsString();

            // Card data
            JsonObject card = null;
            if (user.has("card"))
                card = user.get("card").getAsJsonObject();
            if (card != null) {
                if (!card.get("cardnumber").isJsonNull())
                    this.cardNumber = card.get("cardnumber").getAsLong();
                if (!card.get("valid").isJsonNull())
                    this.cardValDate = card.get("valid").getAsString();
            }

            // Info data
            JsonObject info = null;
            if (user.has("info"))
                info = user.get("info").getAsJsonObject();
            if (info != null) {
                if (!info.get("profile").isJsonNull())
                    this.profileImage = info.get("profile").getAsString();
                if (!info.get("birthday").isJsonNull())
                    this.birthDay = info.get("birthday").getAsString();
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_long() {
        return postal_long;
    }
    public void setPostal_long(String postal_long) {
        this.postal_long = postal_long;
    }

    public String getPostal_short() {
        return postal_short;
    }
    public void setPostal_short(String postal_short) {
        this.postal_short = postal_short;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getFaceUserId() {
        return faceUserId;
    }
    public void setFaceUserId(String faceUserId) {
        this.faceUserId = faceUserId;
    }

    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getMobile() {
        return mobile;
    }
    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public long getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardValDate() {
        return cardValDate;
    }
    public void setCardValDate(String cardValDate) {
        this.cardValDate = cardValDate;
    }

    public String getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

}
