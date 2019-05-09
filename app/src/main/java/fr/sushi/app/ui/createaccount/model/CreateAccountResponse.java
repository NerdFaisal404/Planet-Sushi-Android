package fr.sushi.app.ui.createaccount.model;

import com.google.gson.annotations.SerializedName;


public class CreateAccountResponse{

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
			"CreateAccountResponse{" + 
			"response = '" + response + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}