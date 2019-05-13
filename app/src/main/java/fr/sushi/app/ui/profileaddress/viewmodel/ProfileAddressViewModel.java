package fr.sushi.app.ui.profileaddress.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.remote.network.ApiResponseError;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/10/2019 at 11:10 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/10/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class ProfileAddressViewModel extends ViewModel {

    private MutableLiveData<List<ProfileAddressModel>> addressLiveData = new MutableLiveData<>();

    public void parseUserProfileAddress() {
        String addressJson = SharedPref.read(PrefKey.USER_ADDRESS, "");

        GsonHelper.on().convertJsonToAddress(addressJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessAddressResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error", throwable.toString());
    }

    private void onSuccessAddressResponse(List<ProfileAddressModel> addressList) {
        if (addressList == null) addressList = new ArrayList<>();

        addressLiveData.setValue(addressList);
    }

    public MutableLiveData<List<ProfileAddressModel>> getAddressList() {
        return addressLiveData;
    }
}
