package fr.sushi.app.data.model.food_menu;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CrossSellingCategoriesItem{

	@SerializedName("sub_categories")
	private List<Object> subCategories;

	@SerializedName("Products")
	private List<ProductsItem> products;

	@SerializedName("deleted")
	private String deleted;

	@SerializedName("picture_url")
	private String pictureUrl;

	@SerializedName("id_category")
	private String idCategory;

	@SerializedName("name")
	private String name;

	@SerializedName("footer_seo")
	private String footerSeo;

	@SerializedName("link")
	private String link;

	@SerializedName("active")
	private String active;

	@SerializedName("html_name")
	private String htmlName;

	@SerializedName("position")
	private String position;

	@SerializedName("id_parent")
	private String idParent;

	public void setSubCategories(List<Object> subCategories){
		this.subCategories = subCategories;
	}

	public List<Object> getSubCategories(){
		return subCategories;
	}

	public void setProducts(List<ProductsItem> products){
		this.products = products;
	}

	public List<ProductsItem> getProducts(){
		return products;
	}

	public void setDeleted(String deleted){
		this.deleted = deleted;
	}

	public String getDeleted(){
		return deleted;
	}

	public void setPictureUrl(String pictureUrl){
		this.pictureUrl = pictureUrl;
	}

	public String getPictureUrl(){
		return pictureUrl;
	}

	public void setIdCategory(String idCategory){
		this.idCategory = idCategory;
	}

	public String getIdCategory(){
		return idCategory;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setFooterSeo(String footerSeo){
		this.footerSeo = footerSeo;
	}

	public String getFooterSeo(){
		return footerSeo;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setHtmlName(String htmlName){
		this.htmlName = htmlName;
	}

	public String getHtmlName(){
		return htmlName;
	}

	public void setPosition(String position){
		this.position = position;
	}

	public String getPosition(){
		return position;
	}

	public void setIdParent(String idParent){
		this.idParent = idParent;
	}

	public String getIdParent(){
		return idParent;
	}

	@Override
 	public String toString(){
		return 
			"CrossSellingCategoriesItem{" + 
			"sub_categories = '" + subCategories + '\'' + 
			",products = '" + products + '\'' + 
			",deleted = '" + deleted + '\'' + 
			",picture_url = '" + pictureUrl + '\'' + 
			",id_category = '" + idCategory + '\'' + 
			",name = '" + name + '\'' + 
			",footer_seo = '" + footerSeo + '\'' + 
			",link = '" + link + '\'' + 
			",active = '" + active + '\'' + 
			",html_name = '" + htmlName + '\'' + 
			",position = '" + position + '\'' + 
			",id_parent = '" + idParent + '\'' + 
			"}";
		}
}