package fr.sushi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import fr.sushi.app.ui.home.data.HomeConfigurationData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<RestuarentsResponse> restuarentListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<HomeConfigurationData> homeConfigurationDataMutableLiveData;
    public HomeViewModel(){
        homeConfigurationDataMutableLiveData = new MutableLiveData<>();
    }
    private MutableLiveData<FoodMenuResponse> foodMenuListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> deliveryAddressLiveData =new MutableLiveData<>();
    private MutableLiveData<ResponseBody> storeProductLiveData = new MutableLiveData<>();

    public void getShopList() {
        Repository.getAppShops().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessShopRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }


    public void getStoreProducts(String storeId) {
        Repository.getStoreProducts(storeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessGetStoreProcduct,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onSuccessGetStoreProcduct(ResponseBody responseBody) {
        if (responseBody != null) {
            storeProductLiveData.setValue(responseBody);
        }
    }

    public MutableLiveData<ResponseBody> getStoreProductLiveData() {
        return storeProductLiveData;
    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error",throwable.toString());
    }


    private void onSuccessShopRequest(RestuarentsResponse restuarentsResponse) {

        if (restuarentsResponse == null) return;

        restuarentListMutableLiveData.setValue(restuarentsResponse);


    }



    public void getFoodMenu() {
        Repository.getFoodMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessMenuRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }




    private void onSuccessMenuRequest(FoodMenuResponse restuarentsResponse) {

        if (restuarentsResponse == null) return;

        foodMenuListMutableLiveData.setValue(restuarentsResponse);


    }

    public MutableLiveData<FoodMenuResponse> getFoodMenuListMutableLiveData() {
        return foodMenuListMutableLiveData;
    }

    public MutableLiveData<RestuarentsResponse> getRestuarentListMutableLiveData() {
        return restuarentListMutableLiveData;
    }

    public void getHomeConfigData(){
        Repository.getHomeConfigData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessHomeconfigData,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onSuccessHomeconfigData(HomeConfigurationData homeConfigurationData){
        if(homeConfigurationData != null){
            homeConfigurationDataMutableLiveData.setValue(homeConfigurationData);
        }
    }

    public MutableLiveData<HomeConfigurationData> getHomeConfigLiveData(){
        return homeConfigurationDataMutableLiveData;
    }

    public MutableLiveData<ResponseBody> getDeliveryAddressLiveData(){
        return deliveryAddressLiveData;
    }

    public void setDeliveryAddress(String address,
                                   String postcode, String city) {
        Repository.setDeliveryAddress(address, postcode, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccesssetDeliveryAddress,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onSuccesssetDeliveryAddress(ResponseBody responseBody) {
        if(responseBody != null){
            deliveryAddressLiveData.setValue(responseBody);
        }
    }
}
