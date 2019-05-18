package fr.sushi.app.ui.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> loginLiveData;

    public LoginViewModel() {
        loginLiveData = new MutableLiveData<>();
    }

    public void addAddress(List<ProfileAddressModel> addressModels) {
        String finalJson = GsonHelper.on().convertAddressToJson(addressModels);
        SharedPref.write(PrefKey.USER_ADDRESS, finalJson);
    }

    public void loginAccount(
            String email,
            String password) {
        Repository.loginAccount(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCreateAccountResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));

    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error", throwable.toString());
    }

    private void onCreateAccountResponse(ResponseBody response) {
        loginLiveData.setValue(response);
    }

    public MutableLiveData<ResponseBody> getLoginAccountLiveData() {
        return loginLiveData;
    }
}
