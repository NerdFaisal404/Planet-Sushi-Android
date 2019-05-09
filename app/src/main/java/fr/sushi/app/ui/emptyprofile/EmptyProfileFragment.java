package fr.sushi.app.ui.emptyprofile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.Arrays;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.databinding.FragmentEmptyProfileBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.createaccount.CreateAccountActivity;
import fr.sushi.app.ui.login.LoginActivity;
import fr.sushi.app.ui.main.MainActivity;

public class EmptyProfileFragment extends BaseFragment {

    private FragmentEmptyProfileBinding mBinding;

    private EmptyProfileViewModel mViewModel;

    private CallbackManager callbackManager;
    private String[] PERMISSIONS = {"public_profile"};

    public EmptyProfileFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_empty_profile;
    }

    @Override
    protected void startUI() {

        mBinding = (FragmentEmptyProfileBinding) getViewDataBinding();
        initView();

        initViewModel();

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

    }


    @Override
    protected void stopUI() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 500) {
            Log.d("LoginTest", "call fragment");
            ((MainActivity) getActivity()).goProfilePage();
        }
        // check if else condition if any result here
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.button_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PERMISSIONS));
                mViewModel.facebookLogin(callbackManager);
                break;
            case R.id.button_google:
                break;
            case R.id.button_create_account:
                startActivityForResult(new Intent(getActivity(), CreateAccountActivity.class), 500);
                break;
            case R.id.text_view_login:
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 500);
                break;
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(EmptyProfileViewModel.class);
    }

    private void initView() {
        // set click listener
        setClickListener(mBinding.buttonGoogle, mBinding.buttonFacebook,
                mBinding.buttonCreateAccount, mBinding.textViewLogin);

    }
}
