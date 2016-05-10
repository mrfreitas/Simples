package pt.admedia.simples.validator;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import pt.admedia.simples.R;
import pt.admedia.simples.validator.validators.ConfirmValidator;
import pt.admedia.simples.validator.validators.EmailValidator;
import pt.admedia.simples.validator.validators.MobileValidator;
import pt.admedia.simples.validator.validators.NameValidator;
import pt.admedia.simples.validator.validators.PasswordValidator;
import pt.admedia.simples.validator.validators.PostalCodeValidator;
import pt.admedia.simples.validator.validators.RequiredValidator;

/**
 * mrfreitas
 * Date: 27/07/2015
 * Time: 13:54
 */
public class ValidatorFactory
{
    private FormValidator formValidator;
    private Context context;

    public ValidatorFactory(Context context)
    {
        formValidator = new FormValidator();
        this.context = context;
    }

    public void emptyValidate(TextView v, boolean addToForm)
    {
        // Name Validate
        Validator emptyValidator = new Validator(v);
        emptyValidator.addValidator(new RequiredValidator(this.context.getResources().getString(R.string.empty_validation)));
        v.setOnFocusChangeListener(emptyValidator);
        if(addToForm)
        formValidator.addValidates(emptyValidator);
    }

    public void nameValidate(TextView v, boolean addToForm)
    {
        // Name Validate
        Validator nameValidator = new Validator(v);
        nameValidator.addValidator(new RequiredValidator(this.context.getResources().getString(R.string.empty_validation)));
        nameValidator.addValidator(new NameValidator(this.context.getResources().getString(R.string.name_validation)));
        v.setOnFocusChangeListener(nameValidator);
        if(addToForm)
        formValidator.addValidates(nameValidator);
    }

    public void emailValidate(TextView v , boolean addToForm)
    {
        Validator emailValidator = new Validator(v);
        emailValidator.addValidator(new RequiredValidator(this.context.getResources().getString(R.string.empty_validation)));
        emailValidator.addValidator(new EmailValidator(this.context.getResources().getString(R.string.email_validation)));
        v.setOnFocusChangeListener(emailValidator);
        if(addToForm)
        formValidator.addValidates(emailValidator);
    }

    public void cEmailValidate(TextView v ,TextView v1, boolean addToForm)
    {
        Validator cEmailValidator = new Validator(v);
        cEmailValidator.addValidator(new ConfirmValidator(this.context.getResources().getString(R.string.conf_email_validation), v1));;
        v.setOnFocusChangeListener(cEmailValidator);
        if(addToForm)
            formValidator.addValidates(cEmailValidator);
    }

    public void passwordValidate(TextView v , boolean addToForm)
    {
        Validator passwordValidator = new Validator(v);
        passwordValidator.addValidator(new PasswordValidator(this.context.getResources().getString(R.string.password_validation), "", false));
        v.setOnFocusChangeListener(passwordValidator);
        if(addToForm)
            formValidator.addValidates(passwordValidator);
    }

    public void cPasswordValidate(TextView v ,TextView v1, boolean addToForm)
    {
        Validator cPasswordValidator = new Validator(v);
        cPasswordValidator.addValidator(new ConfirmValidator(this.context.getResources().getString(R.string.conf_password_validation), v1));
        v.setOnFocusChangeListener(cPasswordValidator);
        if(addToForm)
            formValidator.addValidates(cPasswordValidator);
    }

    public void mobileValidate(TextView v , boolean addToForm)
    {
        Validator mobileValidator = new Validator(v);
        mobileValidator.addValidator(new MobileValidator(this.context.getResources().getString(R.string.mobile_validation)));
        v.setOnFocusChangeListener(mobileValidator);
        if (addToForm)
            formValidator.addValidates(mobileValidator);
    }

    public void rGroupValidate(RadioGroup group, RadioButton lastRButton, boolean addToForm)
    {
/*        Validator rGroupValidate = new Validator(v);
        rGroupValidate.addValidator(new MobileValidator(this.context.getResources().getString(R.string.mobile_validation)));
        v.setOnFocusChangeListener(rGroupValidate);
        formValidator.addValidates(rGroupValidate);
        if(addToForm)
            formValidator.addValidates(rGroupValidate);*/
    }

    public void pCodeValidator(TextView v , boolean addToForm)
    {
        Validator pCValidator = new Validator(v);
        pCValidator.addValidator(new PostalCodeValidator(this.context.getResources().getString(R.string.pCode_validation)));
        v.setOnFocusChangeListener(pCValidator);
        if (addToForm)
            formValidator.addValidates(pCValidator);
    }

    public void isDepending(TextView v ,TextView v1, boolean addToForm)
    {
        Validator cPasswordValidator = new Validator(v);
        cPasswordValidator.addValidator(new ConfirmValidator(this.context.getResources().getString(R.string.conf_password_validation), v1));
        v.setOnFocusChangeListener(cPasswordValidator);
        if(addToForm)
            formValidator.addValidates(cPasswordValidator);
    }

    public boolean formValidate()
    {
        return formValidator.validate();
    }

}
