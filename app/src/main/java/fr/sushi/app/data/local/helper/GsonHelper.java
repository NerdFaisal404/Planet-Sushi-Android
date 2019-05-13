package fr.sushi.app.data.local.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.data.model.ProfileAddressModel;
import io.reactivex.Flowable;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/10/2019 at 11:13 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/10/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class GsonHelper {

    public static GsonHelper on() {
        return new GsonHelper();
    }

    public String convertAddressToJson(List<ProfileAddressModel> addressList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProfileAddressModel>>() {
        }.getType();
        return gson.toJson(addressList, type);
    }

    public Flowable<List<ProfileAddressModel>> convertJsonToAddress(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProfileAddressModel>>() {
        }.getType();
        List<ProfileAddressModel> addressList = gson.fromJson(json, type);
        if (addressList == null) {
            addressList = new ArrayList<>();
        }

        return Flowable.just(addressList);
    }

    public List<ProfileAddressModel> convertJsonToNormalAddress(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProfileAddressModel>>() {
        }.getType();
        List<ProfileAddressModel> addressList = gson.fromJson(json, type);
        if (addressList == null) {
            addressList = new ArrayList<>();
        }

        return addressList;
    }
}
