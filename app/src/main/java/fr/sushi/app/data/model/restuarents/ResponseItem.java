package fr.sushi.app.data.model.restuarents;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public class ResponseItem implements ClusterItem {

	/*@SerializedName("Schedules")
	private Schedules schedules;*/

	@SerializedName("address")
	private String address;

	@SerializedName("lng")
	private double lng;

	@SerializedName("is_open")
	private int isOpen;

	@SerializedName("city")
	private String city;

	@SerializedName("active_fidelity")
	private int activeFidelity;

	@SerializedName("today_schedules")
	private List<TodaySchedulesItem> todaySchedules;

	@SerializedName("postcode")
	private String postcode;

	@SerializedName("active")
	private int active;

	@SerializedName("payment_merchant_account")
	private String paymentMerchantAccount;

	@SerializedName("id_store")
	private String idStore;

	@SerializedName("picture")
	private String picture;

	@SerializedName("pictures")
	private List<PicturesItem> pictures;

	@SerializedName("is_franchise")
	private int isFranchise;

	@SerializedName("active_app")
	private int activeApp;

	@SerializedName("delivery_area")
	private String deliveryArea;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("lat")
	private double lat;

	@SerializedName("active_online_payment")
	private String activeOnlinePayment;

	//active_online_payment
/*
	public void setSchedules(Schedules schedules){
		this.schedules = schedules;
	}

	public Schedules getSchedules(){
		return schedules;
	}
*/

	public String getActiveOnlinePayment() {
		return activeOnlinePayment;
	}

	public void setActiveOnlinePayment(String activeOnlinePayment) {
		this.activeOnlinePayment = activeOnlinePayment;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLng(double lng){
		this.lng = lng;
	}

	public double getLng(){
		return lng;
	}

	public void setIsOpen(int isOpen){
		this.isOpen = isOpen;
	}

	public int getIsOpen(){
		return isOpen;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setActiveFidelity(int activeFidelity){
		this.activeFidelity = activeFidelity;
	}

	public int getActiveFidelity(){
		return activeFidelity;
	}

	public void setTodaySchedules(List<TodaySchedulesItem> todaySchedules){
		this.todaySchedules = todaySchedules;
	}

	public List<TodaySchedulesItem> getTodaySchedules(){
		return todaySchedules;
	}

	public void setPostcode(String postcode){
		this.postcode = postcode;
	}

	public String getPostcode(){
		return postcode;
	}

	public void setActive(int active){
		this.active = active;
	}

	public int getActive(){
		return active;
	}

	public void setPaymentMerchantAccount(String paymentMerchantAccount){
		this.paymentMerchantAccount = paymentMerchantAccount;
	}

	public String getPaymentMerchantAccount(){
		return paymentMerchantAccount;
	}

	public void setIdStore(String idStore){
		this.idStore = idStore;
	}

	public String getIdStore(){
		return idStore;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	public void setPictures(List<PicturesItem> pictures){
		this.pictures = pictures;
	}

	public List<PicturesItem> getPictures(){
		return pictures;
	}

	public void setIsFranchise(int isFranchise){
		this.isFranchise = isFranchise;
	}

	public int getIsFranchise(){
		return isFranchise;
	}

	public void setActiveApp(int activeApp){
		this.activeApp = activeApp;
	}

	public int getActiveApp(){
		return activeApp;
	}

	public void setDeliveryArea(String deliveryArea){
		this.deliveryArea = deliveryArea;
	}

	public String getDeliveryArea(){
		return deliveryArea;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"ResponseItem{" +
			",address = '" + address + '\'' + 
			",lng = '" + lng + '\'' + 
			",is_open = '" + isOpen + '\'' + 
			",city = '" + city + '\'' + 
			",active_fidelity = '" + activeFidelity + '\'' + 
			",today_schedules = '" + todaySchedules + '\'' + 
			",postcode = '" + postcode + '\'' + 
			",active = '" + active + '\'' + 
			",payment_merchant_account = '" + paymentMerchantAccount + '\'' + 
			",id_store = '" + idStore + '\'' + 
			",picture = '" + picture + '\'' + 
			",pictures = '" + pictures + '\'' + 
			",is_franchise = '" + isFranchise + '\'' + 
			",active_app = '" + activeApp + '\'' + 
			",delivery_area = '" + deliveryArea + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return "";
    }
}