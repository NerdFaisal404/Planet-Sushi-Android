package fr.sushi.app.ui.profileaddress;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.databinding.ActivityAddressAddBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.profileaddress.viewmodel.AddAddressViewModel;

public class AddressAddActivity extends BaseActivity {

    private ActivityAddressAddBinding mBinding;
    private AddAddressViewModel mViewModel;

    private String mLocation, mCity, mZipCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_add;
    }


    @Override
    protected void startUI() {
        mBinding = (ActivityAddressAddBinding) getViewDataBinding();

        setClickListener(mBinding.buttonAdd, mBinding.imageViewBack, mBinding.textViewAddressType);

        parseIntent();

        initViewModel();
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_add:
                break;
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_address_type:
                openBottomSheet();
                break;
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AddAddressViewModel.class);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        boolean isCreateAddress = false;
        if (intent.hasExtra(IntentKey.KEY_IS_CREATE_ADDRESS)) {
            isCreateAddress = intent.getBooleanExtra(IntentKey.KEY_IS_CREATE_ADDRESS, false);
        }

        if (isCreateAddress) {
            mLocation = intent.getStringExtra(IntentKey.ADDRESS);
            mCity = intent.getStringExtra(IntentKey.CITY);
            mZipCode = intent.getStringExtra(IntentKey.ZIP_CODE);
            String ad = mLocation + " ," + mZipCode + " " + mCity;
            mBinding.textViewAddress.setText(ad);
        }
    }

    private void openBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.dialog_address_type_picker, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    class AddressDialog extends BottomSheetDialog implements View.OnClickListener {
        View bottomSheet;

        public AddressDialog(@NonNull Context context, int theme, int layout) {
            super(context, theme);
            bottomSheet = getLayoutInflater().inflate(layout, null);

            setContentView(bottomSheet);
            setCanceledOnTouchOutside(true);

            View view1,view2,view3,view4,view5;
        }

        public void DialogShow() {
            show();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.view_maison:
                case R.id.text_view_maison:

                    break;
                case R.id.view_cher:
                case R.id.text_view_cher:

                    break;
                case R.id.view_bureau:
                case R.id.text_view_bureau:

                    break;
                case R.id.view_cez:
                case R.id.text_view_cez:

                    break;
                case R.id.view_autre:
                case R.id.text_view_autre:
                    break;
            }

            dismiss();
        }
    }
}
