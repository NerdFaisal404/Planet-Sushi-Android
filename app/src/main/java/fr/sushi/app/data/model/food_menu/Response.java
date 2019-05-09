package fr.sushi.app.data.model.food_menu;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("Categories")
	private List<CategoriesItem> categories;

	public void setCategories(List<CategoriesItem> categories){
		this.categories = categories;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"categories = '" + categories + '\'' + 
			"}";
		}
}