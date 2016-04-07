package pt.admedia.simples;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.model.My_Realm;
import pt.admedia.simples.model.PartnersEntity;

/**
 * Created by mrfreitas on 04/03/2016.
 */
public class SimplesApplication extends Application {

    public static boolean isTablet = false;
    public static Typeface fontAwesome;
    private float dpHeight, dpWidth;
    private double ratio = 1.50; // racio 3:2
    public static int customHeight, customHeightDetails;
    public static PartnersEntity currentPartner = null;

    public boolean isOnline;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getBaseContext());
        Fabric.with(this, new Crashlytics());

        //printHasKey();
        buildDatabase();

        //access all over the app to know the device context it's running (tablet vs phone)
        isTablet = getResources().getBoolean(R.bool.isTablet);

        fontAwesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Session session = new Session(this);
        session.setFirstLoad(true);
        session.setPartnerCategory(0);

        // Calculate the height for images
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        this.dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if(!isTablet)
            customHeight = (int) Math.round((dpWidth/ratio) * displayMetrics.density);
        else {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                customHeight = (int) Math.round(((dpWidth / ratio) * displayMetrics.density) / 2);
            else
                customHeight = (int) Math.round(((dpHeight / ratio) * displayMetrics.density) / 2);
        }

        customHeightDetails = (int) Math.round(((dpWidth/ratio) * displayMetrics.density));



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
            isOnline();
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

    public void isOnline()
    {
        Session session = new Session(this);
        String currentPartner = session.getCurrentPartner();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting() && currentPartner.equals(""))
        {
            My_Realm my_realm = new My_Realm(this);
            my_realm.removePartners();
            isOnline = true;
        }
        else
            isOnline = false;
    }
}
