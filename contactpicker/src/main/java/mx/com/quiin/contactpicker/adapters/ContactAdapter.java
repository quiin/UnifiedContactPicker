package mx.com.quiin.contactpicker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.Utils;
import mx.com.quiin.contactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.contactpicker.ui.ContactSpinner;

/**
 * Created by Carlos Reyna on 20/01/17.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    private static final String TAG = ContactAdapter.class.getSimpleName();
    private final int[] mMaterialColors;
    private final List<Contact> mContacts;
    private final LinkedHashMap<Contact, String> mSelectedItems;
    private final Context mContext;
    private final int selectedColor;
    private final int subtitleColor;
    private final ContactSelectionListener mListener;

    public ContactAdapter(Context context, List<Contact> contacts, ContactSelectionListener listener) {
        this.mMaterialColors = context.getResources().getIntArray(R.array.colors);
        this.mContacts = contacts;
        this.mSelectedItems = new LinkedHashMap<>();
        this.mContext = context;
        this.selectedColor = ResourcesCompat.getColor(context.getResources(), R.color.color_7,null);
        this.subtitleColor= ResourcesCompat.getColor(context.getResources(), R.color.subtitle,null);
        this.mListener = listener;
    }


    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_contact_row,parent,false);
        final ContactViewHolder viewHolder = new ContactViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                toggle(position, viewHolder);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        final int pos = position;
        final ContactViewHolder viewHolder = holder;

        if(contact != null){

            List<String> cellphones = contact.getCellphones();
            List<String> emails = contact.getEmails();

            holder.letterIcon.setLetter(contact.getInitial());
            holder.letterIcon.setShapeColor(mMaterialColors[position % mMaterialColors.length]);


            final List<String> communications = new ArrayList<>(cellphones);
            communications.addAll(emails);

            ContactSpinnerAdapter adapter = new ContactSpinnerAdapter(mContext, R.layout.cp_spinner_row, communications, contact.getDisplayName(), new ContactSpinnerAdapter.ClickListener() {
                @Override
                public void onSpinnerClick(String communication, String displayName) {
                    toggle(pos, viewHolder);
                }
            });

            holder.spinner.setAdapter(adapter);
            setSpinnerWithoutCallingListener(pos, communications, viewHolder);

            //Select item if previously selected
            selectView(mSelectedItems.containsKey(contact), holder);


        }else
            Log.e(TAG, "onBindViewHolder: contact null");
    }

    private void setSpinnerWithoutCallingListener(final int pos, final List<String> communications, final ContactViewHolder viewHolder) {
        final ContactSpinner spinner = viewHolder.spinner;
        final AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageView ivCommunicationIcon = (ImageView) view.findViewById(R.id.ivCommunicationIcon);
                ImageView ivSelectedCommunication = (ImageView) view.findViewById(R.id.cp_ivSelectedComm);
                String communication = communications.get(position);
                if(Utils.isEmail(communication))
                    ivSelectedCommunication.setImageResource(R.drawable.ic_email);
                else
                    ivSelectedCommunication.setImageResource(R.drawable.ic_message);
                ivCommunicationIcon.setVisibility(View.GONE);
                ivSelectedCommunication.setVisibility(View.VISIBLE);

                toggle(pos, viewHolder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(null);
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.post(new Runnable() {
                    @Override
                    public void run() {

                        spinner.setOnItemSelectedListener(listener);
                    }
                });
            }
        });

    }


    private void toggle(int position, ContactViewHolder viewHolder){
        Contact contact = mContacts.get(position);
        String selected = (String) viewHolder.spinner.getSelectedItem();
        String communication = contact.findCommunication(selected);
        if(mSelectedItems.containsKey(contact)) {
            String prevSelected = mSelectedItems.get(contact);
            if(prevSelected.equals(selected)){
                mSelectedItems.remove(contact);
                mListener.onContactDeselected(contact,communication);
                selectView(false, viewHolder);
            }else{
                mSelectedItems.remove(contact);
                mListener.onContactDeselected(contact, communication);

                mSelectedItems.put(contact, communication);
                mListener.onContactSelected(contact,communication);
            }

        }else {
            mSelectedItems.put(contact, communication);
            mListener.onContactSelected(contact,communication);
            selectView(true, viewHolder);
        }
    }


    private void selectView(boolean select, ContactViewHolder view) {
        ImageView ivSelected = view.ivSelected;
        MaterialLetterIcon letterIcon = view.letterIcon;

        if(select){
            letterIcon.setVisibility(View.GONE);
            ivSelected.setVisibility(View.VISIBLE);
        }else{
            letterIcon.setVisibility(View.VISIBLE);
            ivSelected.setVisibility(View.GONE);
        }

        View selectedView = view.spinner.getSelectedView();

        if(selectedView != null){
            TextView tvDisplayName = (TextView) selectedView.findViewById(R.id.tvDisplayName);
            TextView tvCommunication = (TextView) selectedView.findViewById(R.id.tvCommunication);
            if(tvDisplayName != null && tvCommunication != null){
                if(select){
                    tvDisplayName.setTextColor(selectedColor);
                    tvCommunication.setTextColor(selectedColor);
                }else{
                    tvDisplayName.setTextColor(Color.BLACK);
                    tvCommunication.setTextColor(subtitleColor);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }



    public class ContactViewHolder extends RecyclerView.ViewHolder{

        public MaterialLetterIcon letterIcon;
        public ContactSpinner spinner;
        public ImageView ivSelected;

        public ContactViewHolder(View view) {
            super(view);
            letterIcon = (MaterialLetterIcon) view.findViewById(R.id.letterIcon);
            spinner = (ContactSpinner) view.findViewById(R.id.cp_spinner);
            ivSelected = (ImageView) view.findViewById(R.id.cp_ivSelected);
        }


    }

}
