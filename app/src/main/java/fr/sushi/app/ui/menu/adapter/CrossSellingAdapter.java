package fr.sushi.app.ui.menu.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.CrossSellingItem;
import fr.sushi.app.data.model.food_menu.CrossSellingProductsItem;
import fr.sushi.app.databinding.ItemCrossSellingMenuBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;
/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 5/15/2019 at 11:34 AM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 5/15/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class CrossSellingAdapter extends BaseAdapter<CrossSellingProductsItem> {
    @Override
    public boolean isEqual(CrossSellingProductsItem left, CrossSellingProductsItem right) {
        return false;
    }

    @Override
    public BaseViewHolder newViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCrossSellingMenuBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_cross_selling_menu, parent, false);
        return new CrossSellingVH(binding);
    }

    class CrossSellingVH extends BaseViewHolder<CrossSellingProductsItem> {
        ItemCrossSellingMenuBinding binding;

        public CrossSellingVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemCrossSellingMenuBinding) viewDataBinding;
        }

        @Override
        public void bind(CrossSellingProductsItem item) {
            if (item.getMaxCount() > 1) {
                binding.radionButton.setVisibility(View.GONE);
                binding.checkBox.setVisibility(View.VISIBLE);
                binding.checkBox.setText(item.getName());
            } else {
                binding.checkBox.setVisibility(View.GONE);
                binding.radionButton.setVisibility(View.VISIBLE);
                binding.radionButton.setText(item.getName());
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
