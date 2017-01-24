package mx.com.quiin.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.TreeSet;

import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;

public class MainActivity extends AppCompatActivity {

    private static final int READ_CONTACT_REQUEST = 1;
    private static final int CONTACT_PICKER_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchContactPicker(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Intent contactPicker = new Intent(this, ContactPickerActivity.class);
//            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SHOW_CHIPS, false);
//            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_FAB_COLOR, "#FFF722");
//            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_COLOR, "#FFF722");
//            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_FAB_DRAWABLE, PickerUtils.sendDrawable(getResources(),R.drawable.ic_person));
//            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_DRAWABLE, PickerUtils.sendDrawable(getResources(),R.drawable.ic_person));
            startActivityForResult(contactPicker, CONTACT_PICKER_REQUEST);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    READ_CONTACT_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CONTACT_PICKER_REQUEST:
                //contacts were selected
                if(resultCode == RESULT_OK){
                    if(data != null){
                        TreeSet<SimpleContact> selectedContacts = (TreeSet<SimpleContact>) data.getSerializableExtra(ContactPickerActivity.CP_SELECTED_CONTACTS);
                        if(selectedContacts != null)
                            for (SimpleContact selectedContact : selectedContacts) {
                                Log.e("Selected", selectedContact.toString());
                            }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case READ_CONTACT_REQUEST:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    launchContactPicker(null);
                }
        }
    }
}
