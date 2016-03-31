package pt.admedia.simples;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.model.My_Realm;

/**
 * Created by mrfreitas on 04/03/2016.
 */
public class SimplesApplication extends Application {

    public static boolean isTablet = false;
    public static Typeface fontAwesome;

    public boolean isOnline;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getBaseContext());
        Fabric.with(this, new Crashlytics());

        printHasKey();
        buildDatabase();

        //access all over the app to know the device context it's running (tablet vs phone)
        isTablet = getResources().getBoolean(R.bool.isTablet);

        fontAwesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Session session = new Session(this);
        session.setFirstLoad(true);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void printHasKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "pt.admedia.simples",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public Realm buildDatabase(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        try {
            Realm realm = Realm.getInstance(realmConfiguration);
            isOnline(realmConfiguration, realm);
            return realm;
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }
    }

    public void isOnline(RealmConfiguration realmConfiguration, Realm realm)
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting())
        {
            My_Realm my_realm = new My_Realm(this);
            my_realm.removePartners();
            isOnline = true;
        }
        else
            isOnline = false;
    }
}
