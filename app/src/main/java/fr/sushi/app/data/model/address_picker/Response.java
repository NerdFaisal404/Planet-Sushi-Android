package fr.sushi.app.data.model.address_picker;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("id_amabis")
	private boolean idAmabis;

	@SerializedName("Schedules")
	private Schedules schedules;

	@SerializedName("iris")
	private boolean iris;

	@SerializedName("postcode")
	private String postcode;

	public void setIdAmabis(boolean idAmabis){
		this.idAmabis = idAmabis;
	}

	public boolean isIdAmabis(){
		return idAmabis;
	}

	public void setSchedules(Schedules schedules){
		this.schedules = schedules;
	}

	public Schedules getSchedules(){
		return schedules;
	}

	public void setIris(boolean iris){
		this.iris = iris;
	}

	public boolean isIris(){
		return iris;
	}

	public void setPostcode(String postcode){
		this.postcode = postcode;
	}

	public String getPostcode(){
		return postcode;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"id_amabis = '" + idAmabis + '\'' + 
			",schedules = '" + schedules + '\'' + 
			",iris = '" + iris + '\'' + 
			",postcode = '" + postcode + '\'' + 
			"}";
		}
}