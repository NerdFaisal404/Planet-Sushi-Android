package fr.sushi.app.ui.profileaddress;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.databinding.ActivityProfileAddressBinding;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.base.ItemClickListener;
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

        mAddressViewModel.parseUserProfileAddress();

        mAddressViewModel.getAddressList().observe(this, addressList -> {
            mAdapter.clear();
            mAdapter.addItems(addressList);
        });
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onItemClick(View view, ProfileAddressModel item) {
        // go to edit page or AddressAddActivity
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_add:
                startActivity(new Intent(ProfileAddressActivity.this, LocationChoiceActivity.class));
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
