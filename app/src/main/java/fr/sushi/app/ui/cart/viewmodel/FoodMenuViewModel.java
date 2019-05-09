package fr.sushi.app.ui.cart.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodMenuViewModel extends ViewModel {

    private MutableLiveData<FoodMenuResponse> foodMenuListMutableLiveData = new MutableLiveData<>();


    public void getFoodMenu() {
        Repository.getFoodMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessMenuRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onError(Throwable throwable, ApiResponseError errorType) {

    }


    private void onSuccessMenuRequest(FoodMenuResponse restuarentsResponse) {

        if (restuarentsResponse == null) return;

        foodMenuListMutableLiveData.setValue(restuarentsResponse);


    }

    public MutableLiveData<FoodMenuResponse> getFoodMenuListMutableLiveData() {
        return foodMenuListMutableLiveData;
    }
}