package fr.sushi.app.ui.editprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.databinding.ActivityEditProfileBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.util.Utils;

public class EditProfileActivity extends BaseActivity {
    private ActivityEditProfileBinding mBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void startUI() {
        mBinding = (ActivityEditProfileBinding) getViewDataBinding();
        setClickListener(mBinding.buttonSave, mBinding.imageViewBack);
        initView();
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
                break;
            case R.id.image_view_back:
                Utils.hideSoftKeyboard(this);
                finish();
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

}
