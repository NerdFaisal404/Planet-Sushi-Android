package fr.sushi.app.data.db;

import java.util.List;

import fr.sushi.app.SushiApp;
import fr.sushi.app.data.model.food_menu.CrossSellingProductsItem;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.menu.MyCartProduct_;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem_;
import io.objectbox.Box;

public class DBManager {
    private static DBManager dbManager;
    private Box<MyCartProduct> productBox;
    private Box<CrossSellingSelectedItem> crossSellingSelectedItemBox;

    private DBManager() {
        productBox = SushiApp.getBoxStore().boxFor(MyCartProduct.class);
        crossSellingSelectedItemBox = SushiApp.getBoxStore().boxFor(CrossSellingSelectedItem.class);
    }

    public static DBManager on() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }

        return dbManager;
    }

    public void clearMyCartProduct(){
        productBox.removeAll();
        crossSellingSelectedItemBox.removeAll();
    }

    public void saveProductItem(ProductsItem item, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            saveProductItem(item);
        }
    }

    public void saveProductItem(ProductsItem productsItem) {
        MyCartProduct myCartProduct = buildMyCartProduct(productsItem);

        MyCartProduct existItem = productBox.query()
                .equal(MyCartProduct_.productId, productsItem.getIdProduct())
                .build()
                .findFirst();
        if (existItem == null) {
            productBox.put(myCartProduct);
        } else {
            existItem.setItemCount(existItem.getItemCount() + 1);
            productBox.put(existItem);
        }

    }

    public void removeProduct(ProductsItem item) {
        productBox.query()
                .equal(MyCartProduct_.productId, item.getIdProduct())
                .build()
                .remove();
    }

    public List<MyCartProduct> getAllProducts() {
        return productBox.getAll();
    }

    public void removeProduct(MyCartProduct item) {
        productBox.remove(item);
    }

    private MyCartProduct buildMyCartProduct(ProductsItem item) {
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

    /*
     * For cross selling selected item
     * */

    public void insertCrossSellingSelectedItem(String mainId, CrossSellingProductsItem item) {
        CrossSellingSelectedItem selectedItem = new CrossSellingSelectedItem();
        selectedItem.setMainProductId(mainId);
        selectedItem.setProductId(item.getIdProduct());
        selectedItem.setFree(item.isFree());
        selectedItem.setProductName(item.getName());
        selectedItem.setProductPrice(item.getPriceHt());

        CrossSellingSelectedItem existItem = crossSellingSelectedItemBox.query()
                .equal(CrossSellingSelectedItem_.mainProductId, mainId)
                .and()
                .equal(CrossSellingSelectedItem_.productId, item.getIdProduct())
                .build()
                .findFirst();

        if (existItem == null) {
            selectedItem.setProductCount(1);
            crossSellingSelectedItemBox.put(selectedItem);
        } else {
            existItem.setProductCount(existItem.getProductCount() + 1);
            crossSellingSelectedItemBox.put(existItem);
        }
    }

    public void deleteSelectedItemById(String mainId) {
        crossSellingSelectedItemBox.query()
                .equal(CrossSellingSelectedItem_.mainProductId,mainId)
                .build()
                .remove();

    }

    public List<CrossSellingSelectedItem> getCrossSellingItemById(String mainId){
        List<CrossSellingSelectedItem> selectedItemList = crossSellingSelectedItemBox.query()
                .equal(CrossSellingSelectedItem_.mainProductId, mainId)
                .build()
                .find();
        return selectedItemList;
    }

    public List<CrossSellingSelectedItem> getAllCrossSellingItems(){
        return crossSellingSelectedItemBox.getAll();
    }

    public List<MyCartProduct> getMyCartProductsWithCrossSellingItems(){
        List<MyCartProduct> myCartProducts = productBox.getAll();

        for(MyCartProduct item : myCartProducts){
            List<CrossSellingSelectedItem> sellingSelectedItemList =  getCrossSellingItemById(item.getProductId());
            item.setCrossSellingSelectedItems(sellingSelectedItemList);
        }
        return myCartProducts;
    }

    public boolean isCardEmpty(){
        List<MyCartProduct> myCartProducts = productBox.getAll();
        if(myCartProducts.isEmpty()) return true;
        return false;
    }
}
