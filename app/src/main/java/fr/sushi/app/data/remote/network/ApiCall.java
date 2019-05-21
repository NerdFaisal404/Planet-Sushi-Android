package fr.sushi.app.data.remote.network;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import fr.sushi.app.ui.home.data.HomeConfigurationData;
import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiCall {


    @GET("getAppStores")
    Flowable<RestuarentsResponse> getAppShops();

    @GET("getAppDatabase")
    Flowable<FoodMenuResponse> getFoodMenu();

    @GET("getConfiguration")
    Flowable<HomeConfigurationData> getHomeConfigData();

    @POST("createCustomerAccount")
    Flowable<ResponseBody> createAccount(@Query("params") JsonObject url);


    @POST("Authentification")
    Flowable<ResponseBody> login(@Query("params") JsonObject url);

    @POST("setTakeawayStore")
    Flowable<ResponseBody> setTakeawayStore(@Query("params") JsonObject url);

    @POST("setDeliveryAddress")
    Flowable<ResponseBody> setDeliveryAddress(@Query("params") JsonObject url);

    @GET("getCheckoutSideProducts")
    Flowable<ResponseBody> getCheckoutSideProducts();

    @POST("updateCustomer")
    Flowable<ResponseBody> updateCustomerProfile(@Query("params") JsonObject profile);

    @POST("saveCustomerAddress")
    Flowable<ResponseBody> addOrUpdateAddress(@Query("params") JsonObject address);

    @POST("createPaymentSession")
    Flowable<ResponseBody> sendAdyenPayment(@Body PaymentModel paymentModel);
}
