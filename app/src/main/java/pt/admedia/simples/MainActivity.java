package pt.admedia.simples;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;

import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.api.UserAPI;
import pt.admedia.simples.fragments.CardFragment;
import pt.admedia.simples.fragments.PartnersFragment;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.lib.SimplesPrefs;
import pt.admedia.simples.model.My_Realm;
import pt.admedia.simples.model.UserEntity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Session session;
    public UserEntity userEntity;
    private TextView userName, userEmail;
    private ImageView userImage;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // Check if the user have a session
        session = new Session(this);
        firstTime();

        My_Realm my_realm = new My_Realm(this);
        userEntity = my_realm.getUser();
        if (userEntity != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
            setSupportActionBar(toolbar);

            // Spinner
            Spinner spinner = (Spinner)findViewById(R.id.categories);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.categories,
                    getResources().getStringArray(R.array.categories));
            dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            // Navigation drawer
            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            assignView(navigationView);

            // Assign user data to it's views
            assignUserData();

            getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                    }
                }
            });

            if(toolbar != null)
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        else
                            drawer.openDrawer(GravityCompat.START);
                    }
                }
            });

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, cardFragment()).commit();
        }
        else
            logOut();
    }


    @Override
    public void onBackPressed() {
        final int count = getSupportFragmentManager().getBackStackEntryCount();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(count == 0){
            super.onBackPressed();
        }
        else
            getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        session.setFirstLoad(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         Fragment fragment = null;
        switch (id) {
            case R.id.partners:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFirstLoad", session.getFirstLoad());
                fragment = new PartnersFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.card:
                fragment = cardFragment();
                break;
/*            case R.id.preferences:
                fragment = new PreferencesFragment();
                break;
            case R.id.favorites:
                fragment = new FavoritesFragment();
                break;*/
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        return true;
    }

    private void firstTime() {
        Log.i("TOKEN", session.getToken());
        if (session.getToken().equals("")) {
            Intent firstTime = new Intent(this, FirstTime.class);
            firstTime.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            firstTime.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            firstTime.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(firstTime);
            this.overridePendingTransition(0, 0);
        }
    }

    private void logOut() {
        String token = session.getToken();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BaseURL.BASE_URL.toString())
                .build();
        UserAPI api = adapter.create(UserAPI.class);
        api.logOut(token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, Response response) {
                // TODO move card deletion, clear data, clear data base, to here
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
        // Card deletion
        File f = new File(getCacheDir(), SimplesPrefs.CARD_NAME.toString());
        if (f.exists())
            f.delete();
        // Clear session
        session.setToken("");
        if (session.getFaceLogin()) {
            LoginManager.getInstance().logOut();
            session.setFaceLogin(false);
        }
        // Clear data base
        My_Realm my_realm = new My_Realm(MainActivity.this);
        my_realm.removeUser();
        // User is redirected for the first time screen
        firstTime();
    }

    private void assignView(NavigationView navigationView) {
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);
        userImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_image);
    }

    private void assignUserData() {
        // Query user data
        Resources res = getResources();
        if (userEntity != null) {
            String text = String.format(res.getString(R.string.user_name), userEntity.getFirstName(), userEntity.getLastName());
            userName.setText(text);
            userEmail.setText(userEntity.getEmail());
            if (session.getFaceLogin()) {
                String imgLink = BaseURL.FACE_1.toString() + userEntity.getFaceUserId() + BaseURL.FACE_2.toString();
                Picasso.with(this).load(imgLink).into(userImage);
            }
        }
    }

    private Fragment cardFragment()
    {
        Bundle bundle = new Bundle();
        bundle.putString("fistName", userEntity.getFirstName());
        bundle.putString("lastName", userEntity.getLastName());
        bundle.putLong("cardNumber", userEntity.getCardNumber());
        bundle.putString("cardValDate", userEntity.getCardValDate());
        Fragment fragment = new CardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
