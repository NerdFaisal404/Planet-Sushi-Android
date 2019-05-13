package fr.sushi.app.data.model.food_menu;

import com.google.gson.annotations.SerializedName;

public class CrossSellingItem{

	@SerializedName("only_pm")
	private int onlyPm;

	@SerializedName("category_name")
	private String categoryName;

	@SerializedName("id_category")
	private int idCategory;

	@SerializedName("description")
	private String description;

	@SerializedName("only_am")
	private int onlyAm;

	@SerializedName("active_webmobile")
	private int activeWebmobile;

	@SerializedName("id_product")
	private int idProduct;

	@SerializedName("id_product_cross_selling")
	private String idProductCrossSelling;

	@SerializedName("is_pack")
	private int isPack;

	@SerializedName("is_required")
	private int isRequired;

	@SerializedName("is_attached")
	private int isAttached;

	@SerializedName("error_description")
	private String errorDescription;

	@SerializedName("is_free")
	private int isFree;

	@SerializedName("quantity_max")
	private int quantityMax;

	@SerializedName("position")
	private int position;

	public void setOnlyPm(int onlyPm){
		this.onlyPm = onlyPm;
	}

	public int getOnlyPm(){
		return onlyPm;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public void setIdCategory(int idCategory){
		this.idCategory = idCategory;
	}

	public int getIdCategory(){
		return idCategory;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setOnlyAm(int onlyAm){
		this.onlyAm = onlyAm;
	}

	public int getOnlyAm(){
		return onlyAm;
	}

	public void setActiveWebmobile(int activeWebmobile){
		this.activeWebmobile = activeWebmobile;
	}

	public int getActiveWebmobile(){
		return activeWebmobile;
	}

	public void setIdProduct(int idProduct){
		this.idProduct = idProduct;
	}

	public int getIdProduct(){
		return idProduct;
	}

	public void setIdProductCrossSelling(String idProductCrossSelling){
		this.idProductCrossSelling = idProductCrossSelling;
	}

	public String getIdProductCrossSelling(){
		return idProductCrossSelling;
	}

	public void setIsPack(int isPack){
		this.isPack = isPack;
	}

	public int getIsPack(){
		return isPack;
	}

	public void setIsRequired(int isRequired){
		this.isRequired = isRequired;
	}

	public int getIsRequired(){
		return isRequired;
	}

	public void setIsAttached(int isAttached){
		this.isAttached = isAttached;
	}

	public int getIsAttached(){
		return isAttached;
	}

	public void setErrorDescription(String errorDescription){
		this.errorDescription = errorDescription;
	}

	public String getErrorDescription(){
		return errorDescription;
	}

	public void setIsFree(int isFree){
		this.isFree = isFree;
	}

	public int getIsFree(){
		return isFree;
	}

	public void setQuantityMax(int quantityMax){
		this.quantityMax = quantityMax;
	}

	public int getQuantityMax(){
		return quantityMax;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
	}

	@Override
 	public String toString(){
		return 
			"CrossSellingItem{" + 
			"only_pm = '" + onlyPm + '\'' + 
			",category_name = '" + categoryName + '\'' + 
			",id_category = '" + idCategory + '\'' + 
			",description = '" + description + '\'' + 
			",only_am = '" + onlyAm + '\'' + 
			",active_webmobile = '" + activeWebmobile + '\'' + 
			",id_product = '" + idProduct + '\'' + 
			",id_product_cross_selling = '" + idProductCrossSelling + '\'' + 
			",is_pack = '" + isPack + '\'' + 
			",is_required = '" + isRequired + '\'' + 
			",is_attached = '" + isAttached + '\'' + 
			",error_description = '" + errorDescription + '\'' + 
			",is_free = '" + isFree + '\'' + 
			",quantity_max = '" + quantityMax + '\'' + 
			",position = '" + position + '\'' + 
			"}";
		}
}