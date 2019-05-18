package fr.sushi.app.ui.editprofile;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
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
import java.util.Calendar;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.ActivityEditProfileBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.Utils;
import okhttp3.ResponseBody;

public class EditProfileActivity extends BaseActivity {
    private ActivityEditProfileBinding mBinding;
    private EditProfileViewModel mViewModel;
    private boolean isValidFirstName, isValidLastName, isValidPhone;

    private String firstName, lastName, phone, email, birthday;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void startUI() {
        mBinding = (ActivityEditProfileBinding) getViewDataBinding();
        setClickListener(mBinding.buttonSave, mBinding.imageViewBack, mBinding.editTextBirthday);
        mBinding.editTextFirstName.addTextChangedListener(new MyTextWatcher(mBinding.editTextFirstName));
        mBinding.editTextLastName.addTextChangedListener(new MyTextWatcher(mBinding.editTextLastName));
        mBinding.editTextPhone.addTextChangedListener(new MyTextWatcher(mBinding.editTextPhone));
        initView();
        initViewModel();

        mViewModel.getupdateAccountLiveData().observe(this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(@Nullable ResponseBody responseBody) {
                DialogUtils.hideDialog();
                if (responseBody != null) {
                    try {
                        String response = responseBody.string();
                        Log.w("UpdateProfileResponse", "response: " + response);

                        JSONObject responseObject = new JSONObject(response);
                        boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                        if (error) {
                            ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                            Utils.showAlert(EditProfileActivity.this, "Erreur!", errorResponse.getErrorString());
                        } else {

                            // we have to save data in preference
                            SharedPref.write(PrefKey.USER_FIRST_NAME, firstName);
                            SharedPref.write(PrefKey.USER_LAST_NAME, lastName);
                            SharedPref.write(PrefKey.USER_EMAIL, email);
                            SharedPref.write(PrefKey.USER_PHONE, phone);
                            SharedPref.write(PrefKey.USER_BIRTHDAY, birthday);

                            finish();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_save:
                // do save stuff
                firstName = mBinding.editTextFirstName.getText().toString();
                lastName = mBinding.editTextLastName.getText().toString();
                phone = mBinding.editTextPhone.getText().toString();
                birthday = mBinding.editTextBirthday.getText().toString();
                email = mBinding.editTextEmail.getText().toString();
                DialogUtils.showDialog(this);
                mViewModel.updateCustomerProfile(firstName, lastName, phone, birthday, email);
                break;
            case R.id.image_view_back:
                Utils.hideSoftKeyboard(this);
                finish();
                break;
            case R.id.edit_text_birthday:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (view1, year, monthOfYear, dayOfMonth) -> mBinding.editTextBirthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }


    private void initView() {
        String firstName = SharedPref.read(PrefKey.USER_FIRST_NAME, "");
        String lastName = SharedPref.read(PrefKey.USER_LAST_NAME, "");
        String email = SharedPref.read(PrefKey.USER_EMAIL, "");
        String phone = SharedPref.read(PrefKey.USER_PHONE, "");
        String birthday;

        mBinding.editTextEmail.setText(email);
        mBinding.editTextFirstName.setText(firstName);
        mBinding.editTextLastName.setText(lastName);
        mBinding.editTextPhone.setText(phone);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
    }

    class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.edit_text_first_name:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidFirstName = false;
                    } else {
                        isValidFirstName = true;
                    }
                    break;
                case R.id.edit_text_last_name:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidLastName = false;
                    } else {
                        isValidLastName = true;
                    }
                    break;
                case R.id.edit_text_phone:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidPhone = false;
                    } else {
                        isValidPhone = true;
                    }
                    break;
            }

            if (isValidLastName && isValidFirstName && isValidPhone) {
                mBinding.buttonSave.setEnabled(true);
            } else {
                mBinding.buttonSave.setEnabled(false);
            }
        }
    }

}
