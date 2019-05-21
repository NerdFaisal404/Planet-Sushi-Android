package fr.sushi.app.ui.checkout.model;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("paymentSession")
	private String paymentSession;

	public void setPaymentSession(String paymentSession){
		this.paymentSession = paymentSession;
	}

	public String getPaymentSession(){
		return paymentSession;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"paymentSession = '" + paymentSession + '\'' + 
			"}";
		}
}