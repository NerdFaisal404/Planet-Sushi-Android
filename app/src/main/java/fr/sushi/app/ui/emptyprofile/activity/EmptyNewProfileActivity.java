package fr.sushi.app.ui.emptyprofile.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
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
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.FragmentNewEmptyProfileBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.checkout.PaymentMethodCheckoutActivity;
import fr.sushi.app.ui.createaccount.CreateAccountActivity;
import fr.sushi.app.ui.emptyprofile.EmptyNewProfileFragment;
import fr.sushi.app.ui.emptyprofile.EmptyProfileViewModel;
import fr.sushi.app.ui.emptyprofile.viewmodel.EmptyNewProfileViewModel;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.login.LoginViewModel;
import fr.sushi.app.ui.main.MainActivity;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.Utils;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/21/2019 at 11:01 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/21/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class EmptyNewProfileActivity extends BaseActivity {
    FragmentNewEmptyProfileBinding mBinding;
    LoginViewModel mViewModel;
    boolean isFromCart;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_empty_profile;
    }

    @Override
    protected void startUI() {
        mBinding = (FragmentNewEmptyProfileBinding) getViewDataBinding();
        setClickListener(mBinding.buttonCreateAccount, mBinding.buttonLogin);
        initViewModel();

        parseIntent();
        initLoginAccountViewMode();

        mBinding.ivClose.setOnClickListener(v -> finish());
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 500) {
            if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
                if (isFromCart) {
                    startActivity(new Intent(EmptyNewProfileActivity.this, PaymentMethodCheckoutActivity.class));
                }
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_create_account:
                startActivityForResult(new Intent(this, CreateAccountActivity.class), 500);
                break;
            case R.id.button_login:
                signUp();
                break;
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(IntentKey.KEY_IS_FROM_CART)) {
            isFromCart = intent.getBooleanExtra(IntentKey.KEY_IS_FROM_CART, false);
        }
    }

    private void signUp() {
        String email = mBinding.inputEmail.getText().toString();
        String password = mBinding.inputPassword.getText().toString();

        if (email.isEmpty() || !isValidEmail(email)) {
            mBinding.inputEmail.setHighlightColor(Color.RED);
            return;
        } else if (password.isEmpty()) {
            mBinding.inputPassword.setHighlightColor(Color.RED);
            return;
        }

        DialogUtils.showDialog(this);

        mViewModel.loginAccount(email, password);
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void initLoginAccountViewMode() {
        mViewModel.getLoginAccountLiveData().observe(this, response -> {
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
                        if (responseObj.has("CustomerAddresses")) {
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

                        if (isFromCart) {
                            startActivity(new Intent(EmptyNewProfileActivity.this, PaymentMethodCheckoutActivity.class));
                        }
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
                mViewModel.addAddress(addressList);
                ProfileAddressModel addressModel = addressList.get(addressList.size() - 1);
                SearchPlace searchPlace = new SearchPlace(addressModel.getZipCode(), addressModel.getCity(),
                        addressModel.getLocation());
                searchPlace.setAddressId(addressModel.getId());
                searchPlace.setInterphone(addressModel.getInterphone());
                searchPlace.setFloor(addressModel.getFloor());
                searchPlace.setAccessCode(addressModel.getAccessCode());
                PlaceUtil.saveDefaultSearchPlace(searchPlace);
            }
        }
    }

}
