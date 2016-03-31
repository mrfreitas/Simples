package pt.admedia.simples;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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


public class RequestCard extends AppCompatActivity implements LoaderCallbacks<Cursor>
{
    private Button requestCard;
    private EditText firstNameTv, lastNameTv, mobileTv, birthDateTv, addressTv, postalCodeTv, parishTv;
    private AutoCompleteTextView emailTv;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private ValidatorFactory validatorFactory;
    private RadioButton genderMale, genderFemale;

    //Id to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;
    public static final String ENDPOINT = BaseURL.BASE_URL.toString();
    private ProgressDialog progress;
    UserAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validatorFactory = new ValidatorFactory(getApplicationContext());
        assignViews();

        progress=new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.authenticating));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        // Birth date handler
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        birthDateTv.setKeyListener(null); // Input Text protected
        birthDateTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(RequestCard.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        birthDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestCard.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Adapter for the requests
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        api = adapter.create(UserAPI.class);

        requestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatorFactory.formValidate() && IsOnline.isOnline(RequestCard.this)) {
                    progress.show();
                    // Create user account request
                    String firstName = firstNameTv.getText().toString();
                    String lastName = lastNameTv.getText().toString();
                    int mobile;
                    if(!mobileTv.getText().toString().equals(""))
                        mobile = Integer.parseInt(mobileTv.getText().toString());
                    else
                        mobile = 0;
                    String email = emailTv.getText().toString();
                    String birthDate = birthDateTv.getText().toString();
                    String gender = "";
                    if(genderMale.isChecked())
                        gender = "m";
                    else if(genderFemale.isChecked())
                        gender = "f";
                    String address = addressTv.getText().toString();
                    int[] postalCode = decodePostalCode(postalCodeTv.getText().toString());
                    String parish = parishTv.getText().toString();
                    String faceId = "";

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(BaseURL.BASE_URL.toString())
                            .build();
                    UserAPI api = adapter.create(UserAPI.class);
                    api.loginRegister(firstName, lastName, gender, faceId, birthDate, mobile, email, address, postalCode[0], postalCode[1], parish,  new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonResponse, Response response) {
                            serverSuccess(jsonResponse);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            progress.dismiss();
                            Toast.makeText(RequestCard.this, getBaseContext().getString(R.string.rc_6) +
                                            " " + getBaseContext().getString(R.string.server_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
/*        getMenuInflater().inflate(R.menu.menu_create_account, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings)
        {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void assignViews() {
        firstNameTv = (EditText) findViewById(R.id.firstName);
        validatorFactory.nameValidate(firstNameTv, true);

        lastNameTv = (EditText) findViewById(R.id.lastName);
        validatorFactory.nameValidate(lastNameTv, true);

        mobileTv = (EditText) findViewById(R.id.mobile);
        validatorFactory.mobileValidate(mobileTv, true);

        // Autocomplete field
        emailTv = (AutoCompleteTextView) findViewById(R.id.regist_email);
        populateAutoComplete();
        validatorFactory.emailValidate(emailTv, true);

        birthDateTv = (EditText) findViewById(R.id.birthDate);

        genderMale = (RadioButton)findViewById(R.id.male);
        genderFemale = (RadioButton) findViewById(R.id.female);

        addressTv = (EditText) findViewById(R.id.address);

        postalCodeTv = (EditText) findViewById(R.id.postal_code);
        validatorFactory.pCodeValidator(postalCodeTv, true);

        parishTv = (EditText) findViewById(R.id.parish);

        requestCard = (Button) findViewById(R.id.pedir_cartao_button);

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(emailTv, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RequestCard.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailTv.setAdapter(adapter);
    }

    private void updateLabel() {

        String myFormat = "yyyy-mm-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        birthDateTv.setText(sdf.format(myCalendar.getTime()));
    }

    private void serverSuccess(JsonObject jsonResponse)
    {
        // Save user session
        Session session = new Session(RequestCard.this);
        if (jsonResponse.has("token"))
            session.setToken(jsonResponse.get("token").getAsString());
        session.setFaceLogin(true);
        // Persist user
        My_Realm my_realm = new My_Realm(RequestCard.this);
        UserEntity newUser = new UserEntity(jsonResponse);
        my_realm.setUser(newUser);
        // Login is finished
        progress.dismiss();
        startMainActivity(newUser);
    }

    private void startMainActivity(UserEntity newUser)
    {
        My_Answers my_answers = new My_Answers(newUser.getEmail());
        my_answers.signUp("Email", newUser.getMobile());
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(main);
    }

    private int[] decodePostalCode(String postalCode)
    {
        if(!postalCode.equals("")) {
            String[] parts = postalCode.split("-");
            return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        }
        else
            return new int[]{0, 0};
    }

}

