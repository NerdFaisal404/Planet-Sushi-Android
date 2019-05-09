package fr.sushi.app.ui.home.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class GetStoreProducts{

	@SerializedName("description")
	private String description;

	@SerializedName("params")
	private List<String> params;

	@SerializedName("url")
	private String url;

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setParams(List<String> params){
		this.params = params;
	}

	public List<String> getParams(){
		return params;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"GetStoreProducts{" + 
			"description = '" + description + '\'' + 
			",params = '" + params + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}