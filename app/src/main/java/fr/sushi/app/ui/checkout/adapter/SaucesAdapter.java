package fr.sushi.app.ui.checkout.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ListEachRowAccompagnemenntsBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;
import fr.sushi.app.ui.checkout.commade.model.PayingSaucesItem;

public class SaucesAdapter extends BaseAdapter<PayingSaucesItem> {
    private Context context;

    public SaucesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public boolean isEqual(PayingSaucesItem left, PayingSaucesItem right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = inflate(parent, R.layout.list_each_row_accompagnemennts);
        return new ItemViewHolder(binding);
    }

    private class ItemViewHolder extends BaseViewHolder<PayingSaucesItem> {
        private ListEachRowAccompagnemenntsBinding binding;

        public ItemViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ListEachRowAccompagnemenntsBinding) viewDataBinding;
            setClickListener(binding.imgViewPlus, binding.imgViewMinus);
        }

        @Override
        public void bind(PayingSaucesItem item) {
            binding.itemName.setText(item.getName());
            binding.tvPrice.setText(item.getPriceHt() + "€");
            Glide.with(context).load(item.getCoverUrl()).into(binding.imageViewItem);
            binding.tvCount.setText(String.valueOf(item.selectCount));
        }

        @Override
        public void onClick(View v) {
            PayingSaucesItem item = getItem(getAdapterPosition());
            if (v.getId() == R.id.imgViewPlus) {
                item.selectCount += 1;
            } else {
                if (item.selectCount > 0)
                    item.selectCount -= 1;
            }
            binding.tvCount.setText(String.valueOf(item.selectCount));
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, item);
        }
    }
}
