package pt.admedia.simples.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.RealmList;
import pt.admedia.simples.R;
import pt.admedia.simples.SimplesApplication;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.model.Discount;
import pt.admedia.simples.model.PartnersEntity;

/**
 * Created by mrfreitas on 06/09/2015.
 */
public class MyRecyclerListAdapter extends RecyclerView.Adapter<MyRecyclerListAdapter.ViewHolder> {

    private ArrayList elements;
    private Context mContext;
    private int resLayout;
    private LayoutInflater inflater;
    private String partnerImgBaseUrl;
    private float textSize;
    private LinearLayout.LayoutParams discountTvParams;
//    View view;



    public MyRecyclerListAdapter(Context mContext, ArrayList items, int textViewResourceId) {

        this.mContext = mContext;
        this.elements = items;
        this.resLayout = textViewResourceId;
        inflater = LayoutInflater.from(mContext);
        partnerImgBaseUrl = BaseURL.PARTNERS_IMG.toString();
        // Discounts text initialization
        textSize = mContext.getResources().getDimension(R.dimen.discounts_text);
        // Layout params initialization
        discountTvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        discountTvParams.setMargins(2,2,2,2);

        // Layout inflater initialization
//        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    private void loadPartners(int position, View convertView, ViewHolder holder)
    {
        final PartnersEntity partner = (PartnersEntity)elements.get(position);
        if(partner != null)
        {
            holder.partnerName.setText(partner.getTitle());
            holder.locationTitle.setText(partner.getAddress());
            holder.contact.setText(Integer.toString(partner.getPhone()));

            // Font awesome
            holder.locationIcon.setTypeface(SimplesApplication.fontAwesome);
            holder.contactIcon.setTypeface(SimplesApplication.fontAwesome);
//            holder.descriptionTxt.setText(partner.);

            Picasso.with(mContext)
                    .load(partnerImgBaseUrl+partner.getImg())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.presentation_image);

            RealmList<Discount> discounts = partner.getDiscounts();
            holder.discounts.removeAllViews();
            for (Discount discountObj : discounts) {
                TextView discountTV = new TextView(mContext);
                discountTV.setText("\u2022" +" "+ discountObj.getDescription());
                discountTV.setLayoutParams(discountTvParams);
                discountTV.setTextSize(textSize);
                discountTV.setTextColor(ContextCompat.getColor(mContext, R.color.text_color));
                holder.discounts.addView(discountTV);

//                view = inflater.inflate(R.layout.t_divider_s, null);
//                holder.discounts.addView(view);
            }
        }
    }

    @Override
    public MyRecyclerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(resLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyRecyclerListAdapter.ViewHolder holder, final int position) {

        loadPartners(position, holder.itemView, holder);

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
        this.notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView presentation_image;
        LinearLayout toDetail, discounts;
        CardView card_view;
        TextView partnerName, locationTitle, descriptionTitle, locationIcon, contactIcon, contact;
        public ViewHolder(View v) {
            super(v);
            card_view = (CardView)v.findViewById(R.id.card_view);
            presentation_image = (ImageView)v.findViewById(R.id.presentation_image);
            partnerName = (TextView)v.findViewById(R.id.partnerName);
            locationTitle = (TextView)v.findViewById(R.id.locationTitle);
            descriptionTitle = (TextView)v.findViewById(R.id.descriptionTitle);
            toDetail = (LinearLayout)v.findViewById(R.id.toDetail);
            locationIcon = (TextView) v.findViewById(R.id.locationIcon);
            contactIcon = (TextView) v.findViewById(R.id.contactIcon);
            contact = (TextView) v.findViewById(R.id.contactTitle);
            discounts = (LinearLayout) v.findViewById(R.id.discounts);
        }
    }

}
