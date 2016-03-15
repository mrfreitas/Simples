package pt.admedia.simples.model;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by mrfreitas on 09/03/2016.
 */
public class My_Realm {

    private Realm realm;
    private RealmResults<PartnersEntity> partnerAsync;

    public My_Realm(Context context) {
        this.realm = Realm.getInstance(context);
    }

    public RealmResults<PartnersEntity> getPartnerAsync() {
        return partnerAsync;
    }

    public void setUser(UserEntity userEntity)
    {
        // Persist user data
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userEntity);
        realm.commitTransaction();
    }

    public UserEntity getUser()
    {
        realm.beginTransaction();
        // Execute the query:
        RealmQuery<UserEntity> query = realm.where(UserEntity.class);
        UserEntity result = query.findFirst();
        realm.commitTransaction();
        return result;
    }

    public void removeUser()
    {
        RealmResults<UserEntity> results = realm.where(UserEntity.class).findAll();
        // Delete user
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }

    public void setPartners(PartnersEntity partner)
    {
        // Persist user data
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(partner);
        realm.commitTransaction();
    }

    public void getAsyncPartners(RealmChangeListener callback)
    {
        //realm.beginTransaction();
        partnerAsync = realm.where(PartnersEntity.class).findAllAsync();
        partnerAsync.addChangeListener(callback);
        //realm.commitTransaction();
    }

    public ArrayList<PartnersEntity> getPartners(String selection)
    {
        RealmQuery<PartnersEntity> query = realm.where(PartnersEntity.class);
        query.equalTo("seo", selection);
        realm.beginTransaction();
        RealmResults<PartnersEntity> pList = query.findAll();
        ArrayList<PartnersEntity> partnersList = new ArrayList<>();
        realm.commitTransaction();
        for (PartnersEntity partner : pList) {
            partnersList.add(partner);
        }
        return partnersList;
    }

}
