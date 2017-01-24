package mx.com.quiin.contactpicker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.R;

/**
 * Created by Carlos Reyna on 21/01/17.
 */

public class AutoCompleteAdapter extends ArrayAdapter<Contact> implements Filterable{

    private final List<Contact> mContacts;
    private List<Contact> tempContacts, suggestions;
    private final int mResourceId;
    private final int[] mMaterialColors;
    private Filter mFilter = new ContactFilter();

    public AutoCompleteAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.mContacts = objects;
        this.mResourceId = resource;
        this.mMaterialColors = context.getResources().getIntArray(R.array.colors);
        this.tempContacts = new ArrayList<>(objects);
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
        }

        Contact contact = getItem(position);
        if(contact != null){
            TextView tvDisplayName = (TextView) view.findViewById(R.id.tvDisplayName);
            TextView tvCommunication = (TextView) view.findViewById(R.id.tvCommunication);
            MaterialLetterIcon letterIcon = (MaterialLetterIcon) view.findViewById(R.id.letterIcon);

            if(tvDisplayName != null)
                tvDisplayName.setText(contact.getDisplayName());
            if(tvCommunication != null)
                tvCommunication.setText(contact.getDefaultCommunication());
            if(letterIcon != null){
                letterIcon.setLetter(contact.getInitial());
                letterIcon.setShapeColor(mMaterialColors[position % mMaterialColors.length]);

            }
        }

        return  view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ContactFilter extends Filter{

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Contact) resultValue).getDefaultCommunication();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if(constraint != null){
                suggestions.clear();
                String query = constraint.toString().toLowerCase();
                for (Contact contact : tempContacts) {
                    if(contact.getDisplayName().toLowerCase().contains(query) ||
                            contact.getSelectedCommunication().contains(query))
                        suggestions.add(contact);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }else{
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            List<Contact> filterList = (ArrayList<Contact>) results.values;
            if (results.count > 0) {
                clear();
                for (Contact contact : filterList) {
                    add(contact);
                    notifyDataSetChanged();
                }
            }

        }
    }

}
