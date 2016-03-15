package pt.admedia.simples.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 13:46
 */
public class Validator implements TextWatcher, View.OnFocusChangeListener
{

    private final TextView SOURCE_VIEW;
    private List<AbstractValidator> mValidators = new ArrayList<AbstractValidator>();

    public Validator(TextView sourceView) {
        this.SOURCE_VIEW = sourceView;
    }

    public void addValidator(AbstractValidator validator) {
        mValidators.add(validator);
    }

    public boolean validate() {
        boolean asError;
        for (AbstractValidator validator : mValidators)
        {
            asError = validator.validate(SOURCE_VIEW);
            if(asError){return true;}

        }
        return false;
    }

    @Override
    final public void afterTextChanged(Editable s) {
        validate();
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(!hasFocus)
        {
            validate();
        }
    }
}
