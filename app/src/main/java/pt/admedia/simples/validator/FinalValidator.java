package pt.admedia.simples.validator;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 23:49
 */
public class FinalValidator<T extends Activity>
{
    private ViewGroup viewGroup;
    private View view;
    private List<View> textViewList = new ArrayList<View>();

    public boolean validate(T t)
    {
        viewGroup = ((ViewGroup) t.findViewById(android.R.id.content));
        view = viewGroup.getChildAt(0);
        textViewList = view.getFocusables(View.FOCUS_FORWARD);

        for (View textView : textViewList)
        {
            if(textView instanceof EditText)
            {
                if(((EditText) textView).getError() == null || ((EditText) textView).getText().toString().equals(""))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
