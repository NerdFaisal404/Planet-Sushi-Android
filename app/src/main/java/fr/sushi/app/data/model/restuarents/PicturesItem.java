package fr.sushi.app.data.model.restuarents;

import com.google.gson.annotations.SerializedName;

public class PicturesItem{

	@SerializedName("url")
	private String url;

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"PicturesItem{" + 
			"url = '" + url + '\'' + 
			"}";
		}
}