package fr.sushi.app.data.db;

import java.util.List;

import fr.sushi.app.SushiApp;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.menu.MyCartProduct_;
import io.objectbox.Box;

public class DBManager {
    private static DBManager dbManager;
    private Box<MyCartProduct> productBox;

    private DBManager() {
        productBox = SushiApp.getBoxStore().boxFor(MyCartProduct.class);
    }

    public static DBManager on(){
      if(dbManager == null){
          dbManager = new DBManager();
      }

      return dbManager;
    }

    public void saveProductItem(ProductsItem item , int itemCount){
        for(int i = 0; i < itemCount; i++){
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

    public void removeProduct(ProductsItem item){
        MyCartProduct existItem = productBox.query()
                .equal(MyCartProduct_.productId, item.getIdProduct())
                .build()
                .findFirst();
        if(existItem != null){
            productBox.remove(existItem);
        }
    }

    public List<MyCartProduct> getAllProducts(){
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


}
