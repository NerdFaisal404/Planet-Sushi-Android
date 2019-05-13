package fr.sushi.app.ui.profileaddress.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.databinding.ItemAddressBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;

public class ProfileAddressAdapter extends BaseAdapter<ProfileAddressModel> {
    @Override
    public boolean isEqual(ProfileAddressModel left, ProfileAddressModel right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAddressBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_address, parent, false);
        return new ProfileAddressVH(binding);
    }

    class ProfileAddressVH extends BaseViewHolder<ProfileAddressModel> {
        ItemAddressBinding binding;

        ProfileAddressVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemAddressBinding) viewDataBinding;
        }

        @Override
        public void bind(ProfileAddressModel item) {
            setClickListener(binding.getRoot());

            // pass data in ui
            binding.setAddress(item);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(view, getItem(getAdapterPosition()));
        }
    }
}
