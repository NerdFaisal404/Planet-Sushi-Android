package fr.sushi.app.ui.home.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("bdd_version")
	private double bddVersion;

	@SerializedName("home_slides")
	private List<HomeSlidesItem> homeSlides;

	@SerializedName("img_base_url")
	private String imgBaseUrl;

	@SerializedName("ws")
	private Ws ws;

	public void setBddVersion(double bddVersion){
		this.bddVersion = bddVersion;
	}

	public double getBddVersion(){
		return bddVersion;
	}

	public void setHomeSlides(List<HomeSlidesItem> homeSlides){
		this.homeSlides = homeSlides;
	}

	public List<HomeSlidesItem> getHomeSlides(){
		return homeSlides;
	}

	public void setImgBaseUrl(String imgBaseUrl){
		this.imgBaseUrl = imgBaseUrl;
	}

	public String getImgBaseUrl(){
		return imgBaseUrl;
	}

	public void setWs(Ws ws){
		this.ws = ws;
	}

	public Ws getWs(){
		return ws;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"bdd_version = '" + bddVersion + '\'' + 
			",home_slides = '" + homeSlides + '\'' + 
			",img_base_url = '" + imgBaseUrl + '\'' + 
			",ws = '" + ws + '\'' + 
			"}";
		}
}