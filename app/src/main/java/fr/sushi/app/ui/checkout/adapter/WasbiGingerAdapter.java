package fr.sushi.app.ui.checkout.adapter;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ListEachRowAccompagnemenntsBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;
import fr.sushi.app.ui.checkout.commade.model.PayingWasabiGingerItem;

public class WasbiGingerAdapter extends BaseAdapter<PayingWasabiGingerItem> {
    @Override
    public boolean isEqual(PayingWasabiGingerItem left, PayingWasabiGingerItem right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = inflate(parent, R.layout.list_each_row_accompagnemennts);
        return new ItemViewHolder(binding);
    }

    private class ItemViewHolder extends BaseViewHolder<PayingWasabiGingerItem>{
        private ListEachRowAccompagnemenntsBinding binding;
        public ItemViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ListEachRowAccompagnemenntsBinding)viewDataBinding;
        }

        @Override
        public void bind(PayingWasabiGingerItem item) {

        }

        @Override
        public void onClick(View v) {

        }
    }
}
