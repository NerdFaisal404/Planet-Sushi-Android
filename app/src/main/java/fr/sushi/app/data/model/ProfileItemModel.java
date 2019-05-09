package fr.sushi.app.data.model;

import android.graphics.drawable.Drawable;

public class ProfileItemModel {

    private String itemName;
    private int icon;

    public ProfileItemModel(String itemName, int icon) {
        this.itemName = itemName;
        this.icon = icon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
