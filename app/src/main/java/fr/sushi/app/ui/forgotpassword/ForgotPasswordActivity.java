package fr.sushi.app.ui.forgotpassword;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.sushi.app.R;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.ActivityForgotPasswordBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.editprofile.EditProfileActivity;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.Utils;
import okhttp3.ResponseBody;

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding mBinding;
    private ForgotPasswordViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }


    @Override
    protected void startUI() {

        mBinding = (ActivityForgotPasswordBinding) getViewDataBinding();

        initViewModel();

        emailTextWatcher();

        mBinding.buttonForgotPassword.setOnClickListener(v -> {
            DialogUtils.showDialog(ForgotPasswordActivity.this);
            String email = mBinding.editTextEmail.getText().toString();
            mViewModel.forgetPassword(email);
        });

        mBinding.imageViewBack.setOnClickListener(v -> finish());

        mViewModel.getForgetPasswordLiveData().observe(this, responseBody -> {
            DialogUtils.hideDialog();
            if (responseBody != null) {
                try {
                    String jsonString = responseBody.string();
                    Log.d("ForgetPasswordResponse", "Response: " + jsonString);
                    JSONObject responseObject = new JSONObject(jsonString);
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    if (error) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(ForgotPasswordActivity.this, "Erreur!", errorResponse.getErrorString());
                    } else {
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
    }

    private void emailTextWatcher() {
        mBinding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mBinding.buttonForgotPassword.setEnabled(false);
                } else {
                    mBinding.buttonForgotPassword.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void stopUI() {

    }
}
