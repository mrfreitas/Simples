package pt.admedia.simples.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

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
    @POST("/login/register")
    void loginRegister(@Field("firstname") String firstName,
                       @Field("lastname") String lastName,
                       @Field("sex") String sex,
                       @Field("faceuser") String faceId,
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
}