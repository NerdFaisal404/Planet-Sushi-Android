package fr.sushi.app.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchPlace implements Parcelable {
    private String postalCode;
    private String city;
    private String address;

    public SearchPlace(String postalCode, String city,String address){
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

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public SearchPlace(Parcel parcel){
        address = parcel.readString();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);

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
