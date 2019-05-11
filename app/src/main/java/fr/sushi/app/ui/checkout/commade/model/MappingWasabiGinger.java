package fr.sushi.app.ui.checkout.commade.model;

import com.google.gson.annotations.SerializedName;


public class MappingWasabiGinger{

	@SerializedName("851")
	private String jsonMember851;

	@SerializedName("854")
	private String jsonMember854;

	public void setJsonMember851(String jsonMember851){
		this.jsonMember851 = jsonMember851;
	}

	public String getJsonMember851(){
		return jsonMember851;
	}

	public void setJsonMember854(String jsonMember854){
		this.jsonMember854 = jsonMember854;
	}

	public String getJsonMember854(){
		return jsonMember854;
	}

	@Override
 	public String toString(){
		return 
			"MappingWasabiGinger{" + 
			"851 = '" + jsonMember851 + '\'' + 
			",854 = '" + jsonMember854 + '\'' + 
			"}";
		}
}