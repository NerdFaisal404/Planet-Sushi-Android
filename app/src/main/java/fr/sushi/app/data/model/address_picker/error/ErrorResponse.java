package fr.sushi.app.data.model.address_picker.error;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse{

	@SerializedName("error_string")
	private String errorString;

	@SerializedName("error")
	private boolean error;

	public void setErrorString(String errorString){
		this.errorString = errorString;
	}

	public String getErrorString(){
		return errorString;
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
			"ErrorResponse{" + 
			"error_string = '" + errorString + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}