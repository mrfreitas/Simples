package pt.admedia.simples.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import pt.admedia.simples.R;

/**
 * Created by mrfreitas on 14/03/2016.
 */
public class IsOnline {

    public static boolean isOnline(Context c)
    {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(!(netInfo != null && netInfo.isConnectedOrConnecting())) {
            Toast.makeText(c, c.getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
}
