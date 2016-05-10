package pt.admedia.simples.api;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * mrfreitas
 * Date: 22/07/2015
 * Time: 13:30
 */
public interface UserAPI {

    @POST("/login/index/{user}/{pass}")
    void login(@Path("user") String user, @Path("pass") String pass, Callback<JsonObject> response);

    @POST("/login/face")
    void loginFace(@Query("id") String id, Callback<JsonObject> response);

    @FormUrlEncoded
    @POST("/lead/mobile")
    void leadMobile(@Field("firstname") String firstName,
                       @Field("lastname") String lastName,
                       @Field("sex") String sex,
                       @Field("facebookuser") String faceId,
                       @Field("birthday") String birth,
                       @Field("mobile") int phone,
                       @Field("email") String email,
                       @Field("address") String address,
                       @Field("long") int postal_long,
                       @Field("code") int code,
                       @Field("region") String region,
                       Callback<JsonObject> response);

    @POST("/login/out")
    void logOut(@Query("token") String token, Callback<JsonObject> response);

    @POST("/login/auth")
    void loginAuth(@Query("user") String user, @Query("pass") String pass, Callback<JsonObject> response);


}