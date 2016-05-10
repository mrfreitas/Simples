package pt.admedia.simples.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.admedia.simples.MainActivity;
import pt.admedia.simples.R;
import pt.admedia.simples.api.BaseURL;
import pt.admedia.simples.lib.IsOnline;
import pt.admedia.simples.lib.My_Answers;
import pt.admedia.simples.lib.SimplesPrefs;


public class CardFragment extends Fragment {

    private ProgressBar cardProgress;
    private String firstName, lastName, cardValDate;
    long cardNumber;

    public CardFragment() {
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Answers initialization
        setAnswers();
        ImageView cardImage = (ImageView) getActivity().findViewById(R.id.card_image);
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
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cardProgress.setVisibility(View.VISIBLE);

        //Card image
        File f = new File(getActivity().getBaseContext().getCacheDir(), SimplesPrefs.CARD_NAME.toString());
        if (f.exists()) {
            cardImage.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
            cardProgress.setVisibility(View.GONE);
        }
        else {
            if(IsOnline.isOnline(getActivity().getBaseContext()))
                Glide.with(getActivity().getBaseContext())
                        .load(BaseURL.CARD_IMG + ((MainActivity) getActivity()).session.getToken())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(cardImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                saveCardImage(resource);
                            }
                        });
        }
    }

    private void saveCardImage(Bitmap resource)
    {
        try
        {
            File f = new File(getActivity().getBaseContext().getCacheDir(), SimplesPrefs.CARD_NAME.toString());
            if(!f.exists())
            {
                //noinspection ResultOfMethodCallIgnored
                f.createNewFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                resource.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }
            cardProgress.setVisibility(View.GONE);
        }
        catch (IOException ignored) {}

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setAnswers()
    {
        My_Answers my_answers = new My_Answers(((MainActivity) getActivity()).userEntity.getEmail());
        my_answers.cardDisplay();
    }
}
