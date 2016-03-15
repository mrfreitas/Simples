package pt.admedia.simples.validator.validators;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.admedia.simples.validator.AbstractValidator;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 14:25
 */
public class EmailValidator extends AbstractValidator
{
    private String mDomainName = "";

    public EmailValidator(String message)
    {
        super(message);
    }

    /**
     * Lets say that the email address must be valid for such domain.
     * This function only accepts strings of Regexp
     *
     * @param domainName Regexp Domain Name
     *                   <p/>
     *                   example : gmail.com
     */
    public void setDomainName(String domainName) {
        mDomainName = domainName;
    }

    private boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

    @Override
    public boolean validate(TextView textView)
    {
        String email = textView.getText().toString();
        if (isNotEmpty(email))
        {
            Pattern pattern;
            if (isNotEmpty(mDomainName))
            {
                pattern = Pattern.compile(".+@" + mDomainName);
            }
            else
            {
                pattern = Pattern.compile(".+@.+\\.[a-z]+");
            }
            Matcher matcher = pattern.matcher(email);
            boolean asError = !matcher.matches();
            textView.setError(asError ? errorMessage : null);
            return !matcher.matches();
        }
        return false;
    }
}

