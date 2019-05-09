package fr.sushi.app.ui.emptyprofile;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;


public class EmptyProfileViewModel extends ViewModel {


    void facebookLogin(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success

                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                            try {
                                String id = object.getString("id");
                                String name = object.getString("name");

                                Log.d("FacebookTest", "name: " + name);

                                // we got data
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,first_name,last_name,name");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                        // user cancel fb login
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("FacebookTest", "Error: " + exception.getMessage());
                    }
                }
        );
    }

}
