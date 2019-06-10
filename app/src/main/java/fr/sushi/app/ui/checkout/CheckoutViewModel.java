package fr.sushi.app.ui.checkout;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
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
import fr.sushi.app.ui.home.SearchPlace;
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
        SearchPlace searchPlace = new SearchPlace(model.getZipCode(), model.getCity(),
                model.getLocation());
        searchPlace.setAddressId(model.getId());
        searchPlace.setInterphone(model.getInterphone());
        searchPlace.setFloor(model.getFloor());
        searchPlace.setAccessCode(model.getAccessCode());
        // PlaceUtil.saveDefaultSearchPlace(searchPlace);

        for (ProfileAddressModel item : itemList) {
            if (item.getId().equals(model.getId())) {
                item.setAddressType(TextUtils.isEmpty(model.getAddressType()) ? "" : model.getAddressType());
                item.setLocation(TextUtils.isEmpty(model.getLocation()) ? "" : model.getLocation());
                item.setCity(TextUtils.isEmpty(model.getCity()) ? "" : model.getCity());
                item.setZipCode(TextUtils.isEmpty(model.getZipCode()) ? "" : model.getZipCode());
                item.setBuilding(TextUtils.isEmpty(model.getBuilding()) ? "" : model.getBuilding());
                item.setFloor(TextUtils.isEmpty(model.getFloor()) ? "" : model.getFloor());
                item.setAppartment(TextUtils.isEmpty(model.getAppartment()) ? "" : model.getAppartment());
                item.setCompany(TextUtils.isEmpty(model.getCompany()) ? "" : model.getCompany());
                item.setInterphone(TextUtils.isEmpty(model.getInterphone()) ? "" : model.getInterphone());
                item.setAccessCode(TextUtils.isEmpty(model.getAccessCode()) ? "" : model.getAccessCode());
                item.setInformation(TextUtils.isEmpty(model.getInformation()) ? "" : model.getInformation());


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