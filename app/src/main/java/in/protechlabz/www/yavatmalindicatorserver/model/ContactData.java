package in.protechlabz.www.yavatmalindicatorserver.model;

import java.io.Serializable;

/**
 * Created by Nikesh on 23/12/2016.
 */
public class ContactData implements Serializable{

    private String name;
    private String address;
    //private String telephone;
    private long telephone;
    private double latitude;
    private double longitude;

    public ContactData() {
    }

    public ContactData(String name, String address, long telephone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public long getTelephone() {
        return telephone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
