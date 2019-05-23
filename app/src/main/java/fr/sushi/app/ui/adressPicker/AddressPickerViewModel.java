package fr.sushi.app.ui.adressPicker;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class AddressPickerViewModel extends ViewModel {
    private MutableLiveData<RestuarentsResponse> restuarentListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> takeWayAddressLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> deliveryAddressLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> storeProductLiveData = new MutableLiveData<>();

    public void getShopList() {
        Repository.getAppShops().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessShopRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }


    public void setTakeawayStore(String storeId) {
        Repository.setTakeawayStore(storeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessSetStoreId,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onSuccessSetStoreId(ResponseBody responseBody) {
        if (responseBody != null) {
            takeWayAddressLiveData.setValue(responseBody);
        }
    }

    public void setDeliveryAddress(String address,
                                   String postcode, String city) {
        Repository.setDeliveryAddress(address, postcode, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccesssetDeliveryAddress,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    public void getStoreProducts(String storeId) {
        Repository.getStoreProducts(storeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessGetStoreProcduct,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onSuccesssetDeliveryAddress(ResponseBody responseBody) {
        if (responseBody != null) {
            deliveryAddressLiveData.setValue(responseBody);
        }
    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error", throwable.toString());
    }


    private void onSuccessShopRequest(RestuarentsResponse restuarentsResponse) {
        if (restuarentsResponse == null) return;
        restuarentListMutableLiveData.setValue(restuarentsResponse);
    }

    public MutableLiveData<RestuarentsResponse> getRestuarentListMutableLiveData() {
        return restuarentListMutableLiveData;
    }

    public MutableLiveData<ResponseBody> getDeliveryAddressLiveData() {
        return deliveryAddressLiveData;
    }

    public MutableLiveData<ResponseBody> getTakeWayAddressLiveData() {
        return takeWayAddressLiveData;
    }

    private void onSuccessGetStoreProcduct(ResponseBody responseBody) {
        if (responseBody != null) {
            storeProductLiveData.setValue(responseBody);
        }
    }

    public MutableLiveData<ResponseBody> getStoreProductLiveData() {
        return storeProductLiveData;
    }
}
