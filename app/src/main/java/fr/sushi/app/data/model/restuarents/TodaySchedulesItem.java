package fr.sushi.app.data.model.restuarents;

import com.google.gson.annotations.SerializedName;

public class TodaySchedulesItem{

	@SerializedName("day_name")
	private String dayName;

	@SerializedName("store_name")
	private String storeName;

	@SerializedName("time_end")
	private String timeEnd;

	@SerializedName("position")
	private int position;

	@SerializedName("id_store")
	private int idStore;

	@SerializedName("day")
	private int day;

	@SerializedName("time_deb")
	private String timeDeb;

	@SerializedName("id_store_schedule")
	private String idStoreSchedule;

	@SerializedName("is_closed")
	private int isClosed;

	public void setDayName(String dayName){
		this.dayName = dayName;
	}

	public String getDayName(){
		return dayName;
	}

	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

	public String getStoreName(){
		return storeName;
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

	public void setIdStore(int idStore){
		this.idStore = idStore;
	}

	public int getIdStore(){
		return idStore;
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

	public void setIdStoreSchedule(String idStoreSchedule){
		this.idStoreSchedule = idStoreSchedule;
	}

	public String getIdStoreSchedule(){
		return idStoreSchedule;
	}

	public void setIsClosed(int isClosed){
		this.isClosed = isClosed;
	}

	public int getIsClosed(){
		return isClosed;
	}

	@Override
 	public String toString(){
		return 
			"TodaySchedulesItem{" + 
			"day_name = '" + dayName + '\'' + 
			",store_name = '" + storeName + '\'' + 
			",time_end = '" + timeEnd + '\'' + 
			",position = '" + position + '\'' + 
			",id_store = '" + idStore + '\'' + 
			",day = '" + day + '\'' + 
			",time_deb = '" + timeDeb + '\'' + 
			",id_store_schedule = '" + idStoreSchedule + '\'' + 
			",is_closed = '" + isClosed + '\'' + 
			"}";
		}
}