package pt.admedia.simples.validator.validators;

import android.content.Context;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.admedia.simples.validator.AbstractValidator;


/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 18:25
 */
public class MobileValidator extends AbstractValidator
{
    final static String PATTERN = "[0-9]{9}";
    public MobileValidator(Context c, int messageRes)
    {
        super(c, messageRes);
    }

    public MobileValidator(String message)
    {
        super(message);
    }


    @Override
    public boolean validate(TextView textView)
    {
        Pattern pattern= Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(textView.getText().toString());
        boolean asError = !matcher.matches();
        textView.setError(asError ? errorMessage : null);
        return asError;
    }
}
