package pt.admedia.simples.validator;

import android.content.Context;
import android.widget.TextView;

/**
 * mrfreitas
 * Date: 19/07/2015
 * Time: 21:35
 */
public abstract class AbstractValidator
{
    protected String errorMessage;
    private Context mContext;
    private int messageRes;

    public AbstractValidator(Context c, int messageRes) {
        mContext = c;
        errorMessage = c.getString(messageRes);
    }

    public AbstractValidator(String Pattern,Context c, int messageRes) {
        mContext = c;
        errorMessage = c.getString(messageRes);
    }

    public AbstractValidator(String message) {
        this.errorMessage = message;
    }

    public AbstractValidator(String Pattern, String message) {
        this.errorMessage = message;
    }


    public abstract boolean validate(TextView textView);


}
