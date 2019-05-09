package fr.sushi.app.ui.shop.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends ViewModel {

    private MutableLiveData<RestuarentsResponse> restuarentListMutableLiveData = new MutableLiveData<>();


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

    public MutableLiveData<RestuarentsResponse> getRestuarentListMutableLiveData() {
        return restuarentListMutableLiveData;
    }
}
