package fr.sushi.app.ui.editprofile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class EditProfileViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> updateAccountLiveData;

    public EditProfileViewModel() {
        updateAccountLiveData = new MutableLiveData<>();
    }

    public void updateCustomerProfile(String firstName,
                                      String lastName,
                                      String phone,
                                      String birthDate,
                                      String email) {

        Repository.updateCustomerProfile(firstName, lastName, phone, birthDate, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUpdateAccountResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));


    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.e("UpdateProfileResponse", throwable.toString());
    }

    private void onUpdateAccountResponse(ResponseBody response) {
        updateAccountLiveData.setValue(response);
    }

    public MutableLiveData<ResponseBody> getupdateAccountLiveData() {
        return updateAccountLiveData;

    }
}
