package fr.sushi.app.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchPlace implements Parcelable {
    private String address;
    private double lat;
    private double lng;

    public SearchPlace(String address, double lat, double lng) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SearchPlace(Parcel parcel){
        address = parcel.readString();
        lat = parcel.readDouble();
        lng = parcel.readDouble();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }

    public static Creator<SearchPlace> CREATOR = new Creator<SearchPlace>() {
        @Override
        public SearchPlace createFromParcel(Parcel parcel) {
            return new SearchPlace(parcel);
        }

        @Override
        public SearchPlace[] newArray(int size) {
            return new SearchPlace[size];
        }
    };
}
