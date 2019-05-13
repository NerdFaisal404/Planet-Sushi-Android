package fr.sushi.app.ui.checkout.commade.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Response{


	//let collectionViewDataKeys: [String] = ["paying_sauces", "upsell", "drinks", "desserts", "paying_wasabi_ginger", "chopsticks"]

	//FWasbi and ginger 5th item
	@SerializedName("paying_wasabi_ginger")
	private List<PayingWasabiGingerItem> payingWasabiGinger;

	//4th Item Deserts
	@SerializedName("desserts")
	private List<DessertsItem> desserts;

	@SerializedName("free_wasabi_ginger")
	private List<FreeWasabiGingerItem> freeWasabiGinger;

	@SerializedName("mapping_wasabi_ginger")
	private MappingWasabiGinger mappingWasabiGinger;

	//Bossons third item
	@SerializedName("drinks")
	private List<DrinksItem> drinks;

	//6Th item baguettes
	@SerializedName("chopsticks")
	private List<ChopsticksItem> chopsticks;

	@SerializedName("free_sauces")
	private List<FreeSaucesItem> freeSauces;

	//sauces for 1st item
	@SerializedName("paying_sauces")
	private List<PayingSaucesItem> payingSauces;

	//Accompagements 2nd item
	@SerializedName("upsell")
	private List<UpsellItem> upsell;

	@SerializedName("mapping_sauces")
	private MappingSauces mappingSauces;

	public void setPayingWasabiGinger(List<PayingWasabiGingerItem> payingWasabiGinger){
		this.payingWasabiGinger = payingWasabiGinger;
	}

	public List<PayingWasabiGingerItem> getPayingWasabiGinger(){
		return payingWasabiGinger;
	}

	public void setDesserts(List<DessertsItem> desserts){
		this.desserts = desserts;
	}

	public List<DessertsItem> getDesserts(){
		return desserts;
	}

	public void setFreeWasabiGinger(List<FreeWasabiGingerItem> freeWasabiGinger){
		this.freeWasabiGinger = freeWasabiGinger;
	}

	public List<FreeWasabiGingerItem> getFreeWasabiGinger(){
		return freeWasabiGinger;
	}

	public void setMappingWasabiGinger(MappingWasabiGinger mappingWasabiGinger){
		this.mappingWasabiGinger = mappingWasabiGinger;
	}

	public MappingWasabiGinger getMappingWasabiGinger(){
		return mappingWasabiGinger;
	}

	public void setDrinks(List<DrinksItem> drinks){
		this.drinks = drinks;
	}

	public List<DrinksItem> getDrinks(){
		return drinks;
	}

	public void setChopsticks(List<ChopsticksItem> chopsticks){
		this.chopsticks = chopsticks;
	}

	public List<ChopsticksItem> getChopsticks(){
		return chopsticks;
	}

	public void setFreeSauces(List<FreeSaucesItem> freeSauces){
		this.freeSauces = freeSauces;
	}

	public List<FreeSaucesItem> getFreeSauces(){
		return freeSauces;
	}

	public void setPayingSauces(List<PayingSaucesItem> payingSauces){
		this.payingSauces = payingSauces;
	}

	public List<PayingSaucesItem> getPayingSauces(){
		return payingSauces;
	}

	public void setUpsell(List<UpsellItem> upsell){
		this.upsell = upsell;
	}

	public List<UpsellItem> getUpsell(){
		return upsell;
	}

	public void setMappingSauces(MappingSauces mappingSauces){
		this.mappingSauces = mappingSauces;
	}

	public MappingSauces getMappingSauces(){
		return mappingSauces;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"paying_wasabi_ginger = '" + payingWasabiGinger + '\'' + 
			",desserts = '" + desserts + '\'' + 
			",free_wasabi_ginger = '" + freeWasabiGinger + '\'' + 
			",mapping_wasabi_ginger = '" + mappingWasabiGinger + '\'' + 
			",drinks = '" + drinks + '\'' + 
			",chopsticks = '" + chopsticks + '\'' + 
			",free_sauces = '" + freeSauces + '\'' + 
			",paying_sauces = '" + payingSauces + '\'' + 
			",upsell = '" + upsell + '\'' + 
			",mapping_sauces = '" + mappingSauces + '\'' + 
			"}";
		}
}