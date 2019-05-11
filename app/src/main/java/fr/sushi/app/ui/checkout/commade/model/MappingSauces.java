package fr.sushi.app.ui.checkout.commade.model;

import com.google.gson.annotations.SerializedName;


public class MappingSauces{

	@SerializedName("730")
	private String jsonMember730;

	@SerializedName("514")
	private String jsonMember514;

	@SerializedName("515")
	private String jsonMember515;

	@SerializedName("516")
	private String jsonMember516;

	@SerializedName("517")
	private String jsonMember517;

	public void setJsonMember730(String jsonMember730){
		this.jsonMember730 = jsonMember730;
	}

	public String getJsonMember730(){
		return jsonMember730;
	}

	public void setJsonMember514(String jsonMember514){
		this.jsonMember514 = jsonMember514;
	}

	public String getJsonMember514(){
		return jsonMember514;
	}

	public void setJsonMember515(String jsonMember515){
		this.jsonMember515 = jsonMember515;
	}

	public String getJsonMember515(){
		return jsonMember515;
	}

	public void setJsonMember516(String jsonMember516){
		this.jsonMember516 = jsonMember516;
	}

	public String getJsonMember516(){
		return jsonMember516;
	}

	public void setJsonMember517(String jsonMember517){
		this.jsonMember517 = jsonMember517;
	}

	public String getJsonMember517(){
		return jsonMember517;
	}

	@Override
 	public String toString(){
		return 
			"MappingSauces{" + 
			"730 = '" + jsonMember730 + '\'' + 
			",514 = '" + jsonMember514 + '\'' + 
			",515 = '" + jsonMember515 + '\'' + 
			",516 = '" + jsonMember516 + '\'' + 
			",517 = '" + jsonMember517 + '\'' + 
			"}";
		}
}