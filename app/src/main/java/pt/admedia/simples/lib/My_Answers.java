package pt.admedia.simples.lib;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mrfreitas on 21/03/2016.
 */
public class My_Answers {

    private SimpleDateFormat sdf;
    private String currentDateTime;
    private String userEmail;

    public My_Answers(String userEmail) {
        this.sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        this.currentDateTime = sdf.format(new Date());
        this.userEmail = userEmail;
    }

    public void logIn(String method)
    {
        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(method)
                .putSuccess(true)
                .putCustomAttribute("User email", userEmail)
                .putCustomAttribute("DateTime", currentDateTime));
    }

    public void signUp(String method, int mobile)
    {
        Answers.getInstance().logSignUp(new SignUpEvent()
                .putMethod(method)
                .putSuccess(true)
                .putCustomAttribute("User email", userEmail)
                .putCustomAttribute("DateTime", currentDateTime)
                .putCustomAttribute("Phone", mobile));
    }

    public void cardDisplay()
    {
        Answers.getInstance().logCustom(new CustomEvent("Card display")
                .putCustomAttribute("User email", userEmail)
                .putCustomAttribute("DateTime", currentDateTime));
    }

    public void partnersList()
    {
        Answers.getInstance().logCustom(new CustomEvent("Partners List")
                .putCustomAttribute("User email", userEmail)
                .putCustomAttribute("DateTime", currentDateTime));
    }

    public void partnerCall()
    {
        Answers.getInstance().logCustom(new CustomEvent("Partner Call")
                .putCustomAttribute("User email", userEmail)
                .putCustomAttribute("DateTime", currentDateTime));
    }
}
