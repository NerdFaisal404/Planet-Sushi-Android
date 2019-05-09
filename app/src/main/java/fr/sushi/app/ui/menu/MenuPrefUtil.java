package fr.sushi.app.ui.menu;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.model.food_menu.ProductsItem;

public class MenuPrefUtil {
    private static final String KEY_MENU_SELECT = "selectMenu";

    public static void saveItem(ProductsItem item){
        Gson gson = new Gson();
        String jsonString = gson.toJson(item);

        String existItem = SharedPref.read(KEY_MENU_SELECT, "");

        if(TextUtils.isEmpty(existItem)){
            SharedPref.write(KEY_MENU_SELECT, jsonString);
        }else {
            existItem = existItem+"@@"+jsonString;
            SharedPref.write(KEY_MENU_SELECT, existItem);
        }

    }

    public static void removeItem(ProductsItem item){
        Gson gson = new Gson();
        String existIteString = SharedPref.read(KEY_MENU_SELECT,"");

        if(!TextUtils.isEmpty(existIteString)){
            String[] itemArray = existIteString.split("@@");
            List<String> itemList = new ArrayList<>();
            for(String pItem : itemArray){
                ProductsItem existItem = gson.fromJson(pItem,  ProductsItem.class);
                if(!item.getIdProduct().equals(existItem.getIdProduct())){
                    itemList.add(pItem);
                }
            }

            if(!itemList.isEmpty()){
                String saveAgain = itemList.get(0);

                if(itemList.size() > 1){
                    for(int i = 1; i < itemList.size(); i++){
                        saveAgain = saveAgain+"@@"+itemList.get(i);
                    }
                }
                SharedPref.write(KEY_MENU_SELECT, saveAgain);
            }else {
                SharedPref.write(KEY_MENU_SELECT, "");
            }
        }
    }

    public static List<ProductsItem> getSaveItems(){
        Gson gson = new Gson();
        String existIteString = SharedPref.read(KEY_MENU_SELECT,"");
        List<ProductsItem> itemList = new ArrayList<>();
        if(!TextUtils.isEmpty(existIteString)){
            String[] itemArray = existIteString.split("@@");
            for(String pItem : itemArray){
                ProductsItem existItem = gson.fromJson(pItem,  ProductsItem.class);
                itemList.add(existItem);
            }

        }
        return itemList;
    }

}
