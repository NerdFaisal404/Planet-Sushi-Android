package fr.sushi.app.ui.checkout.model.payment_success;

import com.google.gson.annotations.SerializedName;


public class PaymentSuccessResponse{

	@SerializedName("response")
	private Response response;

	@SerializedName("error")
	private boolean error;

	public void setResponse(Response response){
		this.response = response;
	}

	public Response getResponse(){
		return response;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"PaymentSuccessResponse{" + 
			"response = '" + response + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}