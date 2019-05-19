package fr.sushi.app.ui.cart.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.databinding.ItemUserAddressBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;

public class AddressAdapter extends BaseAdapter<ProfileAddressModel> {
    @Override
    public boolean isEqual(ProfileAddressModel left, ProfileAddressModel right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemUserAddressBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_user_address, parent, false);
        return new AddressVH(binding);
    }

    class AddressVH extends BaseViewHolder<ProfileAddressModel> {
        private ItemUserAddressBinding mBinding;

        public AddressVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            mBinding = (ItemUserAddressBinding) viewDataBinding;
        }

        @Override
        public void bind(ProfileAddressModel item) {
            setClickListener(mBinding.getRoot());

            String ad1 = item.getZipCode() + " " + item.getCity();
            mBinding.recentAddrTv.setText(ad1);
            mBinding.tvAddresTwo.setText(item.getLocation());
            String ad3 = "Code " + item.getAccessCode() + ", Étage " + item.getFloor();
            mBinding.tvAddressThree.setText(ad3);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getItem(getAdapterPosition()));

            mBinding.imageViewTick.setChecked(true);
        }
    }
}
