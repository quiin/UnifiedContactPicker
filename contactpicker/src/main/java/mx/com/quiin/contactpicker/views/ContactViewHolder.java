package mx.com.quiin.contactpicker.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import mx.com.quiin.contactpicker.R;

/**
 * Created by Carlos Reyna on 22/01/17.
 */

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
