package pt.admedia.simples;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

import pt.admedia.simples.lib.Session;

public class PaymentData extends AppCompatActivity {

    private Button paymentBt;
    private Session session;
    private Boolean exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_payment_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new Session(this);
        exit = false;

        assignViews();

        paymentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceLogOut();
                session.setWaitingPayment(false);
                Intent firstTime = new Intent(PaymentData.this, FirstTime.class);
                firstTime.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                firstTime.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(firstTime);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            finish(); // finish activity
        } else {
            Toast.makeText(this, this.getString(R.string.sair),
                    Toast.LENGTH_SHORT).show();
            exit = true;
            /*
             * The Handler here handles accidental back presses, it simply shows a Toast,
             * and if there is another back press within 3 seconds, it closes the application
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void assignViews()
    {
        TextView tvEntity = (TextView) findViewById(R.id.tv_entity);
        tvEntity.setText(session.getPaymentEntity());

        TextView tvReference = (TextView) findViewById(R.id.tv_reference);
        tvReference.setText(session.getPaymentReference());

        TextView tvValue = (TextView) findViewById(R.id.tv_value);
        String valueStr = String.format(this.getString(R.string.value), session.getPaymentValue());
        tvValue.setText(valueStr);

        TextView tvWarningPayment = (TextView)findViewById(R.id.tv_warning);
        tvWarningPayment.setText(Html.fromHtml(getString(R.string.warning_payment)));

        paymentBt = (Button) findViewById(R.id.payment_bt);
    }
    private void faceLogOut(){
        boolean isLoggedIn = AccessToken.getCurrentAccessToken() != null;
        if(isLoggedIn)
            LoginManager.getInstance().logOut();
    }
}
