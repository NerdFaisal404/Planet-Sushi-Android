package fr.sushi.app.ui.profileaddress.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/10/2019 at 3:56 PM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/10/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class AddAddressViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> addressLiveData;

    public AddAddressViewModel() {
        addressLiveData = new MutableLiveData<>();
    }

    public void addAddress(ProfileAddressModel model) {
        String json = SharedPref.read(PrefKey.USER_ADDRESS, "");
        List<ProfileAddressModel> itemList = GsonHelper.on().convertJsonToNormalAddress(json);
        itemList.add(model);
        String finalJson = GsonHelper.on().convertAddressToJson(itemList);
        SharedPref.write(PrefKey.USER_ADDRESS, finalJson);
    }

    public void updateAddress(ProfileAddressModel model) {
        String json = SharedPref.read(PrefKey.USER_ADDRESS, "");
        List<ProfileAddressModel> itemList = GsonHelper.on().convertJsonToNormalAddress(json);

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
        Repository.addOrUpdateAddress(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddressResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.e("AddressUpdateResponse", throwable.toString());
    }

    private void onAddressResponse(ResponseBody response) {
        addressLiveData.setValue(response);
    }

    public MutableLiveData<ResponseBody> getAddressLiveData() {
        return addressLiveData;

    }
}
