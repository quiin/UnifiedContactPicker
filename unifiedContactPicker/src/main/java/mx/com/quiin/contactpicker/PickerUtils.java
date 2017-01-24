package mx.com.quiin.contactpicker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import java.io.ByteArrayOutputStream;

/**
 * Created by Carlos Reyna on 21/01/17.
 */

public class PickerUtils {

    public static boolean isEmail(String communication){
        if(communication == null)
            return false;
        return communication.contains("@");
    }

    public static byte [] sendDrawable(Resources resources, @DrawableRes int drawableRes) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources , drawableRes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap extractDrawable(byte[] drawable) {
        return BitmapFactory.decodeByteArray(drawable, 0, drawable.length);
    }
}
