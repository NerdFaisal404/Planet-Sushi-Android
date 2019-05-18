package fr.sushi.app.ui.profileaddress;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import fr.sushi.app.R;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.databinding.ActivityAddressAddBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.profileaddress.viewmodel.AddAddressViewModel;

public class AddressAddActivity extends BaseActivity {

    private ActivityAddressAddBinding mBinding;
    private AddAddressViewModel mViewModel;

    private String mLocation, mCity, mZipCode;

    boolean isCreateAddress = false;

    private ProfileAddressModel mAddressModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_add;
    }


    @Override
    protected void startUI() {
        mBinding = (ActivityAddressAddBinding) getViewDataBinding();

        setClickListener(mBinding.buttonAdd, mBinding.imageViewBack, mBinding.textViewAddressType, mBinding.edtAddress);

        parseIntent();

        initViewModel();
    }

    @Override
    protected void stopUI() {
        CommonUtility.LOCATION = null;
        CommonUtility.CITY = null;
        CommonUtility.ZIP_CODE = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonUtility.LOCATION != null
                && CommonUtility.CITY != null
                && CommonUtility.ZIP_CODE != null) {
            mLocation = CommonUtility.LOCATION;
            mCity = CommonUtility.CITY;
            mZipCode = CommonUtility.ZIP_CODE;

            String ad = mLocation + " ," + mZipCode + " " + mCity;
            mBinding.edtAddress.setText(ad);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_add:
                // TODO VALIDATION PENDING
                addOrUpdateAddress(isCreateAddress);

                finish();
                break;
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_address_type:
                //openBottomSheet();
                new AddressDialog(this, R.style.BottomSheetDialogStyle, R.layout.dialog_address_type_picker)
                        .show();
                break;
            case R.id.edtAddress:
                Intent intent = new Intent(AddressAddActivity.this, LocationChoiceActivity.class);
                intent.putExtra(IntentKey.IS_FROM_ADD_REQUEST, false);
                startActivity(intent);
                break;
        }
    }

    private void addOrUpdateAddress(boolean isCreateAddress) {
        ProfileAddressModel model;
        if (isCreateAddress) {
            model = new ProfileAddressModel();
            model.setId(UUID.randomUUID().toString());
        } else {
            model = mAddressModel;
        }

        model.setLocation(mLocation);
        model.setCity(mCity);
        model.setZipCode(mZipCode);
        model.setAddressType(mBinding.textViewAddressType.getText().toString());
        model.setBuilding(TextUtils.isEmpty(mBinding.edtBuilding.getText().toString()) ? "" : mBinding.edtBuilding.getText().toString());
        model.setFloor(TextUtils.isEmpty(mBinding.edtFloor.getText().toString()) ? "" : mBinding.edtFloor.getText().toString());
        model.setAppartment(TextUtils.isEmpty(mBinding.edtAppartment.getText().toString()) ? "" : mBinding.edtAppartment.getText().toString());
        model.setCompany(TextUtils.isEmpty(mBinding.editTextCompany.getText().toString()) ? "" : mBinding.editTextCompany.getText().toString());
        model.setAccessCode(TextUtils.isEmpty(mBinding.editTextAccessCode.getText().toString()) ? "" : mBinding.editTextAccessCode.getText().toString());
        model.setInterphone(TextUtils.isEmpty(mBinding.editTextInterphone.getText().toString()) ? "" : mBinding.editTextInterphone.getText().toString());
        model.setInformation(TextUtils.isEmpty(mBinding.editTextInformation.getText().toString()) ? "" : mBinding.editTextInformation.getText().toString());

        if (isCreateAddress) {
            mViewModel.addAddress(model);
        } else {
            mViewModel.updateAddress(model);
        }
    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AddAddressViewModel.class);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(IntentKey.KEY_IS_CREATE_ADDRESS)) {
            isCreateAddress = intent.getBooleanExtra(IntentKey.KEY_IS_CREATE_ADDRESS, false);
        }

        if (isCreateAddress) {
            mLocation = intent.getStringExtra(IntentKey.ADDRESS);
            mCity = intent.getStringExtra(IntentKey.CITY);
            mZipCode = intent.getStringExtra(IntentKey.ZIP_CODE);
            String ad = mLocation + " ," + mZipCode + " " + mCity;
            mBinding.edtAddress.setText(ad);
        }

        if (intent.hasExtra(IntentKey.ADDRESS_MODEL)) {

            mAddressModel = intent.getParcelableExtra(IntentKey.ADDRESS_MODEL);

            mLocation = mAddressModel.getLocation();
            mCity = mAddressModel.getCity();
            mZipCode = mAddressModel.getZipCode();
            String ad = mLocation + " ," + mZipCode + " " + mCity;
            mBinding.edtAddress.setText(ad);

            mBinding.textViewAddressType.setText(mAddressModel.getAddressType());
            mBinding.edtBuilding.setText(mAddressModel.getBuilding());
            mBinding.edtFloor.setText(mAddressModel.getFloor());
            mBinding.edtAppartment.setText(mAddressModel.getAppartment());
            mBinding.editTextCompany.setText(mAddressModel.getCompany());
            mBinding.editTextInterphone.setText(mAddressModel.getInterphone());
            mBinding.editTextAccessCode.setText(mAddressModel.getAccessCode());
            mBinding.editTextInformation.setText(mAddressModel.getInterphone());
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
        private String addressType;

        View view1, view2, view3, view4, view5;
        TextView text1, text2, text3, text4, text5;

        public AddressDialog(@NonNull Context context, int theme, int layout) {
            super(context, theme);
            bottomSheet = getLayoutInflater().inflate(layout, null);

            setContentView(bottomSheet);
            setCanceledOnTouchOutside(true);

            text1 = bottomSheet.findViewById(R.id.text_view_maison);
            text2 = bottomSheet.findViewById(R.id.text_view_cez);
            text3 = bottomSheet.findViewById(R.id.text_view_cher);
            text4 = bottomSheet.findViewById(R.id.text_view_bureau);
            text5 = bottomSheet.findViewById(R.id.text_view_autre);

            view1 = bottomSheet.findViewById(R.id.view_maison);
            view2 = bottomSheet.findViewById(R.id.view_cez);
            view3 = bottomSheet.findViewById(R.id.view_cher);
            view4 = bottomSheet.findViewById(R.id.view_bureau);
            view5 = bottomSheet.findViewById(R.id.view_autre);

            text1.setOnClickListener(this);
            text2.setOnClickListener(this);
            text3.setOnClickListener(this);
            text4.setOnClickListener(this);
            text5.setOnClickListener(this);

            view1.setOnClickListener(this);
            view2.setOnClickListener(this);
            view3.setOnClickListener(this);
            view4.setOnClickListener(this);
            view5.setOnClickListener(this);
        }

        public void DialogShow() {
            show();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.view_maison:
                case R.id.text_view_maison:
                    addressType = text1.getText().toString();
                    break;
                case R.id.view_cher:
                case R.id.text_view_cher:
                    addressType = text3.getText().toString();
                    break;
                case R.id.view_bureau:
                case R.id.text_view_bureau:
                    addressType = text4.getText().toString();
                    break;
                case R.id.view_cez:
                case R.id.text_view_cez:
                    addressType = text2.getText().toString();
                    break;
                case R.id.view_autre:
                case R.id.text_view_autre:
                    addressType = text5.getText().toString();
                    break;
            }
            if (!TextUtils.isEmpty(addressType)) {
                mBinding.textViewAddressType.setText(addressType);
            }
            dismiss();
        }
    }
}
