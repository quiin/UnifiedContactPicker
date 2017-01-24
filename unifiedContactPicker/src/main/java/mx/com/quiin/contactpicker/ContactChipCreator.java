package mx.com.quiin.contactpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;

/**
 * Created by Carlos Reyna on 21/01/17.
 */

public class ContactChipCreator extends ChipSpanChipCreator{


    @Override
    public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
        Drawable chipIcon;
        if(PickerUtils.isEmail(text.toString()))
            chipIcon = ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_email_white, null);
        else
            chipIcon = ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_message_white, null);
        return new ChipSpan(context,text,chipIcon,data);
    }
}
