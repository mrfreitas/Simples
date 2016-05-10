package pt.admedia.simples.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.JsonObject;

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

    public void setFirstLoad(boolean isFirstTime)
    {
        SharedPreferences.Editor editor = sharedPRF.edit();
        editor.putBoolean("isFirstTime", isFirstTime);
        editor.apply();
    }

    public boolean getFirstLoad()
    {
        return sharedPRF.getBoolean("isFirstTime", true);
    }

    public void setPartnerCategory(int category)
    {
        SharedPreferences.Editor editor = sharedPRF.edit();
        editor.putInt("category", category);
        editor.apply();
    }

    public int getPartnerCategory()
    {
        return sharedPRF.getInt("category", 0);
    }

    public void setCurrentPartner(String niu)
    {
        SharedPreferences.Editor editor = sharedPRF.edit();
        editor.putString("niu", niu);
        editor.apply();
    }

    public String getCurrentPartner()
    {
        return sharedPRF.getString("niu", "");
    }

    public void setWaitingPayment(boolean isWaitingPayment)
    {
        SharedPreferences.Editor editor = sharedPRF.edit();
        editor.putBoolean("isWaitingPayment", isWaitingPayment);
        editor.apply();
    }

    public boolean getWaitingPayment()
    {
        return sharedPRF.getBoolean("isWaitingPayment", false);
    }

    public void setPaymentData(JsonObject paymentData) {
            SharedPreferences.Editor editor = sharedPRF.edit();
        if (!paymentData.get("Referencia").isJsonNull())
                editor.putString("reference", paymentData.get("Referencia").getAsString());
        if (!paymentData.get("Entidade").isJsonNull())
                editor.putString("entity", paymentData.get("Entidade").getAsString());
        if (!paymentData.get("Valor").isJsonNull())
                editor.putString("value", paymentData.get("Valor").getAsString());
        editor.apply();
    }

    public String getPaymentEntity()
    {
        return sharedPRF.getString("entity", "");
    }

    public String getPaymentReference()
    {
        return sharedPRF.getString("reference", "");
    }
    public String getPaymentValue()
    {
        return sharedPRF.getString("value", "");
    }
}
