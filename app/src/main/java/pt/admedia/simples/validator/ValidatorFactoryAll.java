package pt.admedia.simples.validator;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * mrfreitas
 * Date: 19/07/2015
 * Time: 22:19
 */
public class ValidatorFactoryAll<T extends Activity>
{
    private ViewGroup viewGroup;
    private View view;
    private List<View> textViewList = new ArrayList<View>();

    public void setValidators(T t)
    {
        viewGroup = ((ViewGroup) t.findViewById(android.R.id.content));
        view = viewGroup.getChildAt(0);
        textViewList = view.getFocusables(View.FOCUS_FORWARD);

        for (View textView : textViewList)
        {
            if(textView instanceof EditText)
            {
                ((EditText) textView).setOnFocusChangeListener(new Validator((EditText) textView));
            }
        }
    }
}
