package fr.sushi.app.ui.menu;

import java.io.Serializable;

public class MyCartProduct implements Serializable {
    private String productId;
    private String name;
    private String coderUrl;
    private String shortDescription;
    private String categoryId;
    private String categoryName;
    private String piece;
    private String priceHt;
    private String priceTtc;
    private int itemCount;
    private String sellingProductInfo;

    public MyCartProduct(){}

    public MyCartProduct(String productId, String name, String coderUrl,
                         String shortDescription, String categoryId,
                         String categoryName, String piece, String priceHt,
                         String priceTtc, int item_count, String sellingProductInfo) {
        this.productId = productId;
        this.name = name;
        this.coderUrl = coderUrl;
        this.shortDescription = shortDescription;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.piece = piece;
        this.priceHt = priceHt;
        this.priceTtc = priceTtc;
        this.itemCount = item_count;
        this.sellingProductInfo = sellingProductInfo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoderUrl() {
        return coderUrl;
    }

    public void setCoderUrl(String coderUrl) {
        this.coderUrl = coderUrl;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getPriceHt() {
        return priceHt;
    }

    public void setPriceHt(String priceHt) {
        this.priceHt = priceHt;
    }

    public String getPriceTtc() {
        return priceTtc;
    }

    public void setPriceTtc(String priceTtc) {
        this.priceTtc = priceTtc;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getSellingProductInfo() {
        return sellingProductInfo;
    }

    public void setSellingProductInfo(String sellingProductInfo) {
        this.sellingProductInfo = sellingProductInfo;
    }
}
/*
class MyCartProduct: Object {

@objc dynamic var product_id = ""
@objc dynamic var name = ""
@objc dynamic var cover_url = ""
@objc dynamic var short_description = ""
@objc dynamic var category_id = ""
@objc dynamic var category_name = ""
@objc dynamic var piece = ""
@objc dynamic var price_ht = ""
@objc dynamic var price_ttc = ""
@objc dynamic var itemCount = 1
@objc dynamic var selling_product_info = ""

        }
*/