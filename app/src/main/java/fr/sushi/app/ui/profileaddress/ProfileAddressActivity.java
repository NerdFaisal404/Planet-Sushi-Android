package fr.sushi.app.ui.profileaddress;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.databinding.ActivityProfileAddressBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.base.ItemClickListener;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.profileaddress.adapter.ProfileAddressAdapter;
import fr.sushi.app.ui.profileaddress.viewmodel.ProfileAddressViewModel;

public class ProfileAddressActivity extends BaseActivity implements ItemClickListener<ProfileAddressModel> {
    private ActivityProfileAddressBinding mBinding;
    private ProfileAddressAdapter mAdapter;
    private ProfileAddressViewModel mAddressViewModel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_address;
    }


    @Override
    protected void startUI() {
        mBinding = (ActivityProfileAddressBinding) getViewDataBinding();

        initView();

        initViewModel();

        mAddressViewModel.getAddressList().observe(this, addressList -> {
            mAdapter.clear();
            mAdapter.addItems(addressList);
            ProfileAddressModel addressModel = addressList.get(addressList.size() - 1);
            SearchPlace searchPlace = new SearchPlace(addressModel.getZipCode(),addressModel.getCity(),
                    addressModel.getLocation());

            PlaceUtil.saveCurrentPlaceInFirstPosition(searchPlace);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAddressViewModel.parseUserProfileAddress();
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onItemClick(View view, ProfileAddressModel item) {
        // go to edit page or AddressAddActivity
        Intent intent = new Intent(ProfileAddressActivity.this, AddressAddActivity.class);
        intent.putExtra(IntentKey.ADDRESS_MODEL, item);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_add:
                Intent intent = new Intent(ProfileAddressActivity.this, LocationChoiceActivity.class);
                intent.putExtra(IntentKey.IS_FROM_ADD_REQUEST, true);
                startActivity(intent);
                break;
            case R.id.image_view_back:
                finish();
                break;
        }
    }

    private void initView() {
        mBinding.recyclerViewAddress.setHasFixedSize(true);
        mBinding.recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProfileAddressAdapter();
        mAdapter.setItemClickListener(this);
        mBinding.recyclerViewAddress.setAdapter(mAdapter);

        setClickListener(mBinding.buttonAdd, mBinding.imageViewBack);
    }

    private void initViewModel() {
        mAddressViewModel = ViewModelProviders.of(this).get(ProfileAddressViewModel.class);
    }


}
