package fr.sushi.app.util;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.data.model.food_menu.CategoriesItem;

public class DataCacheUtil {
    private static List<CategoriesItem> categoriesItemList = new ArrayList<>();

    public static void addCategoryItemInCache(List<CategoriesItem> items){
        categoriesItemList.clear();
        categoriesItemList.addAll(items);
    }

    public static List<CategoriesItem> getCategoryItemFromCache(){
        return categoriesItemList;
    }
}
