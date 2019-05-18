package fr.sushi.app.ui.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.ActivityLoginBinding;
import fr.sushi.app.ui.main.MainActivity;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.Utils;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private boolean isEmailvalid, isPasswordValid;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.inputEmail.addTextChangedListener(new MyTextWatcher(binding.inputEmail));
        binding.inputPassword.addTextChangedListener(new MyTextWatcher(binding.inputPassword));
        binding.tvSignup.setOnClickListener(v -> signUp());

        initViewModel();

        initCreateAccountViewMode();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initCreateAccountViewMode() {
        loginViewModel.getLoginAccountLiveData().observe(this, response -> {
            if (response != null) {
                DialogUtils.hideDialog();
                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(this, "Erreur!", errorResponse.getErrorString());
                    } else {
                        // CreateAccountResponse addressResponse = new Gson().fromJson(responseObject.toString(), CreateAccountResponse.class);

                        JSONObject responseObj = responseObject.getJSONObject("response");

                        String token = responseObj.getString("token");

                        JSONObject customerObj = responseObj.getJSONObject("Customer");

                        String firstName = customerObj.getString("firstname");
                        String lastName = customerObj.getString("lastname");
                        String email = customerObj.getString("email");
                        String phone = customerObj.getString("phone");
                        String id = customerObj.getString("id_customer");

                        // JSONObject bDayObj = customerObj.getJSONObject("birthday");
                        //int year = bDayObj.getInt("year");
                        // int month = bDayObj.getInt("month");
                        // int day = bDayObj.getInt("day");
                        if(responseObj.has("CustomerAddresses")){
                            JSONArray addressArray = responseObj.getJSONArray("CustomerAddresses");
                            saveAddress(addressArray);
                        }

                        SharedPref.write(PrefKey.USER_NAME, (firstName + " " + lastName));
                        SharedPref.write(PrefKey.USER_FIRST_NAME, firstName);
                        SharedPref.write(PrefKey.USER_LAST_NAME, lastName);
                        SharedPref.write(PrefKey.USER_EMAIL, email);
                        SharedPref.write(PrefKey.USER_PHONE, phone);
                        SharedPref.write(PrefKey.USER_ID, id);
                        SharedPref.write(PrefKey.USER_TOKEN, token);

                        SharedPref.write(PrefKey.IS_LOGINED, true);

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
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

        DialogUtils.showDialog(this);

        loginViewModel.loginAccount(email, password);
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

    private void initViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    private void saveAddress(JSONArray addressArray) {

        if (addressArray.length() > 0) {
            List<ProfileAddressModel> addressList = new ArrayList<>();
            for (int i = 0; i < addressArray.length(); i++) {
                try {
                    JSONObject object = addressArray.getJSONObject(i);

                    String addressId = object.optString("id_address");
                    String company = object.optString("company");
                    String addressType = object.optString("departement");
                    String location = object.optString("address1");
                    String information = object.optString("address2");
                    String building = object.optString("building");
                    String accessCode = object.optString("digicode");
                    String interphone = object.optString("interphone");
                    String floor = object.optString("floor");
                    String door = object.optString("door");
                    String postcode = object.optString("postcode");
                    String city = object.optString("city");

                    ProfileAddressModel model = new ProfileAddressModel();
                    model.setId(addressId);
                    model.setCompany(company);
                    model.setAddressType(addressType);
                    model.setLocation(location);
                    model.setInformation(information);
                    model.setBuilding(building);
                    model.setAccessCode(accessCode);
                    model.setInterphone(interphone);
                    model.setFloor(floor);
                    model.setAppartment(door);
                    model.setZipCode(postcode);
                    model.setCity(city);

                    addressList.add(model);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!addressList.isEmpty()) {
                loginViewModel.addAddress(addressList);
            }
        }
    }
}
