package fr.sushi.app.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import fr.sushi.app.data.model.address_picker.Order;

public class SearchPlace implements Serializable {
    private String postalCode;
    private String city;
    private String address;
    private String title;
    private String time;
    private String type;
    private double lat;
    private double lng;
    private Order order;


    public SearchPlace(String postalCode, String city, String address, double lat, double lng){
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public SearchPlace(String postalCode, String city, String address){
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
    }
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

    public String getAddress() {
        return address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
