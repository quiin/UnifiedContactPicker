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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.adapters.ContactAdapter;
import mx.com.quiin.contactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;

public class MainActivity extends AppCompatActivity implements ContactSelectionListener{

    private static final int READ_CONTACT_REQUEST = 1;
    private static final int CONTACT_PICKER_REQUEST = 2;

    private List<Contact> mContacts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTextView = (TextView) findViewById(R.id.textView);
    }

    public void launchContactPicker(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Intent contactPicker = new Intent(this, ContactPickerActivity.class);
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
                        if(selectedContacts != null) {
                            for (SimpleContact selectedContact : selectedContacts) {
                                List<String> list = new ArrayList<>();
                                list.add(selectedContact.getCommunication());
                                mContacts.add(new Contact(selectedContact.getDisplayName(), list));
                            }
                        }
                    }
                    setRecyclerView();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void setRecyclerView() {
        ContactAdapter adapter = new ContactAdapter(this,mContacts, this, null, null);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toggleViews();
    }

    private void toggleViews() {
        mTextView.setText("Contacts selected: ");
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

    @Override
    public void onContactSelected(Contact contact, String communication) {

    }

    @Override
    public void onContactDeselected(Contact contact, String communication) {

    }
}
