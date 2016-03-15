package pt.admedia.simples.validator.validators;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import pt.admedia.simples.validator.AbstractValidator;


/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 18:25
 */
public class RequiredValidator extends AbstractValidator
{

    public RequiredValidator(Context c, int messageRes)
    {
        super(c, messageRes);
    }

    public RequiredValidator(String message)
    {
        super(message);
    }


    @Override
    public boolean validate(TextView textView)
    {
        String text = textView.getText().toString();
        boolean asError = TextUtils.isEmpty(text);
        textView.setError(asError ? errorMessage : null);
        return asError;
    }
}
