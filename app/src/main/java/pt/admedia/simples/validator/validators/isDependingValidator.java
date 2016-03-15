package pt.admedia.simples.validator.validators;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import pt.admedia.simples.validator.AbstractValidator;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 20:58
 */
public class isDependingValidator extends AbstractValidator
{
    final TextView TO_COMPARE;
    public isDependingValidator(Context c, int messageRes, TextView toCompare)
    {
        super(c, messageRes);
        this.TO_COMPARE = toCompare;
    }

    public isDependingValidator(String message, TextView toCompare)
    {
        super(message);
        this.TO_COMPARE = toCompare;
    }


    @Override
    public boolean validate(TextView textView)
    {
        String toCompareStr = TO_COMPARE.getText().toString();
        String sourceStr = textView.getText().toString();
        boolean asError = (TextUtils.isEmpty(toCompareStr) && !TextUtils.isEmpty(sourceStr));
        textView.setError(asError ? errorMessage : null);
        return asError;
    }
}
