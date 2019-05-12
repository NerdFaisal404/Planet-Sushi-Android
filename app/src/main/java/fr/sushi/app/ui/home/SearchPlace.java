package fr.sushi.app.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SearchPlace implements Serializable {
    private String postalCode;
    private String city;
    private String address;
    private String title;
    private String time;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SearchPlace(String postalCode, String city, String address){
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
