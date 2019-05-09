package fr.sushi.app.data.remote.network;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import fr.sushi.app.util.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit mRetrofit,mRetrofitToken;


    private static String BASE_URL = "https://dev-ws.planetsushi.fr/app/";

    public synchronized static Retrofit getInstance(Context context) {
        if (mRetrofit == null) {
            createRetrofit(context);
        }
        return mRetrofit;
    }




    public synchronized static Retrofit getInstance() {
        return mRetrofit;
    }



    private static void createRetrofit(Context context) {

        OkHttpClient.Builder clientBuilder=new OkHttpClient().newBuilder();

        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            Request.Builder requestBuilder=request.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer e33f3bd035e27ef167b74258e1581329");

            if (Utils.isInternet(context)) {
                requestBuilder
                        .method(request.method(), request.body());
                return chain.proceed(requestBuilder.build());
            } else {
                throw new IOException("No Internet Connectivity");
            }

        });

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Log.d("Message_Retrofit", "message: " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientBuilder.addInterceptor(loggingInterceptor);

        OkHttpClient client= clientBuilder.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
