package pt.admedia.simples.model;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by mrfreitas on 09/03/2016.
 */
public class My_Realm {

    private Context context;
    private Realm realm;

    public My_Realm(Context context) {
        this.context = context;
        this.realm = Realm.getInstance(this.context);
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
}
