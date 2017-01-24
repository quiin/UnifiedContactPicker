package mx.com.quiin.contactpicker.views;

import android.view.View;
import android.widget.TextView;


import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import mx.com.quiin.contactpicker.R;

/**
 * Created by Carlos Reyna on 22/01/17.
 */

public class CommunicationViewHolder extends ChildViewHolder {

    public RoundedImageView ivCommunicationIcon;
    public TextView tvCommunication;

    public CommunicationViewHolder(View view) {
        super(view);
        ivCommunicationIcon = (RoundedImageView) view.findViewById(R.id.ivCommunicationIcon);
        tvCommunication = (TextView) view.findViewById(R.id.tvCommunication);
    }
}
