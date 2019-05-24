package fr.sushi.app.ui.checkout.model.payment_success;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("id_order")
	private int idOrder;

	public void setIdOrder(int idOrder){
		this.idOrder = idOrder;
	}

	public int getIdOrder(){
		return idOrder;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"id_order = '" + idOrder + '\'' + 
			"}";
		}
}