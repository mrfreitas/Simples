package pt.admedia.simples.validator.validators;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.admedia.simples.validator.AbstractValidator;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 21:59
 */
public class PasswordValidator extends AbstractValidator
{
    /**
     * (			# Start of group
     * (?=.*\d)		# must contains one digit from 0-9
     * (?=.*[a-z])  # must contains one lowercase characters
     * (?=.*[A-Z])  # must contains one uppercase characters
     * (?=.*[@#$%])	# must contains one special symbols in the list "@#$%"
     *  .		    # match anything with previous condition checking
     * {6,20}	    # length at least 6 characters and maximum of 20
     * )			# End of group
     */
    final static String PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    final String MY_PATTERN;
    final boolean WITH_PATTERN;

    public PasswordValidator(Context c, int messageRes, String myPattern, boolean withPattern)
    {
        super(c, messageRes);
        this.MY_PATTERN = myPattern;
        this.WITH_PATTERN = withPattern;
    }

    public PasswordValidator(String message, String myPattern, boolean withPattern)
    {
        super(message);
        this.MY_PATTERN = myPattern;
        this.WITH_PATTERN = withPattern;
    }


    @Override
    public boolean validate(TextView textView)
    {
        boolean asError;
        String password = textView.getText().toString();
        if(WITH_PATTERN && !TextUtils.isEmpty(password))
        {
            Pattern pattern;
            if(MY_PATTERN.equals(""))
            {
                pattern = Pattern.compile(PATTERN);
            }
            else
            {
                pattern = Pattern.compile(MY_PATTERN);
            }
            Matcher matcher = pattern.matcher(password);
            asError = !matcher.matches();
            textView.setError(asError ? errorMessage : null);
            return asError;
        }
        else
        {
            int length = password.length();
            asError = (TextUtils.isEmpty(password) || (length < 6 || length > 24));
            textView.setError(asError ? errorMessage : null);
            return asError;
        }
    }
}
