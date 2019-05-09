package fr.sushi.app.data.model.restuarents;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RestuarentsResponse{

	@SerializedName("response")
	private List<ResponseItem> response;

/*	@SerializedName("error")
	private boolean error;*/

	public void setResponse(List<ResponseItem> response){
		this.response = response;
	}

	public List<ResponseItem> getResponse(){
		return response;
	}

	/*public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}*/

	@Override
 	public String toString(){
		return 
			"RestuarentsResponse{" + 
			"response = '" + response + '\'' +
			"}";
		}
}