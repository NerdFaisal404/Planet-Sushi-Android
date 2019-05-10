package fr.sushi.app.ui.createaccount;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.ActivityCreateAccountCompleteBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.main.MainActivity;
import fr.sushi.app.util.DialogUtils;

public class CreateAccountCompleteActivity extends BaseActivity {
    ActivityCreateAccountCompleteBinding binding;

    private String email;
    private String password;

    private CreateAccountViewModel mAccountViewModel;

    private boolean isValidFirstName, isValidLastName, isValidPhone, isValidBirthday;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_account_complete;
    }


    @Override
    protected void startUI() {
        binding = (ActivityCreateAccountCompleteBinding) getViewDataBinding();

        mAccountViewModel = ViewModelProviders.of(this).get(CreateAccountViewModel.class);

        setClickListener(binding.edtDate, binding.tvSignup);

        initTextWatcher();
        parseIntent();

        observeListener();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void observeListener() {
        mAccountViewModel.getCreateAccountLiveData().observe(this, s -> {
            DialogUtils.hideDialog();
            try {
                String json = s.string();
                Log.d("CreateAccountTest", "response: " + json);

                try {
                    JSONObject responseObject = new JSONObject(json);
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Toast.makeText(this, errorResponse.getErrorString(), Toast.LENGTH_SHORT).show();
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

                        //JSONObject bDayObj = customerObj.getJSONObject("birthday");
                        //int year = bDayObj.getInt("year");
                        //int month = bDayObj.getInt("month");
                        //int day = bDayObj.getInt("day");

                        SharedPref.write(PrefKey.USER_NAME, (firstName + " " + lastName));
                        SharedPref.write(PrefKey.USER_EMAIL, email);
                        SharedPref.write(PrefKey.USER_PHONE, phone);
                        SharedPref.write(PrefKey.USER_ID, id);

                        SharedPref.write(PrefKey.IS_LOGINED, true);
                        finish();
                        if (CreateAccountActivity.sInstance != null) {
                            CreateAccountActivity.sInstance.finish();
                        }

                        //MainActivity.goProfilePage();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            case R.id.edt_date:

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (view1, year, monthOfYear, dayOfMonth) -> binding.edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                datePickerDialog.show();

                break;
            case R.id.tvSignup:
                // we have to call api
                String firstName = binding.edtFirstName.getText().toString();
                String lastName = binding.edtLastName.getText().toString();
                String birthdate = binding.edtDate.getText().toString();
                String phone = binding.edtPhoneNumber.getText().toString();
                DialogUtils.showDialog(this);
                mAccountViewModel.createAccount(firstName, lastName, phone, birthdate, email, password, password);

                break;
        }
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(IntentKey.KEY_USER_NAME)) {
            email = intent.getStringExtra(IntentKey.KEY_USER_NAME);
        }

        if (intent.hasExtra(IntentKey.KEY_USER_PASSWORD)) {
            password = intent.getStringExtra(IntentKey.KEY_USER_PASSWORD);
        }
    }

    private void initTextWatcher() {
        binding.edtFirstName.addTextChangedListener(new MyTextWatcher(binding.edtFirstName));
        binding.edtLastName.addTextChangedListener(new MyTextWatcher(binding.edtLastName));
        binding.edtDate.addTextChangedListener(new MyTextWatcher(binding.edtDate));
        binding.edtPhoneNumber.addTextChangedListener(new MyTextWatcher(binding.edtPhoneNumber));
    }

    private class MyTextWatcher implements TextWatcher {

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
                case R.id.edt_first_name:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidFirstName = false;
                    } else {
                        isValidFirstName = true;
                    }
                    break;
                case R.id.edt_last_name:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidLastName = false;
                    } else {
                        isValidLastName = true;
                    }
                    break;
                case R.id.edt_date:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidBirthday = false;
                    } else {
                        isValidBirthday = true;
                    }
                    break;
                case R.id.edtPhoneNumber:
                    if (TextUtils.isEmpty(s.toString())) {
                        isValidPhone = false;
                    } else {
                        isValidPhone = true;
                    }
                    break;
            }

            if (isValidFirstName
                    && isValidLastName
                    && isValidPhone
                    && isValidBirthday) {

                binding.tvSignup.setEnabled(true);
            } else {
                binding.tvSignup.setEnabled(false);
            }
        }
    }
}
