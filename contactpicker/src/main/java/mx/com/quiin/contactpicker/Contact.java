package mx.com.quiin.contactpicker;

import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Reyna on 20/01/17.
 */

public class Contact {

    private String displayName;
    private List<String> cellphones;
    private List<String> emails;
    private Uri photoUri;

    /**** Constructors ******/
    public Contact(String displayName) {
        this.displayName = displayName;
        this.cellphones = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public Contact(String displayName, List<String> cellphones) {
        this.displayName = displayName;
        this.cellphones = cellphones;
        this.emails = new ArrayList<>();
    }

    public Contact(String displayName, List<String> cellphones, List<String> emails) {
        this.displayName = displayName;
        this.cellphones = cellphones;
        this.emails = emails;
    }

    /**** Getters ******/
    public String getDisplayName() {
        return displayName;
    }

    public List<String> getCellphones() {
        return cellphones == null ? new ArrayList<String>() : cellphones;
    }

    public List<String> getEmails() {
        return emails == null ? new ArrayList<String>() : emails;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    /**** Setters******/
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setCellphones(List<String> cellphones) {
        this.cellphones = cellphones;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public void addCommunication(String communication, String communicationType){
        communication = communication.replaceAll(" ", "");
        if(communicationType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) && !getEmails().contains(communication))
            getEmails().add(communication);
        else if (!getCellphones().contains(communication))
            getCellphones().add(communication);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Contact){
            Contact contact = (Contact) obj;
            return this.displayName.equals(contact.displayName);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "displayName='" + displayName + '\'' +
                ", cellphones=" + cellphones +
                ", emails=" + emails +
                '}';
    }

    public String getInitial() {
        return String.valueOf(this.displayName.charAt(0));
    }

    public List<String> getAllCommunications(){
        List<String> all = new ArrayList<>(getEmails());
        all.addAll(getCellphones());
        return all;
    }

    public String findCommunication(String selected) {
        List<String> all = getAllCommunications();
        int index = all.indexOf(selected);
        if(index >= 0)
            return all.get(index);

        return all.get(0);
    }
}
