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
import fr.sushi.app.ui.checkout.commade.model.FreeWasabiGingerItem;
import fr.sushi.app.ui.checkout.commade.model.PayingWasabiGingerItem;
import fr.sushi.app.ui.checkout.commade.model.Wasbi;

public class WasbiGingerAdapter extends BaseAdapter<Wasbi> {
    private Map<Integer, Integer> selectCountWsabiMap;
    private Context context;
    private int FREE_ITEM =0;
    private int PAID_ITEM = 1;

    public WasbiGingerAdapter(Context context, Map<Integer, Integer> selectMap) {
        this.context = context;
        this.selectCountWsabiMap = selectMap;
    }

    @Override
    public boolean isEqual(Wasbi left, Wasbi right) {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        Wasbi item = getItem(position);
        if(item instanceof FreeWasabiGingerItem){
            return FREE_ITEM;
        }else {
            return PAID_ITEM;
        }
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = inflate(parent, R.layout.list_each_row_accompagnemennts);
        if(viewType == FREE_ITEM) {
            return new FreeItemViewHolder(binding);
        }
        return new PaidItemViewHolder(binding);
    }

    public void addPaidItems(List<Wasbi> convertedList) {
        clear();
        addItem(convertedList);
    }

    private class FreeItemViewHolder extends BaseViewHolder<FreeWasabiGingerItem> {
        private ListEachRowAccompagnemenntsBinding binding;

        public FreeItemViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ListEachRowAccompagnemenntsBinding) viewDataBinding;
            setClickListener(binding.imgViewMinus, binding.imgViewPlus);
        }

        @Override
        public void bind(FreeWasabiGingerItem item) {
            binding.itemName.setText(item.getName());
            binding.tvPrice.setText(item.getPriceHt() + "€");
            Glide.with(context).load(item.getCoverUrl()).into(binding.imageViewItem);
            Integer value = selectCountWsabiMap.get(getAdapterPosition());
            if(value == null){
                binding.tvCount.setText(String.valueOf(0));
            }else {
                binding.tvCount.setText(String.valueOf(value));
            }
        }

        @Override
        public void onClick(View v) {
            Wasbi item = getItem(getAdapterPosition());
            if(v.getId() == R.id.imgViewMinus && item.selectCount <= 0){
                return;
            }
            Integer value = selectCountWsabiMap.get(getAdapterPosition());

            if (v.getId() == R.id.imgViewPlus) {
                if (value == null) {
                    selectCountWsabiMap.put(getAdapterPosition(), 1);
                } else {
                    selectCountWsabiMap.put(getAdapterPosition(), value + 1);
                }
            }else {
                selectCountWsabiMap.put(getAdapterPosition(), value - 1);
            }

            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, item, getAdapterPosition());
        }
    }

    private class PaidItemViewHolder extends BaseViewHolder<PayingWasabiGingerItem> {
        private ListEachRowAccompagnemenntsBinding binding;

        public PaidItemViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ListEachRowAccompagnemenntsBinding) viewDataBinding;
            setClickListener(binding.imgViewMinus, binding.imgViewPlus);
        }

        @Override
        public void bind(PayingWasabiGingerItem item) {
            binding.itemName.setText(item.getName());
            binding.tvPrice.setText(item.getPriceHt() + "€");
            Glide.with(context).load(item.getCoverUrl()).into(binding.imageViewItem);
            Integer value = selectCountWsabiMap.get(getAdapterPosition());
            if(value == null){
                binding.tvCount.setText(String.valueOf(0));
            }else {
                binding.tvCount.setText(String.valueOf(value));
            }
        }

        @Override
        public void onClick(View v) {
            Wasbi item = getItem(getAdapterPosition());
            if(v.getId() == R.id.imgViewMinus && item.selectCount <= 0){
                return;
            }
            Integer value = selectCountWsabiMap.get(getAdapterPosition());

            if (v.getId() == R.id.imgViewPlus) {
                if (value == null) {
                    selectCountWsabiMap.put(getAdapterPosition(), 1);
                } else {
                    selectCountWsabiMap.put(getAdapterPosition(), value + 1);
                }
            }else {
                selectCountWsabiMap.put(getAdapterPosition(), value - 1);
            }

            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, item, getAdapterPosition());
        }
    }
}
