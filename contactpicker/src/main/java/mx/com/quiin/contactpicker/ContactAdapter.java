package mx.com.quiin.contactpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.List;
import java.util.Random;

import mx.com.quiin.contactpicker.models.Contact;

/**
 * Created by Carlos Reyna on 20/01/17.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {

    private final int[] mMaterialColors;
    private final Random random;

    public ContactAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.mMaterialColors = context.getResources().getIntArray(R.array.colors);
        this.random = new Random();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.cp_contact_row, null);
        }

        Contact contact = getItem(position);

        if(contact != null){

            MaterialLetterIcon letterIcon = (MaterialLetterIcon) view.findViewById(R.id.letterIcon);
            TextView tvName = (TextView) view.findViewById(R.id.cp_tvName);
            TextView tvCommunication = (TextView) view.findViewById(R.id.cp_tvCommunication);

            List<String> cellphones = contact.getCellphones();
            List<String> emails = contact.getEmails();


            tvName.setText(contact.getDisplayName());
            letterIcon.setLetter("" + contact.getDisplayName().charAt(0));
            letterIcon.setShapeColor(mMaterialColors[random.nextInt(mMaterialColors.length)]);
            if(!cellphones.isEmpty())
                tvCommunication.setText(cellphones.get(0));
            else if(!emails.isEmpty())
                tvCommunication.setText(emails.get(0));




        }

        return view;
    }
}
