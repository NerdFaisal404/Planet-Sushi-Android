package fr.sushi.app.data.model.restuarents;


import com.google.gson.annotations.SerializedName;

public class Schedules{

	@SerializedName("1")
	private DayOne dayOne;

	@SerializedName("2")
	private DayTwo dayTwo;

	@SerializedName("3")
	private DayThree dayThree;

	@SerializedName("4")
	private DayFour dayFour;

	@SerializedName("5")
	private DayFive dayFive;

	@SerializedName("6")
	private DaySix daySix;

	@SerializedName("7")
	private DaySeven daySeven;

	public void setDayOne(DayOne dayOne){
		this.dayOne = dayOne;
	}

	public DayOne getDayOne(){
		return dayOne;
	}

	public void setDayTwo(DayTwo dayTwo){
		this.dayTwo = dayTwo;
	}

	public DayTwo getDayTwo(){
		return dayTwo;
	}

	public void setDayThree(DayThree dayThree){
		this.dayThree = dayThree;
	}

	public DayThree getDayThree(){
		return dayThree;
	}

	public void setDayFour(DayFour dayFour){
		this.dayFour = dayFour;
	}

	public DayFour getDayFour(){
		return dayFour;
	}

	public void setDayFive(DayFive dayFive){
		this.dayFive = dayFive;
	}

	public DayFive getDayFive(){
		return dayFive;
	}

	public void setDaySix(DaySix daySix){
		this.daySix = daySix;
	}

	public DaySix getDaySix(){
		return daySix;
	}

	public void setDaySeven(DaySeven daySeven){
		this.daySeven = daySeven;
	}

	public DaySeven getDaySeven(){
		return daySeven;
	}

	@Override
 	public String toString(){
		return 
			"Schedules{" + 
			"1 = '" + dayOne + '\'' +
			",2 = '" + dayTwo + '\'' +
			",3 = '" + dayThree + '\'' +
			",4 = '" + dayFour + '\'' +
			",5 = '" + dayFive + '\'' +
			",6 = '" + daySix + '\'' +
			",7 = '" + daySeven + '\'' +
			"}";
		}
}