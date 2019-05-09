package fr.sushi.app.ui.createaccount;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CreateAccountViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> createAccountLiveData;
    public CreateAccountViewModel(){
        createAccountLiveData = new MutableLiveData<>();
    }

    public void createAccount(String firstName,
                              String lastName,
                              String phone,
                              String birthDate,
                              String email,
                              String password,
                              String confirmPassword){
        Repository.createAccount(firstName, lastName,phone, birthDate, email, password, confirmPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCreateAccountResponse,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));

    }

    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error",throwable.toString());
    }

    private void onCreateAccountResponse(ResponseBody response){
        createAccountLiveData.setValue(response);
    }

    public MutableLiveData<ResponseBody> getCreateAccountLiveData(){
        return createAccountLiveData;
    }
}
