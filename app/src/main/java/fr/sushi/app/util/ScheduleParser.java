package fr.sushi.app.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;

public class ScheduleParser {

    public static AddressResponse parseSchedule(JSONObject jo, AddressResponse addressResponse) {
        try {
            if (jo.has("response")) {
                JSONObject response = jo.getJSONObject("response");

                if(response.has("Schedules")){

                    JSONObject schedule = response.getJSONObject("Schedules");

                    Iterator<String> keys = schedule.keys();
                    Gson gson = new Gson();
                    List<Order> orderList = new ArrayList<>();
                    while (keys.hasNext()){
                        String key = keys.next();

                        JSONObject category=schedule.optJSONObject(key);
                        if(category != null) continue;

                        JSONArray jsonArray = schedule.getJSONArray(key);
                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Order order = gson.fromJson(object.toString(), Order.class);
                            if(order != null) {
                                orderList.add(order);
                            }
                        }

                    }
                    if(addressResponse == null){
                        addressResponse = new AddressResponse();
                    }
                    addressResponse.getResponse().getSchedules().setOrderList(orderList);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addressResponse;
    }
}
