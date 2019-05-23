package fr.sushi.app.data.remote.network;

/*
 * *
 *  * Created by Faisal Ahmed on 10/14/18 12:41 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 10/14/18 12:41 PM
 *
 */


import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.ui.checkout.model.PaymentModel;
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
        jsonObject.addProperty("firstname", firstName);
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

    public static Flowable<ResponseBody> updateCustomerProfile(String firstName,
                                                               String lastName,
                                                               String phone,
                                                               String birthDate,
                                                               String email) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);

        JsonObject mainObj = new JsonObject();
        String id = SharedPref.read(PrefKey.USER_ID, "");
        String token = SharedPref.read(PrefKey.USER_TOKEN, "");
        mainObj.addProperty("token", token);
        mainObj.addProperty("id_customer", id);
        mainObj.addProperty("email", email);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstname", firstName);
        jsonObject.addProperty("lastname", lastName);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("birthday", birthDate);

        //array.add(jsonObject);

        mainObj.add("Customer", jsonObject);

        Log.d("UpdateProfileResponse", "res: " + mainObj.toString());

        return apiInterface.updateCustomerProfile(mainObj);
    }

    public static Flowable<ResponseBody> addOrUpdateAddress(ProfileAddressModel model) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);

        JsonObject mainObj = new JsonObject();
        String id = SharedPref.read(PrefKey.USER_ID, "");
        String token = SharedPref.read(PrefKey.USER_TOKEN, "");
        String email = SharedPref.read(PrefKey.USER_EMAIL, "");
        mainObj.addProperty("token", token);
        mainObj.addProperty("id_customer", id);
        mainObj.addProperty("email", email);


        JsonObject addressObj = new JsonObject();
        addressObj.addProperty("id_address", model.getId());
        addressObj.addProperty("company", model.getCompany());
        addressObj.addProperty("departement", model.getAddressType()); //pending
        addressObj.addProperty("address1", model.getLocation());
        addressObj.addProperty("postcode", model.getZipCode());
        addressObj.addProperty("city", model.getCity());
        addressObj.addProperty("address2", model.getInformation());
        addressObj.addProperty("building", model.getBuilding());
        addressObj.addProperty("digicode", model.getAccessCode());
        addressObj.addProperty("interphone", model.getInterphone());
        // addressObj.addProperty("stair", model.getId()); //pending
        addressObj.addProperty("floor", model.getFloor());
        addressObj.addProperty("door", model.getAppartment());
        // addressObj.addProperty("phone", model.getAppartment());//pending

        //array.add(jsonObject);

        mainObj.add("Address", addressObj);

        Log.d("AddressUpdateResponse", "res: " + mainObj.toString());

        return apiInterface.addOrUpdateAddress(mainObj);
    }

    public static Flowable<ResponseBody> sendPayment(JsonObject jsonObject) {
        ApiCall apiInterface = RetrofitClient.getInstance().create(ApiCall.class);

        return apiInterface.saveOrderPayment(jsonObject);
    }

    public static Flowable<ResponseBody> sendAdyenPayment(PaymentModel paymentModel) {
        ApiCall apiInterface = RetrofitClient.getAdyenRetrofitInstance().create(ApiCall.class);

        return apiInterface.sendAdyenPayment(paymentModel);
    }

    public static Flowable<ResponseBody> getStoreProducts(String storeId) {
        ApiCall apiInterface = RetrofitClient.getAdyenRetrofitInstance().create(ApiCall.class);
        JsonObject object = new JsonObject();
        object.addProperty("id_store", storeId);
        return apiInterface.getStoreProducts(object);
    }
}
