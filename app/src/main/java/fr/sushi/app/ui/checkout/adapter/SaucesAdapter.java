package fr.sushi.app.ui.checkout.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ListEachRowAccompagnemenntsBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;
import fr.sushi.app.ui.checkout.commade.model.FreeSaucesItem;
import fr.sushi.app.ui.checkout.commade.model.PayingSaucesItem;
import fr.sushi.app.ui.checkout.commade.model.Sauces;

public class SaucesAdapter extends BaseAdapter<Sauces> {
    private Context context;

    private Map<Integer, Integer> selectCountMap = new HashMap<>();

    private int FREE_ITEM = 0;
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
        if (item instanceof FreeSaucesItem) {
            return FREE_ITEM;
        } else {
            return PAID_ITEM;
        }
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = inflate(parent, R.layout.list_each_row_accompagnemennts);
        if (viewType == FREE_ITEM) {
            return new FreeItemViewHolder(binding);
        }
        return new PaidItemViewHolder(binding);
    }

    public void addPaidItems(List<Sauces> convertedList) {
        clear();
        addItem(convertedList);
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

            Integer value = selectCountMap.get(getAdapterPosition());
            if(value == null){
                binding.tvCount.setText(String.valueOf(0));
            }else {
                binding.tvCount.setText(String.valueOf(value));
            }

        }

        @Override
        public void onClick(View v) {
            Sauces item = getItem(getAdapterPosition());
            if (v.getId() == R.id.imgViewMinus && item.selectCount <= 0) {
                return;
            }
            Integer value = selectCountMap.get(getAdapterPosition());

            if (v.getId() == R.id.imgViewPlus) {
                if (value == null) {
                    selectCountMap.put(getAdapterPosition(), 1);
                } else {
                    selectCountMap.put(getAdapterPosition(), value + 1);
                }
            }else {
                selectCountMap.put(getAdapterPosition(), value - 1);
            }

            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, item, getAdapterPosition());
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

            Integer value = selectCountMap.get(getAdapterPosition());
            if(value == null){
                binding.tvCount.setText(String.valueOf(0));
            }else {
                binding.tvCount.setText(String.valueOf(value));
            }
        }

        @Override
        public void onClick(View v) {
            Sauces item = getItem(getAdapterPosition());
            if (v.getId() == R.id.imgViewMinus && item.selectCount <= 0) {
                return;
            }
            Integer value = selectCountMap.get(getAdapterPosition());

            if (v.getId() == R.id.imgViewPlus) {
                if (value == null) {
                    selectCountMap.put(getAdapterPosition(), 1);
                } else {
                    selectCountMap.put(getAdapterPosition(), value + 1);
                }
            }else {
                selectCountMap.put(getAdapterPosition(), value - 1);
            }

            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, item, getAdapterPosition());
        }
    }
}
