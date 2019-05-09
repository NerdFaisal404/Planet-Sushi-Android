package fr.sushi.app.data.model.address_picker;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Schedules{
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}