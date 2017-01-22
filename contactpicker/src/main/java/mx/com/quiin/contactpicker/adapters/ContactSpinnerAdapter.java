package mx.com.quiin.contactpicker.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.R;
import mx.com.quiin.contactpicker.Utils;

/**
 * Created by Carlos Reyna on 20/01/17.
 */

public class ContactSpinnerAdapter extends ArrayAdapter<String>{

    private final LayoutInflater inflater;
    private final List<String> mCommunications;
    private final String contactName;
    private final ClickListener mListener;

    public ContactSpinnerAdapter(Context context, int resource, List<String> objects, String contactName, ClickListener listener) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.mCommunications = objects;
        this.contactName = contactName;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent, false);
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView, parent, true);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, boolean inDropDown) {
        View view = inflater.inflate(R.layout.cp_spinner_row, null);

        ImageView ivCommunicationIcon = (ImageView) view.findViewById(R.id.ivCommunicationIcon);
        ImageView ivSelectedCommunication = (ImageView) view.findViewById(R.id.cp_ivSelectedComm);
        TextView tvCommunication = (TextView) view.findViewById(R.id.tvCommunication);
        TextView tvDisplayName = (TextView) view.findViewById(R.id.tvDisplayName);

        tvDisplayName.setText(contactName);

        final String communication = mCommunications.get(position);


        if(communication != null){
            tvCommunication.setText(communication);
            if(Utils.isEmail(communication))
                ivCommunicationIcon.setImageResource(R.drawable.ic_email);
            else
                ivCommunicationIcon.setImageResource(R.drawable.ic_message);
        }else{
            ivCommunicationIcon.setVisibility(View.GONE);
            tvCommunication.setText(R.string.not_found);
        }

        if(position == 0){
            ivCommunicationIcon.setVisibility(View.GONE);
            ivSelectedCommunication.setVisibility(View.VISIBLE);

            if(Utils.isEmail(communication))
                ivSelectedCommunication.setImageResource(R.drawable.ic_email);
            else
                ivSelectedCommunication.setImageResource(R.drawable.ic_message);

        }else{
            ivSelectedCommunication.setVisibility(View.GONE);
        }

        if (inDropDown && position == 0){
            ivCommunicationIcon.setVisibility(View.VISIBLE);
            ivSelectedCommunication.setVisibility(View.GONE);
        }

        if(!inDropDown && position == 0){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSpinnerClick(communication, contactName);
                }
            });
        }

        return  view;
    }



    public interface ClickListener{
        void onSpinnerClick(String communication, String displayName);
    }
}
