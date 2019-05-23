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
import fr.sushi.app.ui.checkout.commade.model.FreeSaucesItem;
import fr.sushi.app.ui.checkout.commade.model.PayingSaucesItem;
import fr.sushi.app.ui.checkout.commade.model.Sauces;

public class SaucesAdapter extends BaseAdapter<Sauces> {
    private Context context;

    private int FREE_ITEM =0;
    private int PAID_ITEM = 1;

    public SaucesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public boolean isEqual(Sauces left, Sauces right) {
        return false;
    }


    @Override
    public int getItemViewType(int position) {
        Sauces item = getItem(position);
        if(item instanceof FreeSaucesItem){
            return FREE_ITEM;
        }else {
            return PAID_ITEM;
        }
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = inflate(parent, R.layout.list_each_row_accompagnemennts);
        if(viewType == FREE_ITEM){
            return new FreeItemViewHolder(binding);
        }
        return new PaidItemViewHolder(binding);
    }

    private class FreeItemViewHolder extends BaseViewHolder<FreeSaucesItem> {
        private ListEachRowAccompagnemenntsBinding binding;

        public FreeItemViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ListEachRowAccompagnemenntsBinding) viewDataBinding;
            setClickListener(binding.imgViewPlus, binding.imgViewMinus);
        }

        @Override
        public void bind(FreeSaucesItem item) {
            binding.itemName.setText(item.getName());
            binding.tvPrice.setText(item.getPriceHt() + "€");
            Glide.with(context).load(item.getCoverUrl()).into(binding.imageViewItem);
            binding.tvCount.setText(String.valueOf(item.selectCount));
        }

        @Override
        public void onClick(View v) {
            PayingSaucesItem item = (PayingSaucesItem)getItem(getAdapterPosition());
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

    private class PaidItemViewHolder extends BaseViewHolder<PayingSaucesItem> {
        private ListEachRowAccompagnemenntsBinding binding;

        public PaidItemViewHolder(ViewDataBinding viewDataBinding) {
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
            PayingSaucesItem item = (PayingSaucesItem)getItem(getAdapterPosition());
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
