package mx.com.quiin.contactpicker.interfaces;

import mx.com.quiin.contactpicker.Contact;

/**
 * Created by Carlos Reyna on 21/01/17.
 */

public interface ContactSelectionListener {
    void onContactSelected(Contact contact, String communication);
    void onContactDeselected(Contact contact, String communication);
}
