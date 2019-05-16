package fr.sushi.app.ui.menu;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.model.food_menu.ProductsItem;

public class MenuPrefUtil {
    private static final String KEY_MENU_SELECT = "mycart";
    private static String SPLITTER = "-@-";

    public static void saveItem(ProductsItem item, int count){
        for(int i = 0; i < count; i++){
            //saveItem(item);
        }
    }

    public static void saveItem(ProductsItem item) {
        MyCartProduct myCartProduct = buildMyCartProduct(item);

        String existItems = SharedPref.read(KEY_MENU_SELECT, "");

        Gson gson = new Gson();
        String newItemJson = gson.toJson(myCartProduct);

        if (TextUtils.isEmpty(existItems)) {
            SharedPref.write(KEY_MENU_SELECT, newItemJson);
            return;
        }

        int existItemCount = ifExistReturnCount(item, existItems);

        if (existItemCount == 0) {
            existItems = existItems + SPLITTER + newItemJson;
            SharedPref.write(KEY_MENU_SELECT, existItems);
        } else {
            myCartProduct.setItemCount(existItemCount + 1);
            String oldExistItems = removeExistingProductsIfExist(item, existItems);

            String currentItemJson = gson.toJson(myCartProduct);

            if (TextUtils.isEmpty(oldExistItems)) {
                SharedPref.write(KEY_MENU_SELECT, currentItemJson);
            } else {
                oldExistItems = oldExistItems + SPLITTER + currentItemJson;
                SharedPref.write(KEY_MENU_SELECT, oldExistItems);
            }
        }


    }

    private static int ifExistReturnCount(ProductsItem newItem, String existJson) {
        if (TextUtils.isEmpty(existJson)) return 0;
        Gson gson = new Gson();

        String[] existArray = existJson.split(SPLITTER);

        for (String item : existArray) {
            MyCartProduct product = gson.fromJson(item, MyCartProduct.class);
            if (product.getProductId().equals(newItem.getIdProduct())) {
                return product.getItemCount();
            }
        }
        return 0;
    }

    private static String removeExistingProductsIfExist(ProductsItem newProducts, String existJson) {
        String[] existArray = existJson.split(SPLITTER);
        Gson gson = new Gson();
        List<String> uniqueItem = new ArrayList<>();
        for (String item : existArray) {
            MyCartProduct product = gson.fromJson(item, MyCartProduct.class);
            if (product.getProductId().equals(newProducts.getIdProduct())) {
                continue;
            }
            uniqueItem.add(item);
        }

        if (uniqueItem.isEmpty()) {
            return "";
        }

        String uniqueItemsString = uniqueItem.get(0);

        for (int i = 1; i < uniqueItem.size(); i++) {
            uniqueItemsString = uniqueItemsString + SPLITTER + uniqueItem.get(i);
        }
        return uniqueItemsString;
    }

    public static void removeItem(ProductsItem item) {
        Gson gson = new Gson();
        String existIteString = SharedPref.read(KEY_MENU_SELECT, "");

        if (!TextUtils.isEmpty(existIteString)) {
            String[] itemArray = existIteString.split(SPLITTER);
            List<String> itemList = new ArrayList<>();
            for (String pItem : itemArray) {
                MyCartProduct existItem = gson.fromJson(pItem, MyCartProduct.class);
                if (!item.getIdProduct().equals(existItem.getProductId())) {
                    itemList.add(pItem);
                }
            }

            if (!itemList.isEmpty()) {
                String saveAgain = itemList.get(0);

                if (itemList.size() > 1) {
                    for (int i = 1; i < itemList.size(); i++) {
                        saveAgain = saveAgain + SPLITTER + itemList.get(i);
                    }
                }
                SharedPref.write(KEY_MENU_SELECT, saveAgain);
            } else {
                SharedPref.write(KEY_MENU_SELECT, "");
            }
        }
    }


    public static void removeItem(MyCartProduct item) {
        Gson gson = new Gson();
        String existIteString = SharedPref.read(KEY_MENU_SELECT, "");

        if (!TextUtils.isEmpty(existIteString)) {
            String[] itemArray = existIteString.split(SPLITTER);
            List<String> itemList = new ArrayList<>();
            for (String pItem : itemArray) {
                MyCartProduct existItem = gson.fromJson(pItem, MyCartProduct.class);
                if (!item.getProductId().equals(existItem.getProductId())) {
                    itemList.add(pItem);
                }
            }

            if (!itemList.isEmpty()) {
                String saveAgain = itemList.get(0);

                if (itemList.size() > 1) {
                    for (int i = 1; i < itemList.size(); i++) {
                        saveAgain = saveAgain + SPLITTER + itemList.get(i);
                    }
                }
                SharedPref.write(KEY_MENU_SELECT, saveAgain);
            } else {
                SharedPref.write(KEY_MENU_SELECT, "");
            }
        }
    }

    public static void removeSingle(ProductsItem item) {
        String existItems = SharedPref.read(KEY_MENU_SELECT, "");
        int existItemCount = ifExistReturnCount(item, existItems);
        MyCartProduct myCartProduct = buildMyCartProduct(item);
        myCartProduct.setItemCount(existItemCount - 1);
        String oldExistItems = removeExistingProductsIfExist(item, existItems);
        Gson gson = new Gson();
        String currentItemJson = gson.toJson(myCartProduct);
        if (TextUtils.isEmpty(oldExistItems)) {
            SharedPref.write(KEY_MENU_SELECT, currentItemJson);
        } else {
            oldExistItems = oldExistItems + SPLITTER + currentItemJson;
            SharedPref.write(KEY_MENU_SELECT, oldExistItems);
        }
    }

    public static List<MyCartProduct> getSaveItems() {
        Gson gson = new Gson();
        String existIteString = SharedPref.read(KEY_MENU_SELECT, "");
        List<MyCartProduct> itemList = new ArrayList<>();
        if (!TextUtils.isEmpty(existIteString)) {
            String[] itemArray = existIteString.split(SPLITTER);
            for (String pItem : itemArray) {
                MyCartProduct existItem = gson.fromJson(pItem, MyCartProduct.class);
                itemList.add(existItem);
            }

        }
        return itemList;
    }

    private static MyCartProduct buildMyCartProduct(ProductsItem item) {
        MyCartProduct myCartProduct = new MyCartProduct();
        myCartProduct.setProductId(item.getIdProduct());
        myCartProduct.setName(item.getName());
        myCartProduct.setCoderUrl(item.getCoverUrl());
        myCartProduct.setShortDescription(item.getDescriptionShort());
        myCartProduct.setCategoryId(item.getIdCategory());
        myCartProduct.setCategoryName(item.getCategoryName());
        myCartProduct.setPiece(item.getNbrePiece());
        myCartProduct.setPriceHt(item.getPriceHt());
        myCartProduct.setPriceTtc(item.getPriceTtc());
        myCartProduct.setItemCount(1);
        myCartProduct.setSellingProductInfo(item.getDescriptionSecondary());
        return myCartProduct;
    }

}
