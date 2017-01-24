package mx.com.quiin.contactpicker;

import java.io.Serializable;

/**
 * Created by Carlos Reyna on 22/01/17.
 */

public class SimpleContact implements Comparable<SimpleContact>, Serializable{

    private String displayName;
    private String communication;

    public SimpleContact(String displayName, String communication) {
        this.displayName = displayName;
        this.communication = communication;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SimpleContact){
            SimpleContact contact = (SimpleContact) obj;
            return this.communication.equals(contact.communication);
        }
        return false;
    }

    @Override
    public String toString() {
        return "SimpleContact{" +
                "displayName='" + displayName + '\'' +
                ", communication='" + communication + '\'' +
                '}';
    }


    @Override
    public int compareTo(SimpleContact other) {
        return this.displayName.compareTo(other.displayName);
    }
}
