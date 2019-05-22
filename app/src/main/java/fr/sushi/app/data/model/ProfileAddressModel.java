package fr.sushi.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/10/2019 at 10:48 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/10/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class ProfileAddressModel extends BaseAddress implements Parcelable {
    private String id;
    private String addressType;
    private String location;
    private String city;
    private String zipCode;
    private String building;
    private String floor;
    private String appartment;
    private String company;
    private String interphone;
    private String accessCode;
    private String information;

    public ProfileAddressModel() {
    }

    protected ProfileAddressModel(Parcel in) {
        id = in.readString();
        addressType = in.readString();
        location = in.readString();
        city = in.readString();
        zipCode = in.readString();
        building = in.readString();
        floor = in.readString();
        appartment = in.readString();
        company = in.readString();
        interphone = in.readString();
        accessCode = in.readString();
        information = in.readString();
    }

    public static final Creator<ProfileAddressModel> CREATOR = new Creator<ProfileAddressModel>() {
        @Override
        public ProfileAddressModel createFromParcel(Parcel in) {
            return new ProfileAddressModel(in);
        }

        @Override
        public ProfileAddressModel[] newArray(int size) {
            return new ProfileAddressModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getInterphone() {
        return interphone;
    }

    public void setInterphone(String interphone) {
        this.interphone = interphone;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(addressType);
        parcel.writeString(location);
        parcel.writeString(city);
        parcel.writeString(zipCode);
        parcel.writeString(building);
        parcel.writeString(floor);
        parcel.writeString(appartment);
        parcel.writeString(company);
        parcel.writeString(interphone);
        parcel.writeString(accessCode);
        parcel.writeString(information);
    }
}
