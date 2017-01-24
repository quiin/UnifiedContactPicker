package mx.com.quiin.contactpicker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.contactpicker.views.CommunicationViewHolder;
import mx.com.quiin.contactpicker.views.ContactViewHolder;

/**
 * Created by Carlos Reyna on 20/01/17.
 */

public class ContactAdapter extends ExpandableRecyclerAdapter<Contact, String, ContactViewHolder, CommunicationViewHolder>{

    private static final String TAG = ContactAdapter.class.getSimpleName();
    private int[] mMaterialColors;
    private List<Contact> mContacts;
    private List<Contact> mSelection;
    private final Bitmap selectedDrawable;
    private final int selectedColor;
    private final int subtitleColor;
    private final ContactSelectionListener mListener;
    private final int selectedIconColor;

    public ContactAdapter(Context context, List<Contact> contacts, ContactSelectionListener listener, String selectedIconHex, byte[] selectedDrawable) {
        super(contacts);
        this.mMaterialColors = context.getResources().getIntArray(R.array.colors);
        this.mContacts = contacts;
        this.mSelection = new ArrayList<>();
        this.selectedColor = ResourcesCompat.getColor(context.getResources(), R.color.color_7,null);
        this.subtitleColor= ResourcesCompat.getColor(context.getResources(), R.color.subtitle,null);
        this.mListener = listener;

        if(selectedIconHex == null)
            this.selectedIconColor = ResourcesCompat.getColor(context.getResources(),R.color.materialGreen, null);
        else
            this.selectedIconColor = Color.parseColor(selectedIconHex);

        if(selectedDrawable != null)
            this.selectedDrawable = PickerUtils.extractDrawable(selectedDrawable);
        else
            this.selectedDrawable = PickerUtils.extractDrawable(PickerUtils.sendDrawable(context.getResources(),R.drawable.ic_done));
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateParentViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_contact_row,parent,false);
        return new ContactViewHolder(view);
    }

    @NonNull
    @Override
    public CommunicationViewHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_communication_row,parent,false);
        final CommunicationViewHolder viewHolder = new CommunicationViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childPosition = viewHolder.getChildAdapterPosition();
                int parentPosition = viewHolder.getParentAdapterPosition();
                handleChildSelection(parentPosition,childPosition);
                collapseParent(parentPosition);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindParentViewHolder(@NonNull ContactViewHolder parentViewHolder, int parentPosition, @NonNull Contact parent) {
        bindParent(parentViewHolder, parentPosition);
    }

    @Override
    public void onBindChildViewHolder(@NonNull CommunicationViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull String child) {
        bindCommunicationToViewHolder(childViewHolder, child);
    }

    private void bindCommunicationToViewHolder(CommunicationViewHolder childViewHolder, String child) {
        childViewHolder.tvCommunication.setText(child);
        if(PickerUtils.isEmail(child))
            childViewHolder.ivCommunicationIcon.setImageResource(R.drawable.ic_email);
        else
            childViewHolder.ivCommunicationIcon.setImageResource(R.drawable.ic_message);
    }

    @SuppressLint("DefaultLocale")
    private void bindParent(final ContactViewHolder parentViewHolder, final int position) {

        final Contact contact = mContacts.get(position);
        if(contact != null){

            String communication = contact.getSelectedCommunication();

            parentViewHolder.ivSelected.setBackgroundColor(this.selectedIconColor);
            parentViewHolder.ivSelected.setImageBitmap(selectedDrawable);
            parentViewHolder.letterIcon.setLetter(contact.getInitial());
            parentViewHolder.letterIcon.setShapeColor(mMaterialColors[position % mMaterialColors.length]);
            parentViewHolder.tvCommunication.setText(communication);
            parentViewHolder.tvDisplayName.setText(contact.getDisplayName());

            if(PickerUtils.isEmail(communication))
                parentViewHolder.ivSelectedCommunication.setImageResource(R.drawable.ic_email);
            else
                parentViewHolder.ivSelectedCommunication.setImageResource(R.drawable.ic_message);

            if(contact.getTotalCommunications() > 1) {
                parentViewHolder.ivExpandArrow.setVisibility(View.VISIBLE);
                parentViewHolder.expandableArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expand(contact, parentViewHolder);
                    }
                });

                parentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(contact.isSelected())
                            unSelectContact(contact);
                        else
                            expand(contact, parentViewHolder);
                    }
                });
            }
            else {
                parentViewHolder.ivExpandArrow.setVisibility(View.INVISIBLE);
                parentViewHolder.expandableArea.setClickable(false);
                parentViewHolder.expandableArea.setFocusable(false);
                parentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleParentSelection(position);
                    }
                });
            }


            //Select item if previously selected
            selectView(contact, parentViewHolder);


        }else
            Log.e(TAG, "onBindViewHolder: contact null");
    }

    private void expand(Contact contact, ContactViewHolder parentViewHolder) {
        if(parentViewHolder.isExpanded())
            collapseParent(contact);
        else
            expandParent(contact);
    }


    private void handleParentSelection(int position) {
        Contact contact = mContacts.get(position);
        String communication = contact.getSelectedCommunication();
        selectCommunication(contact, position, communication);
    }

    private void handleChildSelection(int parentPosition, int childPosition) {
        Contact contact = mContacts.get(parentPosition);
        String communication = contact.getCommunications().get(childPosition);
        selectCommunication(contact,parentPosition, communication);
    }

    private void selectCommunication(Contact contact, int parentPosition, String communication){
        if(mSelection.contains(contact)){
            //existing contact
            if(contact.getSelectedCommunication().equals(communication)){
                contact.setSelected(false);
                mSelection.remove(contact);
                mListener.onContactDeselected(contact,communication);
            }else{
                contact.setSelected(true);
                String prevSelected = contact.getSelectedCommunication();
                mListener.onContactDeselected(contact,prevSelected);
                mListener.onContactSelected(contact,communication);
                contact.setSelectedCommunication(communication);
            }
        }else{
            contact.setSelected(true);
            contact.setSelectedCommunication(communication);
            mSelection.add(contact);
            mListener.onContactSelected(contact, communication);
        }
        notifyParentChanged(parentPosition);
    }

    private void unSelectContact(Contact contact){

        if(mSelection.contains(contact)){
            contact.setSelected(false);
            mSelection.remove(contact);
            mListener.onContactDeselected(contact,contact.getSelectedCommunication());
            notifyParentChanged(mContacts.indexOf(contact));
        }

    }

    private void selectView(Contact contact, ContactViewHolder view) {
        ImageView ivSelected = view.ivSelected;
        MaterialLetterIcon letterIcon = view.letterIcon;
        TextView tvDisplayName = view.tvDisplayName;
        TextView tvCommunication = view.tvCommunication;

        if(contact.isSelected()){
            letterIcon.setVisibility(View.GONE);
            ivSelected.setVisibility(View.VISIBLE);
            tvDisplayName.setTextColor(selectedColor);
            tvCommunication.setTextColor(selectedColor);
        }else{
            letterIcon.setVisibility(View.VISIBLE);
            ivSelected.setVisibility(View.GONE);
            tvDisplayName.setTextColor(Color.BLACK);
            tvCommunication.setTextColor(subtitleColor);
        }

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public ArrayList<SimpleContact> getSelection(){
        ArrayList<SimpleContact> selected = new ArrayList<>();
        for (Contact contact : mSelection) {
            selected.add(contact.simplify());
        }
        return selected;
    }

}
