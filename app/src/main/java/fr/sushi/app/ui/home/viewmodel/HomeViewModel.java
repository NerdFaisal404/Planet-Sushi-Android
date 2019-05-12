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

public class HomeViewModel extends ViewModel {

    private MutableLiveData<RestuarentsResponse> restuarentListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<HomeConfigurationData> homeConfigurationDataMutableLiveData;
    public HomeViewModel(){
        homeConfigurationDataMutableLiveData = new MutableLiveData<>();
    }
    private MutableLiveData<FoodMenuResponse> foodMenuListMutableLiveData = new MutableLiveData<>();

    public void getShopList() {
        Repository.getAppShops().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessShopRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
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

}
