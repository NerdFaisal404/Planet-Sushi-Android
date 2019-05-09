package fr.sushi.app.ui.objectbox;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ItemTestUserBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;

public class UserTestAdapter extends BaseAdapter<User> {
    @Override
    public boolean isEqual(User left, User right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        return new UserHolder(inflate(parent, R.layout.item_test_user));
    }

    private class UserHolder extends BaseViewHolder<User>{
        private ItemTestUserBinding binding;
        public UserHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemTestUserBinding)viewDataBinding;
            setClickListener(binding.userName, binding.userId);
        }

        @Override
        public void bind(User item) {
            binding.setUser(item);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getItem(getAdapterPosition()));
        }
    }
}
