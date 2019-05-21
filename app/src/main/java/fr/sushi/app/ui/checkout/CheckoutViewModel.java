package fr.sushi.app.ui.checkout;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CheckoutViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> paymentMutableLiveData = new MutableLiveData<>();


    public void sendPayment(PaymentModel paymentModel) {
        Repository.sendPayment(paymentModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessMenuRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    public void sendAdyenPayment(PaymentModel paymentModel) {
        Repository.sendAdyenPayment(paymentModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessMenuRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }



    private void onError(Throwable throwable, ApiResponseError errorType) {

    }


    private void onSuccessMenuRequest(ResponseBody restuarentsResponse) {

        if (restuarentsResponse == null) return;

        paymentMutableLiveData.setValue(restuarentsResponse);


    }

    public MutableLiveData<ResponseBody> getPaymentMutableLiveData() {
        return paymentMutableLiveData;
    }
}