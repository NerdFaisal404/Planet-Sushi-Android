package fr.sushi.app.ui.checkout.commade;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import fr.sushi.app.data.remote.network.ApiResponseError;
import fr.sushi.app.data.remote.network.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CommadeViewModel extends ViewModel {

    public void getCheckoutSideProducts() {
        Repository.getCheckoutSideProducts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessShopRequest,
                        throwable -> onError(throwable, ApiResponseError.ErrorType));
    }


    private void onError(Throwable throwable, ApiResponseError errorType) {
        Log.d("F_Error",throwable.toString());
    }


    private void onSuccessShopRequest(ResponseBody responseBody) {




    }

}
