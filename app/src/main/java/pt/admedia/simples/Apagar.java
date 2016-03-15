package pt.admedia.simples;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.lib.IsOnline;
import pt.admedia.simples.lib.Session;
import pt.admedia.simples.lib.SimplesPrefs;


public class Apagar extends Fragment {

    private ImageView cardImage;
    private ProgressBar cardProgress;
    private String firstName, lastName, cardValDate;
    long cardNumber;
    // Custom target for the picasso image loader
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
        {
            try
            {
                File f = new File(getContext().getCacheDir(), SimplesPrefs.CARD_NAME.toString());
                if(!f.exists())
                {
                    f.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }
                if(f.exists()) {
                    cardImage.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
                    cardProgress.setVisibility(View.GONE);
                }
            }
            catch (IOException e) {}
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}

    };
    public Apagar() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstName = getArguments().getString("fistName");
        lastName = getArguments().getString("lastName");
        cardNumber = getArguments().getLong("cardNumber");
        cardValDate = getArguments().getString("cardValDate");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Session session = new Session(getContext());
        String token = session.getToken();
        cardImage = (ImageView) getActivity().findViewById(R.id.card_image);
        cardProgress = (ProgressBar) getActivity().findViewById(R.id.card_progress);
        TextView name_tv = (TextView) getActivity().findViewById(R.id.cardName);
        TextView cNumber_tv = (TextView) getActivity().findViewById(R.id.cardNumber);
        TextView valDate_tv = (TextView) getActivity().findViewById(R.id.cardValDate);
        String nameStr = String.format(getActivity().getString(R.string.p_name), firstName, lastName);
        String cNumber = String.format(getActivity().getString(R.string.p_cardNumber), cardNumber);
        String valDate = String.format(getActivity().getString(R.string.p_valDate), cardValDate);
        name_tv.setText(nameStr);
        cNumber_tv.setText(cNumber);
        valDate_tv.setText(valDate);

        Spinner categories = (Spinner) getActivity().findViewById(R.id.categories);
        categories.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.card);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cardProgress.setVisibility(View.VISIBLE);

        //Card image
        File f = new File(getContext().getCacheDir(), SimplesPrefs.CARD_NAME.toString());
        if (f.exists()) {
            cardImage.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
            cardProgress.setVisibility(View.GONE);
        }
        else {
            if(IsOnline.isOnline(getContext()))
                Picasso.with(getContext()).load(BaseURL.CARD_IMG + token).into(target);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
