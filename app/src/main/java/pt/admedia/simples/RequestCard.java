package pt.admedia.simples;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


public class RequestCard extends AppCompatActivity
{
    private Button requestCard;
    private EditText firstNameTv, lastNameTv, mobileTv, birthDateTv, addressTv, postalCodeTv, townTv;
    private AutoCompleteTextView emailTv;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private ValidatorFactory validatorFactory;
    private RadioButton genderMale, genderFemale;
    private String faceId = "";

    //Id to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;
    public static final String ENDPOINT = BaseURL.BASE_URL.toString();
    private ProgressDialog progress;
    UserAPI api;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_request_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validatorFactory = new ValidatorFactory(getApplicationContext());
        assignViews();
        // Check data from facebook
        Intent intentExtras = getIntent();
        Bundle faceData = intentExtras.getExtras();
        if(faceData != null) {
            if (!faceData.isEmpty())
                setFaceData(faceData);
        }

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

        birthDateTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v == birthDateTv && event.getAction() == MotionEvent.ACTION_UP) {
                    new DatePickerDialog(RequestCard.this, date, 1985, myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
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
                    String town = townTv.getText().toString();


                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(BaseURL.BASE_URL.toString())
                            .build();
                    UserAPI api = adapter.create(UserAPI.class);
                    api.leadMobile(firstName, lastName, gender, faceId, birthDate, mobile, email, address, postalCode[0], postalCode[1], town, new Callback<JsonObject>() {
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
        switch (item.getItemId()) {
            case android.R.id.home:
                faceLogOut();
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                break;
        }
        return true;
    }

    private void assignViews() {
        firstNameTv = (EditText) findViewById(R.id.firstName);
        validatorFactory.nameValidate(firstNameTv, true);

        lastNameTv = (EditText) findViewById(R.id.lastName);
        validatorFactory.nameValidate(lastNameTv, true);

        mobileTv = (EditText) findViewById(R.id.mobile);
        validatorFactory.emptyValidate(mobileTv, true);
        validatorFactory.mobileValidate(mobileTv, true);

        // Autocomplete field
        emailTv = (AutoCompleteTextView) findViewById(R.id.regist_email);
        populateAutoComplete();
        validatorFactory.emailValidate(emailTv, true);

        birthDateTv = (EditText) findViewById(R.id.birthDate);
        validatorFactory.emptyValidate(birthDateTv, true);

        genderMale = (RadioButton)findViewById(R.id.male);
        if (genderMale != null)
            genderMale.setChecked(true);
        genderFemale = (RadioButton) findViewById(R.id.female);

        addressTv = (EditText) findViewById(R.id.address);
        validatorFactory.emptyValidate(addressTv, true);

        postalCodeTv = (EditText) findViewById(R.id.postal_code);
        validatorFactory.emptyValidate(postalCodeTv, true);
        validatorFactory.pCodeValidator(postalCodeTv, true);

        townTv = (EditText) findViewById(R.id.town);
        validatorFactory.emptyValidate(townTv, true);

        requestCard = (Button) findViewById(R.id.pedir_cartao_button);
    }

    @SuppressWarnings("ConstantConditions")
    private void setFaceData(Bundle faceData){
        if(faceData.containsKey("firstName"))
            firstNameTv.setText(faceData.getString("firstName"));
        if(faceData.containsKey("lastName"))
            lastNameTv.setText(faceData.getString("lastName"));
        if(faceData.containsKey("email"))
            emailTv.setText(faceData.getString("email"));
        if(faceData.containsKey("email"))
            emailTv.setText(faceData.getString("email"));
        if(faceData.containsKey("id"))
            faceId = faceData.getString("id");
        if(faceData.containsKey("birthDate"))
            birthDateTv.setText(convertDate(faceData.getString("birthDate")));
        if(faceData.containsKey("sex")) {
            if (faceData.getString("sex").equals("m"))
                genderMale.setChecked(true);
            else if (faceData.getString("sex").equals("m"))
                genderFemale.setChecked(true);
        }
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
        emailTv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, emails));
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
    public void onBackPressed() {
        super.onBackPressed();
        faceLogOut();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @SuppressWarnings("SimpleDateFormat")
    private void updateLabel() {
        String myFormat = "yyyy-mm-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        birthDateTv.setText(sdf.format(myCalendar.getTime()));
    }

    private void serverSuccess(JsonObject jsonResponse)
    {
        JsonObject status = jsonResponse.getAsJsonObject("status");
        int statusValue;
        statusValue = status.get("value").getAsInt();
        Session session = new Session(RequestCard.this);
        progress.dismiss();
        switch (statusValue) {
            case 0:
                if (!status.get("description").isJsonNull())
                    Toast.makeText(RequestCard.this, status.get("description").getAsString(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // Save session
                if (jsonResponse.has("token"))
                    session.setToken(jsonResponse.get("token").getAsString());
                session.setFaceLogin(true);
                // Persist user
                My_Realm my_realm = new My_Realm(RequestCard.this);
                my_realm.setUser(new UserEntity(jsonResponse));
                // Start main activity
                startMainActivity();
                break;
            case 2:
                // Save session
                session.setWaitingPayment(true);
                //Get payment data
                JsonObject order = jsonResponse.get("order").getAsJsonObject();
                JsonObject payment = order.get("payment").getAsJsonObject();
                session.setPaymentData(payment);
                // Display payment screen
                startPaymentDisplay();
                break;
        }
    }

    /**
     * Launch the screen to show payment information to the user
     */
    private void startPaymentDisplay()
    {
        My_Answers my_answers = new My_Answers(emailTv.getText().toString());
        my_answers.signUp("Email", Integer.parseInt(mobileTv.getText().toString()));

        Intent paymentData = new Intent(RequestCard.this, PaymentData.class);
        paymentData.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        paymentData.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(paymentData);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    /**
     * Start main activity when the user already have an account, and this request only
     * link the existing account with the new data. (Request statusValue = 1)
     */
    private void startMainActivity()
    {
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    /**
     *
     * @param postalCode
     * @return array - with the 2 parts of the postal code
     */
    private int[] decodePostalCode(String postalCode)
    {
        if(!postalCode.equals("")) {
            String[] parts = postalCode.split("-");
            return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        }
        else
            return new int[]{0, 0};
    }

    @SuppressWarnings("SimpleDateFormat")
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

    private void faceLogOut(){
        boolean isLoggedIn = AccessToken.getCurrentAccessToken() != null;
        if(isLoggedIn)
            LoginManager.getInstance().logOut();
    }

}

