package pt.admedia.simples.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * mrfreitas
 * Date: 21/07/2015
 * Time: 00:47
 */
public class FormValidator
{
    private List<Validator> mValidates = new ArrayList<Validator>();

    /**
     * Function adding Validates to our form
     *
     * @param validate
     * {@link Validator} add Validator
     */
    public void addValidates(Validator validate)
    {
        mValidates.add(validate);
    }

    /**
     * Function removing Validates from our form
     *
     * @param validate
     * {@link Validator} remove Validator
     * @return validate that was removed from the form
     */
    public boolean removeValidates(Validator validate)
    {
        if (mValidates != null && !mValidates.isEmpty())
        {
            return mValidates.remove(validate);
        }
        return false;
    }

    /**
     * Called to validate the form.
     * If an error is found, it will be displayed in the corresponding field.
     *
     * @return boolean true if the form is not valid, otherwise false
     */
    public boolean validate()
    {
        boolean formValid = true;
        boolean asError;
        for (Validator v : mValidates)
        {
            asError = v.validate();
            if (asError && formValid){formValid = false;}
        }
        return formValid;
    }

}
