package fr.sushi.app.ui.createaccount;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
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
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.ActivityCreateAccountBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.createaccount.model.CreateAccountResponse;

public class CreateAccountActivity extends BaseActivity {
    private ActivityCreateAccountBinding binding;
    private CreateAccountViewModel accountViewModel;
    private boolean isEmailvalid, isPasswordValid;
    public static CreateAccountActivity sInstance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_account;
    }

    @Override
    protected void startUI() {
        sInstance = this;
        binding = (ActivityCreateAccountBinding) getViewDataBinding();
        binding.inputEmail.addTextChangedListener(new MyTextWatcher(binding.inputEmail));
        binding.inputPassword.addTextChangedListener(new MyTextWatcher(binding.inputPassword));
        accountViewModel = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        initCreateAccountViewMode();
        initListener();
    }

    private void initCreateAccountViewMode() {
        accountViewModel.getCreateAccountLiveData().observe(this, response -> {
            if (response != null) {

                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                    } else {
                        CreateAccountResponse addressResponse = new Gson().fromJson(responseObject.toString(), CreateAccountResponse.class);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void stopUI() {
        sInstance = null;
    }


    private void initListener() {
        binding.tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAccountCompleteActivity.class);
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();
            intent.putExtra(IntentKey.KEY_USER_NAME, email);
            intent.putExtra(IntentKey.KEY_USER_PASSWORD, password);
            startActivity(intent);
        });
    }

    private void signUp() {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        if (email.isEmpty() || !isValidEmail(email)) {
            binding.inputEmail.setHighlightColor(Color.RED);
            return;
        } else if (password.isEmpty()) {
            binding.inputPassword.setHighlightColor(Color.RED);
            return;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validatePassword() {
        if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            binding.inputPassword.setHintTextColor(Color.GRAY);
            binding.tvSignup.setBackground(getResources().getDrawable(R.drawable.bg_unselected_pink));
            return false;
        } else {
            return true;
        }


    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {

                case R.id.input_email:
                    isEmailvalid = validateEmail();
                    break;
                case R.id.input_password:
                    isPasswordValid = validatePassword();
                    break;
            }

            if (isEmailvalid && isPasswordValid) {
                //binding.tvSignup.setBackground(getResources().getDrawable(R.drawable.bg_selected_pink));
                binding.tvSignup.setEnabled(true);
            } else {
                // binding.tvSignup.setBackground(getResources().getDrawable(R.drawable.bg_unselected_pink));
                binding.tvSignup.setEnabled(false);
            }
        }
    }


    private boolean validateEmail() {
        String email = binding.inputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            // binding.tvSignup.setBackground(getResources().getDrawable(R.drawable.bg_unselected_pink));
            return false;
        } else {

            return true;
        }

    }
}
