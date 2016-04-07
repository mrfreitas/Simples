package pt.admedia.simples.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
    private CardActions cardActions;
    int customHeight;
//    View view;



    public MyRecyclerListAdapter(Context mContext, ArrayList items, int textViewResourceId, CardActions cardActions) {

        this.mContext = mContext;
        this.elements = items;
        this.resLayout = textViewResourceId;
        //TODO verificar poque com as imagens redimencionadas está a estragar isto
       // partnerImgBaseUrl = BaseURL.PARTNERS_IMG.toString();
        partnerImgBaseUrl = BaseURL.IMAGE_URL.toString();
        logoUrl = BaseURL.LOGO_IMG.toString();
        this.cardActions = cardActions;
        this.customHeight = SimplesApplication.customHeight;


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

            holder.presentation_image.getLayoutParams().height = customHeight;

            Glide.with(mContext)
                    .load(partnerImgBaseUrl+partner.getImg())
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.placeholder)
                    .into(holder.presentation_image);
            Glide.with(mContext)
                    .load(logoUrl + partner.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
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
