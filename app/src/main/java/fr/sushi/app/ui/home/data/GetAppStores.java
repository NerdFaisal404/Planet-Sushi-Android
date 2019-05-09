package fr.sushi.app.ui.home.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetAppStores{

	@SerializedName("description")
	private String description;

	@SerializedName("params")
	private List<Object> params;

	@SerializedName("url")
	private String url;

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setParams(List<Object> params){
		this.params = params;
	}

	public List<Object> getParams(){
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
			"GetAppStores{" + 
			"description = '" + description + '\'' + 
			",params = '" + params + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}