package pt.admedia.simples;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.SimplesBaseAPI;
import pt.admedia.simples.api.UserAPI;
import pt.admedia.simples.lib.IsOnline;
import pt.admedia.simples.lib.My_Answers;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.lib.SimplesPrefs;
import pt.admedia.simples.model.My_Realm;
import pt.admedia.simples.model.UserEntity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FirstTime extends AppCompatActivity {


    private LoginButton faceLoginBt;
    private Button customFBBt, emailLoginBt, requestCardBt;
    private CallbackManager callbackManager;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * if @Facebook_LoginButton is declared, the initialization of @facebook_SDK must be placed before
         * @setContentView
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_first_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assignViews();

        /*
         * Simples server need to give authorization before the user can login.
         * This method request the server authorization.
         */

        // TODO face login already return the token
        //  requestAuthorization();

        progress=new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.authenticating));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        // Request card
        requestCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requestCard = new Intent(FirstTime.this, RequestCard.class);
                startActivity(requestCard);
            }
        });

        // Login with an email and password
        emailLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailLogin = new Intent(FirstTime.this, Login.class);
                startActivity(emailLogin);
            }
        });

        faceLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();

            }
        });
        // Login/register with facebook
        faceLoginBt.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                if(IsOnline.isOnline(FirstTime.this))
                    serverLoginFace(accessToken);
            }

            @Override
            public void onCancel() {
                progress.dismiss();

            }

            @Override
            public void onError(FacebookException exception) {
                if(IsOnline.isOnline(FirstTime.this))
                    Toast.makeText(FirstTime.this, getBaseContext().getResources().getString(R.string
                        .facebook_error), Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        if (v == customFBBt) {
            faceLoginBt.performClick();
        }
    }

    private void assignViews()
    {
        callbackManager = CallbackManager.Factory.create();
        requestCardBt = (Button) findViewById(R.id.request_card_button);
        emailLoginBt = (Button) findViewById(R.id.email_login_button);
        customFBBt = (Button) findViewById(R.id.custom_fb_button);
        faceLoginBt = (LoginButton) findViewById(R.id.face_login_button);
        List< String > permissionNeeds = Arrays.asList("email", "public_profile", "user_birthday", "user_friends");
        faceLoginBt.setReadPermissions(permissionNeeds);
    }

    private void requestAuthorization()
    {
        String user = SimplesPrefs.USER.toString();
        String pass = SimplesPrefs.PASS.toString();

        // Create user account request
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        SimplesBaseAPI api = adapter.create(SimplesBaseAPI.class);
        api.login(user, pass, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                // Save user session
                Session session = new Session(FirstTime.this);
                if (jsonResponse.has("token"))
                    session.setToken(jsonResponse.get("token").getAsString());
            }

            @Override
            public void failure(RetrofitError error) {
                /*
                 * This request shouldn't show ani message to the user
                 */
            }
        });

    }

    private void serverLoginFace(final AccessToken accessToken)
    {
        String faceUserId = accessToken.getUserId();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        UserAPI api = adapter.create(UserAPI.class);
        api.loginFace(faceUserId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                JsonObject status = jsonResponse.getAsJsonObject("status");
                /*
                 * If the user do not exist (value = 0) on the simples platform, then proceed with the
                 * registration on the simples platform. If the user already exist, the registration process ends.
                 */
                if (status.get("value").getAsInt() == 0) {
                    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject faceUserData, GraphResponse response) {
                            loginRegister(faceUserData);
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, gender, email, birthday, location");
                    request.setParameters(parameters);
                    request.executeAsync();
                } else {
                    // Login with success
                    faceServerSuccess(jsonResponse, true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(FirstTime.this, getBaseContext().getString(R.string.rc_1) +
                                " " + getBaseContext().getString(R.string.server_error),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginRegister(JSONObject faceUserData) {
        String firstName= "", lastName= "", email= "", faceId= "", birthDate= "", gender = "", sex = "";
        String address = "", region = "";
        int postal_long = 0, code = 0;
        try {
            if (faceUserData.has("first_name"))
                firstName = faceUserData.getString("first_name");
            if (faceUserData.has("last_name"))
                lastName = faceUserData.getString("last_name");
            if (faceUserData.has("email"))
                email = faceUserData.getString("email");
            if (faceUserData.has("id"))
                faceId = faceUserData.getString("id");
            if (faceUserData.has("birthday"))
                birthDate = faceUserData.getString("birthday");
            if (faceUserData.has("gender"))
                gender = faceUserData.getString("gender");
        }
        catch (JSONException e){
            Toast.makeText(FirstTime.this, getBaseContext().getString(R.string.rc_5) +
                            " " + getBaseContext().getString(R.string.server_error),
                    Toast.LENGTH_SHORT).show();
        }

        if(gender.equals("male"))
            sex = "m";
        else if(sex.equals("female"))
            sex = "f";

        birthDate = convertDate(birthDate);
        // At this time is not possible to get the mobile from facebook api
        int phone = 0;
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        UserAPI api = adapter.create(UserAPI.class);
        api.loginRegister(firstName, lastName, sex, faceId, birthDate, phone, email, address, postal_long, code, region, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                faceServerSuccess(jsonResponse, false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(FirstTime.this, getBaseContext().getString(R.string.rc_2) +
                                " " + getBaseContext().getString(R.string.server_error),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void startMainActivity()
    {
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(main);
    }

    private String convertDate(String stringDate)
    {
        DateFormat originalFormat = new SimpleDateFormat("dd/mm/yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = originalFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetFormat.format(date);
    }

    private void faceServerSuccess(JsonObject jsonResponse, boolean isLogin)
    {
        // Save user session
        Session session = new Session(FirstTime.this);
        if (jsonResponse.has("token"))
            session.setToken(jsonResponse.get("token").getAsString());
        session.setFaceLogin(true);
        // Persist user
        My_Realm my_realm = new My_Realm(FirstTime.this);
        UserEntity newUser = new UserEntity(jsonResponse);
        my_realm.setUser(newUser);
        // Login is finished
        progress.dismiss();
        // Answers
        My_Answers my_answers = new My_Answers(newUser.getEmail());
        if(isLogin)
            my_answers.logIn("Facebook");
        else
            my_answers.signUp("Facebook", newUser.getMobile());
        startMainActivity();
    }

}
