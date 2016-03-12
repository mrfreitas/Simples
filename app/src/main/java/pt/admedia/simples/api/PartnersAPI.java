package pt.admedia.simples.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by mrfreitas on 10/03/2016.
 */
public interface PartnersAPI {

    @POST("/partner/items")
    void partnerItems(@Query("filter") String filter,
                   @Query("start") int start,
                   @Query("amount") int amount,
                   Callback<JsonObject> response);
}
