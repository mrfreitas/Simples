package pt.admedia.simples.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.RealmList;
import pt.admedia.simples.R;
import pt.admedia.simples.SimplesApplication;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.model.DiscountEntity;
import pt.admedia.simples.model.PartnersEntity;

/**
 * Created by mrfreitas on 06/09/2015.
 */
public class MyRecyclerListAdapter extends RecyclerView.Adapter<MyRecyclerListAdapter.ViewHolder> {

    private ArrayList elements;
    private Context mContext;
    private int resLayout;
    private String partnerImgBaseUrl, logoUrl;
    private LinearLayout.LayoutParams discountTvParams;
    private CardActions cardActions;
    float dpHeight, dpWidth;
    double ratio = 1.50;
    int customHeight;
//    View view;



    public MyRecyclerListAdapter(Context mContext, ArrayList items, int textViewResourceId, CardActions cardActions) {

        this.mContext = mContext;
        this.elements = items;
        this.resLayout = textViewResourceId;
        partnerImgBaseUrl = BaseURL.PARTNERS_IMG.toString();
        logoUrl = BaseURL.LOGO_IMG.toString();
        // Layout params initialization
        discountTvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        discountTvParams.setMargins(2,2,2,2);
        this.cardActions = cardActions;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        this.dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        this.dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        this.customHeight = (int) Math.round((dpWidth/ratio) * displayMetrics.density);


        // Layout inflater initialization
//        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public MyRecyclerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyRecyclerListAdapter.ViewHolder holder, final int position) {

        final PartnersEntity partner = (PartnersEntity)elements.get(position);
        if(partner != null)
        {
            holder.partnerName.setText(partner.getTitle());
            holder.locationTitle.setText(partner.getAddress().trim());
//            holder.contact.setText(Integer.toString(partner.getPhone()));
//
//            // Font awesome
//            holder.locationIcon.setTypeface(SimplesApplication.fontAwesome);
//            holder.contactIcon.setTypeface(SimplesApplication.fontAwesome);
//            holder.descriptionTxt.setText(partner.);
            //  holder.presentation_image.requestLayout().getLayoutParams().height = Math.round(customHeight);
            holder.presentation_image.getLayoutParams().height = customHeight;
            holder.presentation_image.requestLayout();


            Picasso.with(mContext)
                    .load(partnerImgBaseUrl+partner.getImg())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.presentation_image);
            Picasso.with(mContext)
                    .load(logoUrl + partner.getLogo())
                    .into(holder.logo);

            RealmList<DiscountEntity> discounts = partner.getDiscounts();
         //   holder.discounts.removeAllViews();
            boolean isFirst = true;
            for (DiscountEntity discountObj : discounts) {
                if(isFirst)
                {
                    if(discountObj.getType().equals("perc")) {
                        holder.discount_round.setTypeface(null);
                        if (Build.VERSION.SDK_INT < 23)
                            holder.discount_round.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
                        else
                            holder.discount_round.setTextAppearance(android.R.style.TextAppearance_Medium);
                        holder.discount_round.setText(Integer.toString(discountObj.getValue()) + "%");
                    }
                    else {
                        if (Build.VERSION.SDK_INT < 23)
                            holder.discount_round.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
                        else
                            holder.discount_round.setTextAppearance(android.R.style.TextAppearance_Large);
                        holder.discount_round.setTypeface(SimplesApplication.fontAwesome);
                        holder.discount_round.setText(mContext.getString(R.string.icon_gift));
                    }
                    isFirst = false;
                }
//                TextView discountTV = new TextView(mContext);
//                discountTV.setText("\u2022" +" "+ discountObj.getDescription());
//                discountTV.setLayoutParams(discountTvParams);
//                discountTV.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Small);
//                discountTV.setTextColor(ContextCompat.getColor(mContext, R.color.text_tittle));
//                holder.discounts.addView(discountTV);
////                view = inflater.inflate(R.layout.t_divider_s, null);
////                holder.discounts.addView(view);
//
//                // Listeners
//                holder.contact.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        cardActions.onPhoneClicked(position);
//                    }
//                });
//
//                // Text clickable effect
//                holder.contact.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN)
//                            ((TextView)v).setTextColor(ContextCompat.getColor(mContext, R.color.colorHighlight));
//                        else if (event.getAction() == MotionEvent.ACTION_UP ||
//                                event.getAction() == MotionEvent.ACTION_CANCEL)
//                            ((TextView)v).setTextColor(ContextCompat.getColor(mContext, R.color.text_tittle));
//                        return false;
//                    }
//                });
            }
            // Listeners
            holder.partnerListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardActions.onItemClicked(position);
                }
            });
        }

    }

    public void clearData() {
        if(elements.size() > 0)
            elements.clear();
        this.notifyDataSetChanged();
    }

    public void addToTop(PartnersEntity partner){
        elements.add(0, partner);
        this.notifyItemInserted(0);
    }

    public void filteredLisItems(ArrayList partners){
        clearData();
        elements = partners;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView presentation_image, logo;
        LinearLayout toDetail, discounts;
        RelativeLayout partnerListItem;
        TextView partnerName, locationTitle, descriptionTitle, locationIcon, contactIcon, contact;
        TextView discount_round;
        public ViewHolder(View v) {
            super(v);
            partnerListItem = (RelativeLayout)v.findViewById(R.id.partner_list_item);
            presentation_image = (ImageView)v.findViewById(R.id.presentation_image);
            partnerName = (TextView)v.findViewById(R.id.partnerName);
            locationTitle = (TextView)v.findViewById(R.id.locationTitle);
//            descriptionTitle = (TextView)v.findViewById(R.id.descriptionTitle);
//            locationIcon = (TextView) v.findViewById(R.id.locationIcon);
//            contactIcon = (TextView) v.findViewById(R.id.contactIcon);
//            contact = (TextView) v.findViewById(R.id.contactTitle);
            discount_round = (TextView) v.findViewById(R.id.discount_round);
//            discounts = (LinearLayout) v.findViewById(R.id.discounts);
            logo = (ImageView) v.findViewById(R.id.logo);
        }
    }

    /**
     * Interface to respond to a click events on the partners cards
     * @onAddressClicked Shows the partner location on a map
     * @onPhoneClicked Launch phone activity with the partner phone number
     */
    public interface CardActions{
        void onItemClicked(int position);
    }

}
