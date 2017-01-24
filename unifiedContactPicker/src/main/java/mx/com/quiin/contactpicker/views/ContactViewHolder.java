package mx.com.quiin.contactpicker.views;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import mx.com.quiin.contactpicker.R;

/**
 * Created by Carlos Reyna on 22/01/17.
 */

public class ContactViewHolder extends ParentViewHolder{

    public MaterialLetterIcon letterIcon;
    public TextView tvDisplayName, tvCommunication;
    public ImageView ivSelected, ivSelectedCommunication, ivExpandArrow;
    public View expandableArea;

    public ContactViewHolder(View view) {
        super(view);
        letterIcon = (MaterialLetterIcon) view.findViewById(R.id.letterIcon);
        ivSelected = (ImageView) view.findViewById(R.id.cp_ivSelected);
        ivSelectedCommunication = (ImageView) view.findViewById(R.id.cp_ivSelectedComm);
        ivExpandArrow = (ImageView) view.findViewById(R.id.cp_arrowExpand);
        tvDisplayName = (TextView) view.findViewById(R.id.tvDisplayName);
        tvCommunication = (TextView) view.findViewById(R.id.tvCommunication);
        expandableArea = view.findViewById(R.id.cp_clickArea);
    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }
}
