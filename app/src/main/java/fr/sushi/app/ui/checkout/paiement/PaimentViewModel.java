package fr.sushi.app.ui.checkout.paiement;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.JsonObject;

import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PaimentViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> deliveryAddressLiveData =new MutableLiveData<>();
    private MutableLiveData<ResponseBody> paymentOrderMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> addressLiveData = new MutableLiveData<>();

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

    public void addCartDiscount(JsonObject paymentModel) {
        Repository.addCartDiscount(paymentModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessPaymentOrderRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onSuccessPaymentOrderRequest(ResponseBody restuarentsResponse) {

        if (restuarentsResponse == null) return;

        paymentOrderMutableLiveData.setValue(restuarentsResponse);


    }

    public MutableLiveData<ResponseBody> getPaymentOrderMutableLiveData() {
        return paymentOrderMutableLiveData;
    }

    public void addOrUpdateAddressInServer(ProfileAddressModel model) {
        Repository.updateAddress(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddressResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }


    private void onAddressResponse(ResponseBody response) {
        addressLiveData.setValue(response);
    }

    public MutableLiveData<ResponseBody> getAddressLiveData() {
        return addressLiveData;

    }
}