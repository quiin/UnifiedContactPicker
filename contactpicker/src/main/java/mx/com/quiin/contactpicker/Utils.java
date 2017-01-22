package mx.com.quiin.contactpicker;

/**
 * Created by Carlos Reyna on 21/01/17.
 */

public class Utils {

    public static boolean isEmail(String communication){
        if(communication == null)
            return false;
        return communication.contains("@");
    }
}
