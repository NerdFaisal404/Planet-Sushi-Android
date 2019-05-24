package fr.sushi.app.ui.checkout;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.food_menu.FoodMenuResponse;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CheckoutViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> paymentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> paymentOrderMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> addressLiveData = new MutableLiveData<>();


    public void sendSavePaymentOrder(JsonObject paymentModel) {
        Repository.sendPayment(paymentModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessPaymentOrderRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    public void sendAdyenPayment(PaymentModel paymentModel) {
        Repository.sendAdyenPayment(paymentModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessPaymentRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }


    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_error", throwable.toString());
    }


    private void onSuccessPaymentRequest(ResponseBody restuarentsResponse) {

        if (restuarentsResponse == null) return;

        paymentMutableLiveData.setValue(restuarentsResponse);


    }

    public MutableLiveData<ResponseBody> getPaymentMutableLiveData() {
        return paymentMutableLiveData;
    }

    private void onSuccessPaymentOrderRequest(ResponseBody restuarentsResponse) {

        if (restuarentsResponse == null) return;

        paymentOrderMutableLiveData.setValue(restuarentsResponse);


    }

    public MutableLiveData<ResponseBody> getPaymentOrderMutableLiveData() {
        return paymentOrderMutableLiveData;
    }

    public void updateAddress(ProfileAddressModel model) {
        String json = SharedPref.read(PrefKey.USER_ADDRESS, "");
        List<ProfileAddressModel> itemList = GsonHelper.on().convertJsonToNormalAddress(json);

        for (ProfileAddressModel item : itemList) {
            if (item.getId().equals(model.getId())) {
                //TODO update data of item
                break;
            }
        }
        String finalJson = GsonHelper.on().convertAddressToJson(itemList);
        SharedPref.write(PrefKey.USER_ADDRESS, finalJson);

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