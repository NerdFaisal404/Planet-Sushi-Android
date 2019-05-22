package fr.sushi.app.util;

public class SideProduct {
    private String productId;
    private String itemCount;

    public SideProduct(String productId, String itemCount) {
        this.productId = productId;
        this.itemCount = itemCount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
