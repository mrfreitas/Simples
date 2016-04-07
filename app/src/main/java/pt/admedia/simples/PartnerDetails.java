package pt.admedia.simples;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import io.realm.RealmList;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.model.DiscountEntity;
import pt.admedia.simples.model.PartnersEntity;

public class PartnerDetails extends AppCompatActivity {

    private Session session;
    private String niu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.details_collapsingToolbar);

        session = new Session(this);
        Bundle data = getIntent().getExtras();
        niu = data.getString("partner");

        /*
         * Get partner data. If the activity was lunched by partner fragment, get current partner from
         * Simples application class.
         * If the activity was lunched by Android app back stack, makes a new query to gather the current
         * partner data. In this case was necessary to store niu id from the shared preferences.
         */
        PartnersEntity p;
        if(SimplesApplication.currentPartner != null) {
              p = SimplesApplication.currentPartner;
        }
        else{
            if(niu == null)
                niu = session.getCurrentPartner();
            Realm realm = Realm.getInstance(this);
            realm.beginTransaction();
              p = realm.where(PartnersEntity.class).equalTo("niu",niu).findFirst();
            realm.commitTransaction();
        }
        final PartnersEntity partner = p;

        collapsingToolbar.setTitle(partner.getTitle());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayoutExpandedTextStyle);

        TextView detailPartnerName = (TextView) findViewById(R.id.detail_partnerName);
        detailPartnerName.setText(partner.getTitle());

        ImageView logo = (ImageView) findViewById(R.id.detail_logo);
        Glide.with(this)
                .load(BaseURL.LOGO_IMG.toString() + partner.getLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);

        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.appbar);
        appBar.getLayoutParams().height = SimplesApplication.customHeightDetails;
        ImageView header = (ImageView) findViewById(R.id.image_header);
        Glide.with(this)
                .load(BaseURL.IMAGE_URL.toString() + partner.getImg())
                .into(header);

        // Discounts
        LinearLayout.LayoutParams discountTvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        discountTvParams.setMargins(30, 2, 30, 2);
        LinearLayout discountsLayout = (LinearLayout) findViewById(R.id.discounts);
        RealmList<DiscountEntity> discounts = partner.getDiscounts();
        for (DiscountEntity discountObj : discounts) {
            TextView discountTV = new TextView(this);
            discountTV.setText("\u2022" + " " + discountObj.getDescription());
            discountTV.setLayoutParams(discountTvParams);
            discountTV.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            discountTV.setTextColor(ContextCompat.getColor(this, R.color.text_color));
            discountsLayout.addView(discountTV);



         /*   // Text clickable effect
            holder.contact.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        ((TextView)v).setTextColor(ContextCompat.getColor(mContext, R.color.colorHighlight));
                    else if (event.getAction() == MotionEvent.ACTION_UP ||
                            event.getAction() == MotionEvent.ACTION_CANCEL)
                        ((TextView)v).setTextColor(ContextCompat.getColor(mContext, R.color.text_tittle));
                    return false;
                }
            });*/
        }

        // Description
        if (!partner.getDescription().equals("")) {
            TextView description = (TextView) findViewById(R.id.descriptionTxt);
            description.setText(partner.getDescription());
        } else {
            LinearLayout descriptionLayout = (LinearLayout) findViewById(R.id.descriptionLayout);
            descriptionLayout.setVisibility(View.GONE);
            View divider1 = (View) findViewById(R.id.divider1);
            divider1.setVisibility(View.GONE);
        }

        // Contacts
        //Address
        TextView locationIcon = (TextView) findViewById(R.id.locationIcon);
        locationIcon.setTypeface(SimplesApplication.fontAwesome);
        TextView locationTitle = (TextView) findViewById(R.id.locationTitle);
        locationTitle.setText(partner.getAddress());

        // Phone contact
        TextView contactIcon = (TextView) findViewById(R.id.contactIcon);
        contactIcon.setTypeface(SimplesApplication.fontAwesome);
        final TextView contactTitle = (TextView) findViewById(R.id.contactTitle);
        contactTitle.setText(Integer.toString(partner.getPhone()));
        contactTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Integer.toString(partner.getPhone())));
                startActivity(intent);
            }
        });

        // Email
        TextView emailIcon = (TextView) findViewById(R.id.emailIcon);
        emailIcon.setTypeface(SimplesApplication.fontAwesome);
        TextView emailTitle = (TextView) findViewById(R.id.emailTitle);
        emailTitle.setText(partner.getEmail());
        emailTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:" + partner.getEmail()));
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save current partner
        if(niu != null)
            session.setCurrentPartner(niu);

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
}
