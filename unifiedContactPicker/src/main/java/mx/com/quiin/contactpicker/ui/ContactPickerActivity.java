package mx.com.quiin.contactpicker.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.TreeSet;

import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.SimpleContact;

public class ContactPickerActivity extends AppCompatActivity {

    private static final String FRAGMENT_KEY = "CP_FRAG_KEY";
    public static final String CP_SELECTED_CONTACTS = "CP_SELECTED_CONTACTS";

    public static final String CP_EXTRA_SHOW_CHIPS = "CP_EXTRA_SHOW_CHIPS";
    public static final String CP_EXTRA_PROJECTION = "CP_EXTRA_PROJECTION";
    public static final String CP_EXTRA_SELECTION = "CP_EXTRA_SELECTION";
    public static final String CP_EXTRA_SELECTION_ARGS = "CP_EXTRA_SELECTION_ARGS";
    public static final String CP_EXTRA_SORT_BY= "CP_EXTRA_SORT_BY";
    public static final String CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS = "CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS";
    public static final String CP_EXTRA_SELECTION_COLOR = "CP_EXTRA_SELECTION_COLOR";
    public static final String CP_EXTRA_SELECTION_DRAWABLE = "CP_EXTRA_SELECTION_DRAWABLE";
    public static final String CP_EXTRA_FAB_DRAWABLE = "CP_EXTRA_DAB_DRAWABLE";
    public static final String CP_EXTRA_FAB_COLOR= "CP_EXTRA_FAB_COLOR";

    public static final String [] CP_DEFAULT_PROJECTION = new String[] {
            ContactsContract.Data._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Contacts.PHOTO_URI,
    };

    public static final String CP_DEFAULT_SELECTION =  "(" + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?)";
    public static final String CP_DEFAULT_SORT_BY = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
    public static final String [] CP_DEFAULT_SELECTION_ARGS = new String[] {
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
    };

    private ContactPickerFragment mFragment;

    private boolean showChips;
    private boolean hasCustomArgs;
    private String[] projection;
    private String[] selectArgs;
    private String select;
    private String sortBy;
    private String selectedColor;
    private byte [] selectedDrawable;
    private byte[] fabDrawable;
    private String fabColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null)
            mFragment = (ContactPickerFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        else
            mFragment = (ContactPickerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        readExtras();
        setFAB();
    }


    private void readExtras() {
        Intent intent = getIntent();
        if(intent != null){
            this.showChips = intent.getBooleanExtra(CP_EXTRA_SHOW_CHIPS, true);
            this.hasCustomArgs = intent.getBooleanExtra(CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS, false);
            this.projection = intent.getStringArrayExtra(CP_EXTRA_PROJECTION);
            this.select = intent.getStringExtra(CP_EXTRA_SELECTION);
            this.selectArgs = intent.getStringArrayExtra(CP_EXTRA_SELECTION_ARGS);
            this.sortBy = intent.getStringExtra(CP_EXTRA_SORT_BY);
            this.selectedColor = intent.getStringExtra(CP_EXTRA_SELECTION_COLOR);
            this.fabColor = intent.getStringExtra(CP_EXTRA_FAB_COLOR);
            this.fabDrawable = intent.getByteArrayExtra(CP_EXTRA_FAB_DRAWABLE);
            this.selectedDrawable = intent.getByteArrayExtra(CP_EXTRA_SELECTION_DRAWABLE);
            cleanIfNeeded();
        }
    }

    private void cleanIfNeeded() {
        if(this.projection == null)
            this.projection = CP_DEFAULT_PROJECTION;
        if(this.select == null)
            this.select = CP_DEFAULT_SELECTION;
        if(this.selectArgs == null && ! hasCustomArgs)
            this.selectArgs = CP_DEFAULT_SELECTION_ARGS;
        if(this.sortBy == null)
            this.sortBy = CP_DEFAULT_SORT_BY;
    }

    private void setFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fabColor != null)
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(fabColor)));
        if(fabDrawable != null){
            Bitmap customFabIcon = PickerUtils.extractDrawable(fabDrawable);
            if(customFabIcon != null)
                fab.setImageBitmap(customFabIcon);
        }

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

    public boolean isShowChips() {
        return showChips;
    }

    public String[] getProjection() {
        return projection;
    }

    public String getSelect() {
        return select;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String[] getSelectArgs() {
        return selectArgs;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public byte[] getSelectedDrawable() {
        return selectedDrawable;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager != null)
            fragmentManager.putFragment(outState, FRAGMENT_KEY , mFragment);
    }
}
