package mx.com.quiin.contactpicker;

import android.net.Uri;



import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Reyna on 20/01/17.
 */

public class Contact implements Parent<String>, Serializable{

    private String displayName;
    private boolean isSelected;
    private List<String> communications;
    private String selectedCommunication;


    /**** Constructors ******/
    public Contact(String displayName) {
        setDisplayName(displayName);
        setCommunications(new ArrayList<String>());


    }

    public Contact(String displayName, List<String> communications) {
        setDisplayName(displayName);
        setCommunications(communications);
    }



    /**** Getters ******/
    public String getDisplayName() {
        return displayName;
    }

    public List<String> getCommunications() {
        return communications;
    }

    public String getSelectedCommunication() {
        if(this.selectedCommunication == null)
            this.selectedCommunication = getDefaultCommunication();

        return this.selectedCommunication;
    }

    /**** Setters******/
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSelectedCommunication(String selectedCommunication) {
        this.selectedCommunication = selectedCommunication;
    }

    public void addCommunication(String communication){
        communication = communication.replaceAll(" ", "");
        if(!communications.contains(communication)) {
            communications.add(communication);
        }
    }

    public void setCommunications(List<String> communications) {
        if(communications == null)
            communications = new ArrayList<>();
        this.communications = communications;
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
                ", isSelected=" + isSelected +
                ", communications=" + communications +
                ", selectedCommunication='" + selectedCommunication + '\'' +
                '}';
    }

    public String getInitial() {
        return String.valueOf(this.displayName.charAt(0));
    }


    public String getDefaultCommunication() {
        if(communications.size() > 0)
            return communications.get(0);
        return "Not found";
    }



    public int getTotalCommunications(){
        return communications.size();
    }

    @Override
    public List<String> getChildList() {
        if(communications.size() > 1)
            return communications;
        return new ArrayList<>();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }


    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public SimpleContact simplify() {
        return new SimpleContact(displayName, selectedCommunication);
    }
}
