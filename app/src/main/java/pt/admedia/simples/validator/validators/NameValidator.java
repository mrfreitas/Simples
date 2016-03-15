package pt.admedia.simples.validator.validators;

import android.content.Context;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.admedia.simples.validator.AbstractValidator;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 19:35
 */
public class NameValidator extends AbstractValidator
{
    final static String PATTERN = "[\\p{L}-\\s]+";

    public NameValidator(TextView textView, String message)
    {
        super(message);
    }

    public NameValidator(Context c, int messageRes)
    {
        super(c, messageRes);
    }

    public NameValidator(String message)
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
