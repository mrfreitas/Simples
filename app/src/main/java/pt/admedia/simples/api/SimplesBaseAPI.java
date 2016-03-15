package pt.admedia.simples.api;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by mrfreitas on 08/03/2016.
 */
public interface SimplesBaseAPI {



    @POST("/login/auth")
    void login(@Query("user") String user, @Query("pass") String pass, Callback<JsonObject> response);
}
