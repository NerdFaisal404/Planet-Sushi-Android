package fr.sushi.app.ui.checkout.paiement;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PaimentViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> deliveryAddressLiveData =new MutableLiveData<>();

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error",throwable.toString());
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