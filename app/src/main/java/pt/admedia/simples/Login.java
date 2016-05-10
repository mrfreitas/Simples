package pt.admedia.simples;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.UserAPI;
import pt.admedia.simples.lib.IsOnline;
import pt.admedia.simples.lib.My_Answers;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.model.My_Realm;
import pt.admedia.simples.model.UserEntity;
import pt.admedia.simples.validator.ValidatorFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    // UI references.
    private AutoCompleteTextView loginEmail;
    private EditText mPasswordView;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the login form.
        loginEmail = (AutoCompleteTextView) findViewById(R.id.login_email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        progress=new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.authenticating));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

    }



    private void populateAutoComplete() {
        if (!mayRequestContacts())
            return;
        final Account[] accounts = AccountManager.get(this).getAccounts();
        final Set<String> emailSet = new HashSet<>();
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }
        }
        List<String> emails = new ArrayList<>(emailSet);
        loginEmail.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, emails));
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(loginEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        ValidatorFactory validatorFactory = new ValidatorFactory(getApplicationContext());

        String email = loginEmail.getText().toString();
        String password = mPasswordView.getText().toString();

        // Email Validate
        validatorFactory.emailValidate(loginEmail, true);
        //Password Validate
        validatorFactory.emptyValidate(mPasswordView, true);

        if (validatorFactory.formValidate())
        {
            if(IsOnline.isOnline(Login.this)) {
                progress.show();
                loginAuth(email, password);
            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                break;
        }
        return true;
    }

    private void loginAuth(String email, String password) {
        // Create user account request
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        UserAPI api = adapter.create(UserAPI.class);
        api.loginAuth(email, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                JsonObject status = jsonResponse.get("status").getAsJsonObject();
                int value = status.get("value").getAsInt();
                if(value == 1) {
                    // Save user session
                    Session session = new Session(Login.this);
                    if (jsonResponse.has("token"))
                        session.setToken(jsonResponse.get("token").getAsString());
                    // Persist user
                    My_Realm my_realm = new My_Realm(Login.this);
                    UserEntity newUser = new UserEntity(jsonResponse);
                    my_realm.setUser(newUser);
                    progress.dismiss();
                    startMainActivity(newUser.getEmail());
                }
                else
                {
                    progress.dismiss();
                    String desc = status.get("description").getAsString();
                    Toast.makeText(Login.this, desc, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Login.this, getBaseContext().getString(R.string.rc_7) + " " +
                        getBaseContext().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        });
    }

    private void startMainActivity(String email)
    {
        My_Answers my_answers = new My_Answers(email);
        my_answers.logIn("email");
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(main);
    }
}

