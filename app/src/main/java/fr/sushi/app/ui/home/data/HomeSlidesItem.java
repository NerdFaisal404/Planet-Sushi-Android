package fr.sushi.app.ui.home.data;

import com.google.gson.annotations.SerializedName;


public class HomeSlidesItem{

	@SerializedName("is_video")
	private int isVideo;

	@SerializedName("time_end")
	private String timeEnd;

	@SerializedName("position")
	private int position;

	@SerializedName("day")
	private int day;

	@SerializedName("time_deb")
	private String timeDeb;

	@SerializedName("picture")
	private String picture;

	public void setIsVideo(int isVideo){
		this.isVideo = isVideo;
	}

	public int getIsVideo(){
		return isVideo;
	}

	public void setTimeEnd(String timeEnd){
		this.timeEnd = timeEnd;
	}

	public String getTimeEnd(){
		return timeEnd;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
	}

	public void setDay(int day){
		this.day = day;
	}

	public int getDay(){
		return day;
	}

	public void setTimeDeb(String timeDeb){
		this.timeDeb = timeDeb;
	}

	public String getTimeDeb(){
		return timeDeb;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	@Override
 	public String toString(){
		return 
			"HomeSlidesItem{" + 
			"is_video = '" + isVideo + '\'' + 
			",time_end = '" + timeEnd + '\'' + 
			",position = '" + position + '\'' + 
			",day = '" + day + '\'' + 
			",time_deb = '" + timeDeb + '\'' + 
			",picture = '" + picture + '\'' + 
			"}";
		}
}