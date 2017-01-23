package mx.com.quiin.contactpicker.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.SimpleContact;

public class ContactPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactPickerFragment fragment = (ContactPickerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                for (SimpleContact simpleContact : fragment.getSelected()) {
                    Log.e("FAB", simpleContact.toString());
                }
            }
        });
    }

}
