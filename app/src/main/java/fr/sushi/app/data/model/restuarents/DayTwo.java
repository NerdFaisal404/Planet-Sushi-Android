package fr.sushi.app.data.model.restuarents;

import com.google.gson.annotations.SerializedName;

public class DayTwo {

	@SerializedName("day_name")
	private String dayName;

	@SerializedName("Schedule")
	private String schedule;

	public void setDayName(String dayName){
		this.dayName = dayName;
	}

	public String getDayName(){
		return dayName;
	}

	public void setSchedule(String schedule){
		this.schedule = schedule;
	}

	public String getSchedule(){
		return schedule;
	}

	@Override
 	public String toString(){
		return 
			"DayTwo{" +
			"day_name = '" + dayName + '\'' + 
			",schedule = '" + schedule + '\'' + 
			"}";
		}
}