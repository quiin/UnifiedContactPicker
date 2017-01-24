package mx.com.quiin.contactpicker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.TreeSet;

import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.SimpleContact;

public class ContactPickerActivity extends AppCompatActivity {

    public static final String CP_SELECTED_CONTACTS = "CP_SELECTED_CONTACTS";
    private static final String FRAGMENT_KEY = "CP_FRAG_KEY";
    private ContactPickerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null) {
            mFragment = (ContactPickerFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            Log.e("ContactPickerActivity", "restoring fragment");
        }else
            mFragment = (ContactPickerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        setFABClick();
    }

    private void setFABClick() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TreeSet<SimpleContact> selected = mFragment.getSelected();
                Intent result = new Intent();
                result.putExtra(CP_SELECTED_CONTACTS, selected);
                setResult(RESULT_OK,result);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager != null)
            fragmentManager.putFragment(outState, FRAGMENT_KEY , mFragment);
    }
}
