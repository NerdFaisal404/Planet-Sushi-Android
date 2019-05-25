package fr.sushi.app.util;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.ui.menu.MyCartProduct;

public class DataCacheUtil {
    private static List<CategoriesItem> categoriesItemList = new ArrayList<>();
    private static List<ProductsItem> productsItemList = new ArrayList<>();
    private static List<SideProduct> sideProductList = new ArrayList<>();

    private static RestuarentsResponse restuarentsResponses;

    public static void addCategoryItemInCache(List<CategoriesItem> items){
        categoriesItemList.clear();
        categoriesItemList.addAll(items);
        getCategoryItemFromCache();
    }

    public static List<CategoriesItem> getCategoryItemFromCache(){
        productsItemList.clear();
        for(CategoriesItem item : categoriesItemList){
            List<ProductsItem> itemList = item.getProducts();
            productsItemList.addAll(itemList);
        }

        return new ArrayList<>(categoriesItemList);
    }

    public static void addSideProducts(List<SideProduct> sideProduct){
        sideProductList.clear();
        sideProductList.addAll(sideProduct);
    }

    public static List<SideProduct> getSideProductList(){
        return sideProductList;
    }

    public static void removeSideProducts(){
        sideProductList.clear();
    }

    public static void setRestuarentsResponses(RestuarentsResponse responses){

        restuarentsResponses = responses;
    }

    public static RestuarentsResponse getRestuarentsResponses(){
        return restuarentsResponses;
    }

    public static ProductsItem getProductItems(MyCartProduct item) {
        for(ProductsItem pItem : productsItemList){
            if(pItem.getIdCategory().equals(item.getCategoryId())
                    && pItem.getIdProduct().equals(item.getProductId())){
                return pItem;
            }
        }
        return null;
    }
}
