package fr.sushi.app.ui.menu.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.HashMap;

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

    private HashMap<String, RadioButton> radioButtonCheckList = new HashMap<>();
    private ItemCountListener listener;

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

    public void setItemCountListener(ItemCountListener listener) {
        this.listener = listener;
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

                binding.checkItem.setVisibility(View.VISIBLE);
                binding.radioItem.setVisibility(View.GONE);

                if (item.getItemClickCount() >= 1) {
                    binding.emptyCheck.setVisibility(View.INVISIBLE);
                    binding.textViewSelectedCheck.setVisibility(View.VISIBLE);
                    binding.textViewSelectedCheck.setText(String.valueOf(item.getItemClickCount()));
                } else {
                    binding.emptyCheck.setVisibility(View.VISIBLE);
                    binding.textViewSelectedCheck.setVisibility(View.INVISIBLE);
                }

                binding.textViewCheckItem.setText(item.getName());

                binding.checkItem.setOnClickListener(v -> {

                    if (item.getMaxCount() != item.getItemClickCount()) {
                        item.setItemClickCount(item.getItemClickCount() + 1);
                        binding.emptyCheck.setVisibility(View.INVISIBLE);
                        binding.textViewSelectedCheck.setVisibility(View.VISIBLE);
                        binding.textViewSelectedCheck.setText(String.valueOf(item.getItemClickCount()));

                        if (listener != null) {
                            if (item.isRequired()) {
                                listener.onGetItemCount(item, 1);
                            }
                        }
                    }
                });

                binding.imageViewDelete.setOnClickListener(v -> {
                    item.setItemClickCount(item.getItemClickCount() - 1);

                    if (item.getItemClickCount() >= 1) {
                        binding.emptyCheck.setVisibility(View.INVISIBLE);
                        binding.textViewSelectedCheck.setVisibility(View.VISIBLE);
                        binding.textViewSelectedCheck.setText(String.valueOf(item.getItemClickCount()));

                        if (listener != null) {
                            if (item.isRequired()) {
                                listener.onGetItemCount(item, -1);
                            }
                        }

                    } else {
                        item.setItemClickCount(0);
                        binding.emptyCheck.setVisibility(View.VISIBLE);
                        binding.textViewSelectedCheck.setVisibility(View.INVISIBLE);
                    }
                });

            } else {
                binding.radioItem.setVisibility(View.VISIBLE);
                binding.checkItem.setVisibility(View.GONE);

                binding.textViewRadioItem.setText(item.getName());
                binding.textViewPrice.setText(item.getPriceHt() + "€");

                binding.radioItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioButton rdBtn = radioButtonCheckList.get(item.getCategoryName());

                        if (rdBtn != null) {

                            if (rdBtn.equals(binding.radioButton)) {
                                binding.radioButton.setChecked(false);
                                radioButtonCheckList.remove(item.getCategoryName());

                                if (listener != null && item.isRequired()) {
                                    listener.onGetItemCount(item, -1);
                                }
                            } else {
                                binding.radioButton.setChecked(true);
                                rdBtn.setChecked(false);
                                radioButtonCheckList.put(item.getCategoryName(), binding.radioButton);
                            }

                        } else {
                            Log.d("RadioItemCheck", "click");
                            binding.radioButton.setChecked(true);
                            radioButtonCheckList.put(item.getCategoryName(), binding.radioButton);

                            if (listener != null && item.isRequired()) {
                                listener.onGetItemCount(item, 1);
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface ItemCountListener {
        void onGetItemCount(CrossSellingProductsItem item, int count);
    }
}
