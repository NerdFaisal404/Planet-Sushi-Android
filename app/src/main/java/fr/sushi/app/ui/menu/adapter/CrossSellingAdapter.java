package fr.sushi.app.ui.menu.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sushi.app.R;
import fr.sushi.app.data.model.food_menu.CrossSellingItem;
import fr.sushi.app.data.model.food_menu.CrossSellingProductsItem;
import fr.sushi.app.databinding.ItemCrossSellingMenuBinding;
import fr.sushi.app.ui.base.BaseAdapter;
import fr.sushi.app.ui.base.BaseViewHolder;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.util.Utils;
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

    private HashMap<String, RadioButton> radioButtonCheckList;
    private HashMap<String, CrossSellingProductsItem> radioSelectedItemList;
    private Map<String, String> crossSellingItemRequiredList;
    private Map<String, String> crossSellingItemClickedList;
    private ItemCountListener listener;

    public List<CrossSellingProductsItem> selectedItemList = new ArrayList<>();

    private String mOwnTime = "";

    public CrossSellingAdapter(Map<String, String> crossSellingItemRequiredList) {
        this.crossSellingItemRequiredList = new HashMap<>();
        radioButtonCheckList = new HashMap<>();
        radioSelectedItemList = new HashMap<>();
        crossSellingItemClickedList = new HashMap<>();
        this.crossSellingItemRequiredList = crossSellingItemRequiredList;

        if (PlaceUtil.getRecentSearchAddress() != null) {
            mOwnTime = PlaceUtil.getRecentSearchAddress().getOrder().getSchedule();
        }
    }

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

    public void cleatAllThings() {
        radioButtonCheckList.clear();
        radioSelectedItemList.clear();
        crossSellingItemClickedList.clear();
    }

    class CrossSellingVH extends BaseViewHolder<CrossSellingProductsItem> {
        ItemCrossSellingMenuBinding binding;

        public CrossSellingVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (ItemCrossSellingMenuBinding) viewDataBinding;
        }

        @Override
        public void bind(CrossSellingProductsItem item) {

            if (Integer.parseInt(item.getOnlyAm()) == 0) {
                //show original color
                binding.backgroundLayout.setBackgroundColor(Color.WHITE);
            } else {

                if (TextUtils.isEmpty(mOwnTime)) {
                    // show different color
                    binding.backgroundLayout.setBackgroundColor(Color.parseColor("#40808080"));
                } else {
                    //check exist time or not

                    String arr[] = mOwnTime.split(":");
                    if (Integer.parseInt(arr[0]) >= 11 && Integer.parseInt(arr[0]) <= 15) {
                        Log.d("TimeTest", "match: ");
                        binding.backgroundLayout.setBackgroundColor(Color.WHITE);
                    } else {
                        binding.backgroundLayout.setBackgroundColor(Color.parseColor("#40808080"));
                        Log.d("TimeTest", "not match 2: ");
                    }
                }
            }

            if (item.getMaxCount() > 1) {

                binding.checkItem.setVisibility(View.VISIBLE);
                binding.radioItem.setVisibility(View.GONE);

                if (item.getItemClickCount() > 0) {
                    binding.emptyCheck.setVisibility(View.INVISIBLE);
                    binding.textViewSelectedCheck.setVisibility(View.VISIBLE);
                    binding.textViewSelectedCheck.setText(String.valueOf(item.getItemClickCount()));

                    binding.imageViewDelete.setVisibility(View.VISIBLE);

                    selectedItemList.add(item);
                    String tc = crossSellingItemClickedList.get(item.getCategoryName());
                    int totalCount = tc == null ? 0 : Integer.parseInt(tc);
                    totalCount++;
                    crossSellingItemClickedList.put(item.getCategoryName(), String.valueOf(totalCount));

                    if (listener != null && item.isRequired()) {
                        listener.onGetItemCount(item, 1);
                    }

                } else {
                    binding.emptyCheck.setVisibility(View.VISIBLE);
                    binding.textViewSelectedCheck.setVisibility(View.INVISIBLE);

                    binding.imageViewDelete.setVisibility(View.INVISIBLE);
                }

                // binding.textViewCheckItem.setText(item.getName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.textViewCheckItem.setText(Html.fromHtml(getColorText(item.getName()), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    binding.textViewCheckItem.setText(Html.fromHtml(getColorText(item.getName())), TextView.BufferType.SPANNABLE);
                }

                binding.checkItem.setOnClickListener(v -> {
                    String mc = crossSellingItemRequiredList.get(item.getCategoryName());
                    int maxCount = mc == null ? 0 : Integer.parseInt(mc);
                    String tc = crossSellingItemClickedList.get(item.getCategoryName());
                    int totalCount = tc == null ? 0 : Integer.parseInt(tc);

                    if (totalCount < maxCount) {
                        item.setItemClickCount(item.getItemClickCount() + 1);
                        binding.emptyCheck.setVisibility(View.INVISIBLE);
                        binding.textViewSelectedCheck.setVisibility(View.VISIBLE);
                        binding.textViewSelectedCheck.setText(String.valueOf(item.getItemClickCount()));

                        totalCount++;
                        crossSellingItemClickedList.put(item.getCategoryName(), String.valueOf(totalCount));

                        if (listener != null) {
                            if (item.isRequired()) {
                                listener.onGetItemCount(item, 1);
                            }
                        }

                        if (item.getItemClickCount() > 0) {
                            binding.imageViewDelete.setVisibility(View.VISIBLE);
                        } else {
                            binding.imageViewDelete.setVisibility(View.INVISIBLE);
                        }

                        selectedItemList.add(item);
                    }
                });

                binding.imageViewDelete.setOnClickListener(v -> {
                    item.setItemClickCount(item.getItemClickCount() - 1);

                    String tc = crossSellingItemClickedList.get(item.getCategoryName());
                    int totalCount = tc == null ? 0 : Integer.parseInt(tc);
                    totalCount--;
                    if (totalCount < 0) {
                        totalCount = 0;
                    }
                    crossSellingItemClickedList.put(item.getCategoryName(), String.valueOf(totalCount));

                    selectedItemList.remove(item);

                    if (listener != null) {
                        if (item.isRequired()) {
                            listener.onGetItemCount(item, -1);
                        }
                    }

                    if (item.getItemClickCount() > 0) {
                        binding.emptyCheck.setVisibility(View.INVISIBLE);
                        binding.textViewSelectedCheck.setVisibility(View.VISIBLE);
                        binding.textViewSelectedCheck.setText(String.valueOf(item.getItemClickCount()));

                    } else {
                        item.setItemClickCount(0);
                        binding.emptyCheck.setVisibility(View.VISIBLE);
                        binding.textViewSelectedCheck.setVisibility(View.INVISIBLE);

                        if (item.getItemClickCount() > 0) {
                            binding.imageViewDelete.setVisibility(View.VISIBLE);
                        } else {
                            binding.imageViewDelete.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            } else {
                binding.radioItem.setVisibility(View.VISIBLE);
                binding.checkItem.setVisibility(View.GONE);


                if (item.getItemClickCount() > 0) {
                    binding.radioButton.setChecked(true);

                    radioButtonCheckList.put(item.getCategoryName(), binding.radioButton);
                    radioSelectedItemList.put(item.getCategoryName(), item);
                    selectedItemList.add(item);

                    if (listener != null && item.isRequired()) {
                        listener.onGetItemCount(item, 1);
                    }

                } else {
                    binding.radioButton.setChecked(false);
                }

                //binding.textViewRadioItem.setText(item.getName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.textViewRadioItem.setText(Html.fromHtml(getColorText(item.getName()), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    binding.textViewRadioItem.setText(Html.fromHtml(getColorText(item.getName())), TextView.BufferType.SPANNABLE);
                }

                binding.textViewPrice.setText(item.getPriceHt() + "€");

                binding.radioItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioButton rdBtn = radioButtonCheckList.get(item.getCategoryName());

                        if (rdBtn != null) {

                            if (rdBtn.equals(binding.radioButton)) {
                                binding.radioButton.setChecked(false);
                                radioButtonCheckList.remove(item.getCategoryName());

                                selectedItemList.remove(item);
                                radioSelectedItemList.remove(item.getCategoryName());

                                if (listener != null && item.isRequired()) {
                                    listener.onGetItemCount(item, -1);
                                }
                            } else {
                                binding.radioButton.setChecked(true);
                                rdBtn.setChecked(false);
                                radioButtonCheckList.put(item.getCategoryName(), binding.radioButton);

                                CrossSellingProductsItem prevItem = radioSelectedItemList.get(item.getCategoryName());
                                if (prevItem != null) {
                                    prevItem.setItemClickCount(0);
                                }
                                selectedItemList.remove(prevItem);
                                item.setItemClickCount(1);
                                selectedItemList.add(item);
                                radioSelectedItemList.put(item.getCategoryName(), item);
                            }

                        } else {
                            binding.radioButton.setChecked(true);
                            radioButtonCheckList.put(item.getCategoryName(), binding.radioButton);

                            radioSelectedItemList.put(item.getCategoryName(), item);
                            item.setItemClickCount(1);
                            selectedItemList.add(item);

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

        private String getColorText(String text) {
            String[] t = text.split("\\s");
            if (t.length > 0) {
                String value = "";
                for (int i = 0; i < t.length; i++) {
                    if (i == t.length - 1) {
                        value += "<font color=#EA148A>" + t[i] + " " + "</font>";
                    } else {
                        value += "<font font color=#394F61>" + t[i] + " " + "</font>";
                    }
                }
                return value;

            } else {
                return text;
            }
        }
    }

    public interface ItemCountListener {
        void onGetItemCount(CrossSellingProductsItem item, int count);
    }
}
