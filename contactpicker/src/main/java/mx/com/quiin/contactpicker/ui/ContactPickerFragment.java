package mx.com.quiin.contactpicker.ui;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import mx.com.quiin.contactpicker.ContactAdapter;
import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.models.Contact;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactPickerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int CONTACT_LOADER_ID = 666;
    private static final String TAG = ContactPickerFragment.class.getSimpleName();
    private ListView mListView;

    private ContactAdapter mContactAdapter;
    private final ArrayList<Contact> mContacts = new ArrayList<>();

    private final String[] PROJECTION = new String[] {
            ContactsContract.Data._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Contacts.PHOTO_URI,
    };

    public ContactPickerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mListView = (ListView) view.findViewById(R.id.cp_listView);
        mContactAdapter = new ContactAdapter(getContext(), R.layout.cp_contact_row, mContacts);
        mListView.setAdapter(mContactAdapter);
        mListView.setOnItemClickListener(this);

        getActivity().getSupportLoaderManager()
                .initLoader(CONTACT_LOADER_ID,
                        new Bundle(),
                        this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define the columns to retrieve

        String select =  "(" + ContactsContract.Data.MIMETYPE + "=? OR "
                + ContactsContract.Data.MIMETYPE + "=?)";

        String[] selectionArgs = new String[]{
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
        };

        String sort = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
        // Return the loader for use
        return new CursorLoader(getContext(),
                ContactsContract.Data.CONTENT_URI, // URI
                PROJECTION, // projection fields
                select, // the selection criteria
                selectionArgs, // the selection args
                sort // the sort order
        );
    }

    /**
     * When the system finishes retrieving the Cursor through the CursorLoader,
     * a call to the onLoadFinished() method takes place.
     **/
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()){
            do{
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String communication = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String communicationType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                Contact newContact = new Contact(displayName);

                if(!mContacts.contains(newContact)){
                    String uriString = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    if(uriString == null)
                        newContact.setPhotoUri(null);
                    else
                        newContact.setPhotoUri(Uri.parse(uriString));

                    newContact.addCommunication(communication,communicationType);
                    mContacts.add(newContact);
                }else{
                    Contact existingContact = mContacts.get(mContacts.indexOf(newContact));
                    existingContact.addCommunication(communication, communicationType);
                }

            }while(cursor.moveToNext());
        }
        if(mContactAdapter != null) {
            mContactAdapter.notifyDataSetChanged();
        }

    }

    /**
     * This method is triggered when the loader is being reset
     * and the loader data is no longer available. Called if the data
     * in the provider changes and the Cursor becomes stale.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Called whenever an item in {@param parent} is clicked
     * @param parent listView
     * @param view clicked view
     * @param position clicked position
     * @param id unique identifier
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemClick: position " + position);
        Contact contact = mContacts.get(position);
        if(contact != null)
            Log.e(TAG, contact.toString() );
        else
            Log.e(TAG, "onItemClick: contact is null");
    }
}
