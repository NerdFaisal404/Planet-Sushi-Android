package fr.sushi.app.data.remote.network;

/*
 * *
 *  * Created by Faisal Ahmed on 10/14/18 12:41 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 10/14/18 12:41 PM
 *
 */


import com.google.gson.JsonObject;

import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.ui.home.data.HomeConfigurationData;
import io.reactivex.Flowable;
import okhttp3.ResponseBody;

public class Repository {


    public static Flowable<RestuarentsResponse> getAppShops() {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        return apiInterface.getAppShops();
    }

    public static Flowable<ResponseBody> setTakeawayStore(String storeId) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id_store", storeId);
        return apiInterface.setTakeawayStore(jsonObject);
    }

    public static Flowable<ResponseBody> setDeliveryAddress(String address,
                                                            String postcode, String city) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("postcode", postcode);
        jsonObject.addProperty("city", city);
        return apiInterface.setDeliveryAddress(jsonObject);
    }


    public static Flowable<FoodMenuResponse> getFoodMenu() {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        return apiInterface.getFoodMenu();
    }

    public static Flowable<HomeConfigurationData> getHomeConfigData() {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        return apiInterface.getHomeConfigData();
    }

    public static Flowable<ResponseBody> createAccount(String firstName,
                                                 String lastName,
                                                 String phone,
                                                 String birthDate,
                                                 String email,
                                                 String password,
                                                 String confirmPassword) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);

        /* [0] => firstname
                [1] => lastname
                [2] => phone
                [3] => birthday (YYYY-mm-dd)
                [4] => email
                [5] => password
                [6] => password_confirmation*/

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstname",firstName );
        jsonObject.addProperty("lastname", lastName);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("birthday", birthDate);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("password_confirmation", confirmPassword);
        return apiInterface.createAccount(jsonObject);
    }

    public static Flowable<ResponseBody> loginAccount(String email,
                                                            String password) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        return apiInterface.login(jsonObject);
    }

    public static Flowable<ResponseBody> getCheckoutSideProducts() {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);
        return apiInterface.getCheckoutSideProducts();
    }
}
