package fr.sushi.app.data.model.address_picker;

import com.google.gson.annotations.SerializedName;


public class SchedualeDate {

	@SerializedName("display_value")
	private String displayValue;

	@SerializedName("order_date")
	private String orderDate;

	@SerializedName("delay")
	private int delay;

	@SerializedName("Schedule")
	private String schedule;

	@SerializedName("id_store")
	private String idStore;

	@SerializedName("id_delivery_zone")
	private String idDeliveryZone;

	public void setDisplayValue(String displayValue){
		this.displayValue = displayValue;
	}

	public String getDisplayValue(){
		return displayValue;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setDelay(int delay){
		this.delay = delay;
	}

	public int getDelay(){
		return delay;
	}

	public void setSchedule(String schedule){
		this.schedule = schedule;
	}

	public String getSchedule(){
		return schedule;
	}

	public void setIdStore(String idStore){
		this.idStore = idStore;
	}

	public String getIdStore(){
		return idStore;
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
			"SchedualeDate{" +
			"display_value = '" + displayValue + '\'' + 
			",order_date = '" + orderDate + '\'' + 
			",delay = '" + delay + '\'' + 
			",schedule = '" + schedule + '\'' + 
			",id_store = '" + idStore + '\'' + 
			",id_delivery_zone = '" + idDeliveryZone + '\'' + 
			"}";
		}
}