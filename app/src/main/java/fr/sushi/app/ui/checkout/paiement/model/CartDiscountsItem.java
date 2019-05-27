package fr.sushi.app.ui.checkout.paiement.model;

import com.google.gson.annotations.SerializedName;

public class CartDiscountsItem{

	@SerializedName("id_cart_discount")
	private int idCartDiscount;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("display_amount")
	private String displayAmount;

	@SerializedName("value")
	private String value;

	public void setIdCartDiscount(int idCartDiscount){
		this.idCartDiscount = idCartDiscount;
	}

	public int getIdCartDiscount(){
		return idCartDiscount;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setDisplayAmount(String displayAmount){
		this.displayAmount = displayAmount;
	}

	public String getDisplayAmount(){
		return displayAmount;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"CartDiscountsItem{" + 
			"id_cart_discount = '" + idCartDiscount + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",display_amount = '" + displayAmount + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}