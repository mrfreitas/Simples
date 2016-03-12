package pt.admedia.simples.lib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mrfreitas on 09/03/2016.
 */
public class Session {

    private static final String PREFS = SimplesPrefs.PREFS.toString();
    private SharedPreferences sharedPRF;

    public Session(Context context) {
        sharedPRF = context.getSharedPreferences(PREFS, 0);
    }

    public String getToken()
    {
        return sharedPRF.getString("token", "");
    }

    public void setToken(String token)
    {
        // Save user session token
        SharedPreferences.Editor editor = sharedPRF.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public void setFaceLogin(boolean isFaceLogin)
    {
        SharedPreferences.Editor editor = sharedPRF.edit();
        editor.putBoolean("isFaceLogin", isFaceLogin);
        editor.apply();
    }

    public boolean getFaceLogin()
    {
        return sharedPRF.getBoolean("isFaceLogin", false);
    }

}
