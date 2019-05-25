package fr.sushi.app.ui.checkout.commade;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.model.food_menu.CrossSellingCategoriesItem;
import fr.sushi.app.data.model.food_menu.CrossSellingItem;
import fr.sushi.app.data.model.food_menu.CrossSellingProductsItem;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.menu.RecyclerSectionItemDecoration;
import fr.sushi.app.ui.menu.adapter.CrossSellingAdapter;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.Utils;
import fr.sushi.app.util.swipanim.Extension;
import fr.sushi.app.util.swipanim.ItemTouchHelperExtension;

public class CommadeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface Listener {
        void onItemDeselect(MyCartProduct item, int index);
        void onRefreshUi();
    }

    private Context mContext;
    private final int ITEM_NO_SWIPE = 0;
    private final int ITEM_WITH_SWIPE = 1;
    private List<MyCartProduct> productsItems;
    private ItemTouchHelperExtension mItemTouchHelperExtension;
    // private CommadeAdapter.Listener itemClickListener;

    private BottomSheetDialog dialog;
    double totalPrice;
    int count = 1;

    private Listener itemClickListener;
    public CommadeAdapter(Context context, List<MyCartProduct> itemList, Listener listener) {
        this.mContext = context;
        this.productsItems = itemList;
        itemClickListener = listener;
    }

    /*@Override
    public int getItemViewType(int position) {
        MyCartProduct item = productsItems.get(position);

        if (item.isSelected()) {
            return ITEM_WITH_SWIPE;
        }
        return ITEM_NO_SWIPE;
    }*/

    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    public void move(int from, int to) {
        MyCartProduct prev = productsItems.remove(from);
        productsItems.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_commade, viewGroup, false);
        return new CommadeAdapter.ItemSwipeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int index) {
        MyCartProduct item = productsItems.get(index);
        CommadeAdapter.BaseHolder holder = (CommadeAdapter.BaseHolder) viewHolder;

        if (holder instanceof CommadeAdapter.ItemSwipeViewHolder) {
            CommadeAdapter.ItemSwipeViewHolder swipeViewHolder = (CommadeAdapter.ItemSwipeViewHolder) holder;
            swipeViewHolder.mActionViewRefresh.setOnClickListener(v -> {
                mItemTouchHelperExtension.closeOpened();
                itemClickListener.onItemDeselect(item, index);
            });
        }

        holder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsItem productsItem = DataCacheUtil.getProductItems(item);
                if(productsItem == null) return;
                if (productsItem.getActiveCrossSelling() == 1) {
                    isActiveCrossSelling(productsItem);
                } else {
                    showBottomSheet(productsItem);
                }
            }
        });

        holder.bind(item);
    }

    class BaseHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemCount, tvPice, sideProductsTv;
        View mViewContent, selectedView;
        View mActionContainer;
        ImageView imageViewItem;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.tvPrice);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            sideProductsTv = itemView.findViewById(R.id.side_products_tv);
            itemCount = itemView.findViewById(R.id.tvCount);
            tvPice = itemView.findViewById(R.id.tvPiece);
        }

        private void bind(MyCartProduct item) {
            Glide.with(mContext).load(item.getCoderUrl()).into(imageViewItem);

            String[] title = item.getName().split("\\s");

            itemCount.setText(item.getItemCount() + "X");


            if (title.length > 0) {
                String value = null;
                for (int i = 0; i < title.length; i++) {

                    if (i == 0) {
                        value = "<font color=#394F61>" + title[0] + " " + "</font>";
                        // binding.itemName.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.color_darker_gray)));
                    } else {
                        itemName.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.colorPink)));
                        value += "<font font color=#EA148A>" + title[i] + " " + "</font>";
                    }


                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemName.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    itemName.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);
                }

            } else {
                itemName.setText(item.getName());
            }

            itemPrice.setText(Utils.getDecimalFormat(Double.parseDouble(item.getPriceHt()) * item.getItemCount()) + "€");
            tvPice.setText(item.getPiece() + " Pieces ");

            List<CrossSellingSelectedItem> itemSideProducts = DBManager.on()
                    .getCrossSellingItemById(item.getProductId());

            String sideProducts = "";
            for(CrossSellingSelectedItem cItem : itemSideProducts){
               sideProducts +=cItem.getProductCount()+"X "+cItem.getProductName();
               if(!cItem.isFree()){
                   sideProducts += "("+cItem.getProductPrice()+")\n";
               }else {
                   sideProducts +="\n";
               }
            }
            if(TextUtils.isEmpty(sideProducts)){
              sideProductsTv.setVisibility(View.GONE);
            }else {
                sideProductsTv.setText(sideProducts);
            }
        }
    }

    class ItemSwipeViewHolder extends CommadeAdapter.BaseHolder implements Extension {
        View mActionViewRefresh;

        public ItemSwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mActionViewRefresh = itemView.findViewById(R.id.view_list_repo);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    private void showBottomSheet(ProductsItem item) {
        count = DBManager.on().getProductCountById(item.getIdProduct());
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_item_details, null);

        dialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView tvTitle = (TextView) bottomSheet.findViewById(R.id.tvTitle);
        TextView tvCount = (TextView) bottomSheet.findViewById(R.id.tvCount);
        ImageView ivMinus = bottomSheet.findViewById(R.id.ivMinus);
        ImageView ivPlus = bottomSheet.findViewById(R.id.ivPlus);
        TextView tvPrice = bottomSheet.findViewById(R.id.tvPrice);
        ImageView ivDownArrow = bottomSheet.findViewById(R.id.ivDownArrow);
        ImageView ivItem = bottomSheet.findViewById(R.id.ivItem);
        TextView tvTagList = bottomSheet.findViewById(R.id.tvTagList);
        LinearLayout adjustLayout = bottomSheet.findViewById(R.id.layoutAdjust);
        String[] title = item.getName().split("\\s");

        tvCount.setText(String.valueOf(count));

        totalPrice = Double.parseDouble(item.getPriceTtc());
        tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");

        if (title.length > 0) {

            for (int i = 0; i < title.length; i++) {
                if (i == 0) {
                    tvTitle.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.color_darker_gray)));
                } else {
                    tvTitle.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.colorPink)));

                }
            }

        } else {
            tvTitle.setText(item.getName());
        }

        ivMinus.setOnClickListener(v -> {
            if (count == 0) {
                tvCount.setText(count + "");
                return;
            }
            count -= 1;
            totalPrice -= Double.parseDouble(item.getPriceTtc());
            tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
            tvCount.setText(count + "");
        });

        ivPlus.setOnClickListener(v -> {
            count += 1;
            totalPrice += Double.parseDouble(item.getPriceTtc());
            tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
            tvCount.setText(count + "");

        });

        tvTagList.setText(Html.fromHtml(item.getDescriptionShort()));


        ivDownArrow.setOnClickListener(v -> dialog.dismiss());
        adjustLayout.setOnClickListener(v -> {
            DBManager.on().saveProductItem(item, count);
            itemClickListener.onRefreshUi();
            dialog.dismiss();
        });

        Picasso.get().load(item.getPictureUrl()).into(ivItem);

    }

    private void isActiveCrossSelling(ProductsItem item) {
        Log.w("ProductIdList", "id: " + item.getIdProduct());
        count = DBManager.on().getProductCountById(item.getIdProduct());

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_is_active_cross_selling_item_details, null);

        BottomSheetDialog crossSellingBottomSheet = new BottomSheetDialog(mContext, R.style.BottomSheetDialogStyle);
        crossSellingBottomSheet.setContentView(bottomSheet);
        crossSellingBottomSheet.setCanceledOnTouchOutside(true);
        crossSellingBottomSheet.show();

        TextView tvTitle = bottomSheet.findViewById(R.id.tvItemName);
        TextView tvClose = bottomSheet.findViewById(R.id.tvClose);
        TextView tvCount = bottomSheet.findViewById(R.id.tvCount);
        ImageView ivMinus = bottomSheet.findViewById(R.id.ivMinus);
        ImageView ivPlus = bottomSheet.findViewById(R.id.ivPlus);
        TextView tvPrice = bottomSheet.findViewById(R.id.tvPrice);
        ImageView ivItem = bottomSheet.findViewById(R.id.ivItem);
        LinearLayout adjustLayout = bottomSheet.findViewById(R.id.layoutAdjust);
        TextView tvTagList = bottomSheet.findViewById(R.id.tvTagList);
        tvCount.setText(String.valueOf(count));
        // Cross selling part

        List<CrossSellingProductsItem> crossSellingProductsItemList = new ArrayList<>();
        Map<String, String> crossSellingItemRequiredList = new HashMap<>();

        boolean isItemRequired = false;
        int requireCount = 0;
        final int[] tempRequireCount = {0};

        if (CommonUtility.currentMenuResponse != null) {
            List<CrossSellingCategoriesItem> crossSellingCategories = CommonUtility.currentMenuResponse.getResponse().getCrossSellingCategories();
            Log.d("CrossCategoryTest", "Cross list" + crossSellingCategories.size());
            for (CrossSellingCategoriesItem categoriesItem : crossSellingCategories) {
                for (CrossSellingItem crossSellingItem : item.getCrossSelling()) {
                    for (CrossSellingProductsItem product : categoriesItem.getProducts()) {
                        if (product.getIdCategory().equals(String.valueOf(crossSellingItem.getIdCategory()))) {
                            product.setItemClickCount(0);
                            product.setMaxCount(crossSellingItem.getQuantityMax());
                            product.setFree(crossSellingItem.getIsFree() == 1);
                            product.setRequired(crossSellingItem.getIsRequired() == 1);

                            if (product.isRequired()) {
                                crossSellingItemRequiredList.put(product.getCategoryName(), String.valueOf(product.getMaxCount()));
                            }
                            Log.w("CrossCategoryTest", "isRequired: " + product.isRequired());
                            if (!isItemRequired) {
                                isItemRequired = product.isRequired();
                            }
                            product.setCategoryName(crossSellingItem.getCategoryName());
                            product.setDescription(crossSellingItem.getDescription());
                            crossSellingProductsItemList.add(product);
                        }
                    }
                }
            }
        }

        for (String key : crossSellingItemRequiredList.keySet()) {
            String a = crossSellingItemRequiredList.get(key);
            int b = a == null ? 0 : Integer.parseInt(a);
            requireCount += b;
        }

        if (isItemRequired) {
            // Button will be disable
            adjustLayout.setEnabled(false);
        } else {
            //button will be enable
            adjustLayout.setEnabled(true);
        }

        List<CrossSellingSelectedItem> selectedItemList = DBManager.on().getCrossSellingItemById(item.getIdProduct());

        if (selectedItemList != null && !selectedItemList.isEmpty()) {
            for (CrossSellingSelectedItem selectedItem : selectedItemList) {
                for (CrossSellingProductsItem productsItem : crossSellingProductsItemList) {
                    if (selectedItem.getProductId().equals(productsItem.getIdProduct())) {
                        productsItem.setItemClickCount(selectedItem.getProductCount());
                    }
                }

            }
        }

        CrossSellingAdapter crossAdapter = new CrossSellingAdapter(crossSellingItemRequiredList);

        RecyclerView recyclerView = bottomSheet.findViewById(R.id.rc_cross_selling);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        crossAdapter.clear();
        crossAdapter.addItems(crossSellingProductsItemList);

        recyclerView.setAdapter(crossAdapter);
        RecyclerSectionItemDecoration sectionItemDecoration;
        sectionItemDecoration =
                new RecyclerSectionItemDecoration(mContext.getResources().getDimensionPixelSize(R.dimen.dp10),
                        true,
                        getSectionCallback(crossSellingProductsItemList));

        recyclerView.addItemDecoration(sectionItemDecoration);
        int finalRequireCount = requireCount;
        crossAdapter.setItemCountListener((item1, count) -> {
            tempRequireCount[0] += count;
            if (tempRequireCount[0] == finalRequireCount) {
                adjustLayout.setEnabled(true);
            } else {
                adjustLayout.setEnabled(false);
            }
        });


        // Cross part end

        String[] title = item.getName().split("\\s");

        totalPrice = Double.parseDouble(item.getPriceTtc());
        tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");

        if (title.length > 0) {

            for (int i = 0; i < title.length; i++) {
                if (i == 0) {
                    tvTitle.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.color_darker_gray)));
                } else {
                    tvTitle.append(Utils.getColoredString(title[i], ContextCompat.getColor(mContext, R.color.colorPink)));

                }
            }

        } else {
            tvTitle.setText(item.getName());
        }

        tvTagList.setText(Html.fromHtml(item.getDescriptionShort()));

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    tvCount.setText(count + "");
                    return;
                }
                count -= 1;
                totalPrice -= Double.parseDouble(item.getPriceTtc());
                tvPrice.setText(Utils.getDecimalFormat(totalPrice) + "€");
                tvCount.setText(count + "");
            }
        });

        ivPlus.setOnClickListener(v -> {
            count += 1;
            totalPrice += Double.parseDouble(item.getPriceTtc());
            tvPrice.setText(Utils.getDecimalFormat(totalPrice) + ",00 €");
            tvCount.setText(count + "");

        });


        tvClose.setOnClickListener(v -> crossSellingBottomSheet.dismiss());
        adjustLayout.setOnClickListener(v -> {
            // What is this
            DBManager.on().saveProductItem(item, count);

            if (!crossAdapter.selectedItemList.isEmpty()) {
                DBManager.on().deleteSelectedItemById(item.getIdProduct());
                for (CrossSellingProductsItem productsItem : crossAdapter.selectedItemList) {
                    DBManager.on().insertCrossSellingSelectedItem(item.getIdProduct(), productsItem);
                }
                crossAdapter.selectedItemList.clear();
                Log.i("CrossCategoryTest", "selected list size: " + crossAdapter.selectedItemList.size());
                // here we can add price
                crossAdapter.selectedItemList.clear();
            }
            itemClickListener.onRefreshUi();
            crossSellingBottomSheet.dismiss();
        });

        Picasso.get().load(item.getPictureUrl()).into(ivItem);

    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<CrossSellingProductsItem> item) {

        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                if (item.size() > 0) {
                    if (position >= 0) {
                        return position == 0
                                || !item.get(position)
                                .getCategoryName().equals(item.get(position - 1)
                                        .getCategoryName());
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            @Override
            public String getSectionHeader(int position) {
                if (position >= 0) {
                    return item.get(position)
                            .getCategoryName();
                } else {
                    return "";
                }
            }

            @Override
            public String getSectionSubHeader(int position) {
                if (position >= 0) {
                    return item.get(position)
                            .getDescription();
                } else {
                    return "";
                }
            }
        };
    }
}