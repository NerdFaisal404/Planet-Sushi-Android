package fr.sushi.app.ui.forgotpassword;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ForgotPasswordViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> forgetPasswordLiveData;

    public ForgotPasswordViewModel() {
        forgetPasswordLiveData = new MutableLiveData<>();
    }

    public void forgetPassword(String email) {
        Repository.forgetPassword(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onForgetPasswordResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.e("ForgetPasswordResponse", throwable.toString());
    }

    private void onForgetPasswordResponse(ResponseBody response) {
        forgetPasswordLiveData.setValue(response);
    }

    public MutableLiveData<ResponseBody> getForgetPasswordLiveData() {
        return forgetPasswordLiveData;

    }
}
