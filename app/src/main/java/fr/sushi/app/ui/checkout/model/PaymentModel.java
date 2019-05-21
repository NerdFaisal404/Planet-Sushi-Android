package fr.sushi.app.ui.checkout.model;

import com.google.gson.annotations.SerializedName;


public class PaymentModel{

	@SerializedName("order_date")
	private String orderDate;

	@SerializedName("amount")
	private double amount;

	@SerializedName("app_token")
	private String appToken;

	@SerializedName("id_customer")
	private String idCustomer;

	@SerializedName("channel")
	private String channel;

	@SerializedName("id_store")
	private String idStore;

	@SerializedName("returnUrl")
	private String returnUrl;

	@SerializedName("id_delivery_zone")
	private String idDeliveryZone;

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setAmount(double amount){
		this.amount = amount;
	}

	public double getAmount(){
		return amount;
	}

	public void setAppToken(String appToken){
		this.appToken = appToken;
	}

	public String getAppToken(){
		return appToken;
	}

	public void setIdCustomer(String idCustomer){
		this.idCustomer = idCustomer;
	}

	public String getIdCustomer(){
		return idCustomer;
	}

	public void setChannel(String channel){
		this.channel = channel;
	}

	public String getChannel(){
		return channel;
	}

	public void setIdStore(String idStore){
		this.idStore = idStore;
	}

	public String getIdStore(){
		return idStore;
	}

	public void setReturnUrl(String returnUrl){
		this.returnUrl = returnUrl;
	}

	public String getReturnUrl(){
		return returnUrl;
	}

	public void setIdDeliveryZone(String idDeliveryZone){
		this.idDeliveryZone = idDeliveryZone;
	}

	public String getIdDeliveryZone(){
		return idDeliveryZone;
	}

	@Override
 	public String toString(){
		return 
			"PaymentModel{" + 
			"order_date = '" + orderDate + '\'' + 
			",amount = '" + amount + '\'' + 
			",app_token = '" + appToken + '\'' + 
			",id_customer = '" + idCustomer + '\'' + 
			",channel = '" + channel + '\'' + 
			",id_store = '" + idStore + '\'' + 
			",returnUrl = '" + returnUrl + '\'' + 
			",id_delivery_zone = '" + idDeliveryZone + '\'' + 
			"}";
		}
}