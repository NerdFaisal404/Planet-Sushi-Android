package fr.sushi.app.ui.profile;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.data.model.ProfileItemModel;
import fr.sushi.app.databinding.ItemProfileBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;

public class ProfileAdapter extends BaseAdapter<ProfileItemModel> {
    @Override
    public boolean isEqual(ProfileItemModel left, ProfileItemModel right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_profile, parent, false);
        return new ProfileVH(binding);
    }

    class ProfileVH extends BaseViewHolder<ProfileItemModel> {
        ItemProfileBinding binding;

        public ProfileVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemProfileBinding) viewDataBinding;
        }

        @Override
        public void bind(ProfileItemModel item) {
            binding.imageViewUser.setImageResource(item.getIcon());
            binding.textViewProfileInfo.setText(item.getItemName());
            setClickListener(binding.getRoot());
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(mViewDataBinding.getRoot(), getItem(getAdapterPosition()));
        }
    }
}
