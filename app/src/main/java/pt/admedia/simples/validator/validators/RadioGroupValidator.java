package pt.admedia.simples.validator.validators;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import pt.admedia.simples.validator.AbstractValidator;


/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 18:25
 */
public class RadioGroupValidator extends AbstractValidator
{

    public RadioGroupValidator(Context c, int messageRes)
    {
        super(c, messageRes);
    }

    public RadioGroupValidator(String message)
    {
        super(message);
    }

    @Override
    public boolean validate(TextView textView) {
        return false;
    }

    public boolean validateRGroup(RadioGroup group, RadioButton lastRadioButton)
    {

            boolean asError = group.getCheckedRadioButtonId() < 0;
            lastRadioButton.setError(asError ? errorMessage : null);
            return asError;
    }
}
