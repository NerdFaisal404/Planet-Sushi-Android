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
import fr.sushi.app.ui.checkout.accompagnements.AccompagnementsAdapter;
import fr.sushi.app.ui.checkout.commade.model.UpsellItem;
import fr.sushi.app.util.Utils;

public class AccomplishmentAdapter extends BaseAdapter<UpsellItem> {
    private Context context;

    public AccomplishmentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public boolean isEqual(UpsellItem left, UpsellItem right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = inflate(parent, R.layout.list_each_row_accompagnemennts);
        return new ItemViewHolder(binding);
    }

    private class ItemViewHolder extends BaseViewHolder<UpsellItem> {
        private ListEachRowAccompagnemenntsBinding binding;

        public ItemViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ListEachRowAccompagnemenntsBinding) viewDataBinding;
            setClickListener(binding.imgViewMinus, binding.imgViewPlus);
        }

        @Override
        public void bind(UpsellItem item) {
            binding.itemName.setText(item.getName());
            binding.tvPrice.setText(Utils.getDecimalFormat(Double.parseDouble(item.getPriceTtc())) + "€");;
            Glide.with(context).load(item.getCoverUrl()).into(binding.imageViewItem);
            binding.tvCount.setText(String.valueOf(item.selectCount));
        }

        @Override
        public void onClick(View v) {
            UpsellItem item = getItem(getAdapterPosition());
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
