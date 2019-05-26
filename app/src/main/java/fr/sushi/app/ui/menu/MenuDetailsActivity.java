package fr.sushi.app.ui.menu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.data.model.food_menu.TopMenuItem;
import fr.sushi.app.databinding.ActivityMenuListDetailBinding;
import fr.sushi.app.ui.adressPicker.AddressPickerActivity;
import fr.sushi.app.ui.base.BaseActivity;
import fr.sushi.app.ui.cart.adapter.AddressAdapter;
import fr.sushi.app.ui.checkout.PaymentMethodCheckoutActivity;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.Utils;
import fr.sushi.app.util.flyanim.CircleAnimationUtil;
import fr.sushi.app.util.swipanim.ItemTouchHelperExtension;
import okhttp3.ResponseBody;

public class MenuDetailsActivity extends BaseActivity implements TopMenuAdapter.MenuItemClickListener {
    private ActivityMenuListDetailBinding binding;
    private int currentIndex;
    private List<CategoriesItem> categoriesItems;
    private LinearLayoutManager itemViewLayoutManager;

    //private MenuItemAdapter menuItemAdapter;
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    private TopMenuAdapter topMenuAdapter;
    private int visiblePosition;
    private List<MyCartProduct> selectedProducts = new ArrayList<>();
    private MenuItemSwipeAdapter menuItemSwipeAdapter;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    private SearchPlace mSearchPlace;

    private MenuDetailsViewModel menuDetailsViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu_list_detail;
    }

    @Override
    protected void startUI() {
        binding = (ActivityMenuListDetailBinding) getViewDataBinding();

        //showBottomView();
        Intent intent = getIntent();

        if (intent.hasExtra("index")) {
            currentIndex = intent.getIntExtra("index", 0);
        }
        if (intent.hasExtra(SearchPlace.class.getName())) {
            mSearchPlace = (SearchPlace) intent.getSerializableExtra(SearchPlace.class.getName());
        }
        //categoriesItems = (ArrayList<CategoriesItem>) intent.getSerializableExtra("items");
        categoriesItems = DataCacheUtil.getCategoryItemFromCache();

        observeData();

        binding.priceLayout.setOnClickListener(v -> startActivity(new Intent(MenuDetailsActivity.this, PaymentMethodCheckoutActivity.class)));

        binding.realtiveLayoutAddress.setOnClickListener(v -> {
            Intent intentAddress = new Intent(this, AddressPickerActivity.class);
            boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
            boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
            if (isLivarsion) {
                intentAddress.putExtra(IntentKey.KEY_IS_TAKEWAY,false);
            }else if (isExporter){
                intentAddress.putExtra(IntentKey.KEY_IS_TAKEWAY,true);
            }
            intentAddress.putExtra(IntentKey.KEY_FROM_FOOD_CATEGORY, false);
            startActivity(intentAddress);
        });

        if (categoriesItems != null && categoriesItems.size() > 0) {
            setUpToMenuAdapter();
            loadCategoryItems();
        }

    }


    private void observeData() {
        menuDetailsViewModel = ViewModelProviders.of(this).get(MenuDetailsViewModel.class);
    }


    private void setUpToMenuAdapter() {

        binding.recyclerViewMenuTop.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        itemViewLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewItems.setLayoutManager(itemViewLayoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerViewItems.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.bg_divider_white);
        horizontalDecoration.setDrawable(horizontalDivider);
        binding.recyclerViewItems.addItemDecoration(horizontalDecoration);
    }


    private void loadCategoryItems() {
        List<ProductsItem> productsItems = new ArrayList<>();
        List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        List<TopMenuItem> topMenuList = new ArrayList<>();
        int section = 0;
        for (CategoriesItem item : categoriesItems) {
            sections.add(new SectionedRecyclerViewAdapter.Section(section, item.getName()));
            String menuName = item.getName();
            String imgUrl = item.getPictureUrl();

            TopMenuItem topMenuItem = new TopMenuItem(menuName, imgUrl);
            topMenuList.add(topMenuItem);

            productsItems.addAll(item.getProducts());
            section = section + item.getProducts().size();
        }
        List<String> selectedItemIds = getSelectedItemIds();

        for (ProductsItem item : productsItems) {
            if (selectedItemIds.contains(item.getIdProduct())) {
                item.setSelected(true);
                Log.e("ItemSelect", "Item selected =" + item.getIdProduct());
            } else {
                item.setSelected(false);
            }
        }
        //menuItemAdapter = new MenuItemAdapter(this, productsItems, listener);
        menuItemSwipeAdapter = new MenuItemSwipeAdapter(this, productsItems, selectListener);
        mCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(binding.recyclerViewItems);
        menuItemSwipeAdapter.setItemTouchHelperExtension(mItemTouchHelper);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(this, R.layout.item_menu_section,
                R.id.section_tv, binding.recyclerViewItems, menuItemSwipeAdapter);

        binding.recyclerViewItems.setAdapter(sectionedRecyclerViewAdapter);
        setSections(sections);

        topMenuAdapter = new TopMenuAdapter(this, topMenuList);
        binding.recyclerViewMenuTop.setAdapter(topMenuAdapter);
        setScrollListener();

        /**
         * Scroll expected position
         */
        scrollHeaderMenu(currentIndex);
        int position = sectionedRecyclerViewAdapter.getSectionPosition(topMenuList.get(currentIndex).getName());
        scrollMainItemMenu(position);
    }

    private void setSections(List<SectionedRecyclerViewAdapter.Section> sections) {
        SectionedRecyclerViewAdapter.Section[] sectionArray = new SectionedRecyclerViewAdapter.Section[sections.size()];
        sectionedRecyclerViewAdapter.setSections(sections.toArray(sectionArray));

    }

    private void setScrollListener() {
        binding.recyclerViewItems.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int sectionPos = sectionedRecyclerViewAdapter.getSectionPosition(visiblePosition);
                    scrollHeaderMenu(sectionPos);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visiblePosition = itemViewLayoutManager.findFirstCompletelyVisibleItemPosition();

            }

        });
    }

    private void scrollHeaderMenu(int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MenuDetailsActivity.this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };

        smoothScroller.setTargetPosition(position);
        LinearLayoutManager manager = (LinearLayoutManager) binding.recyclerViewMenuTop.getLayoutManager();
        manager.startSmoothScroll(smoothScroller);
        binding.recyclerViewMenuTop.smoothScrollToPosition(position);
        showHeaderImage(position);

    }

    private void showHeaderImage(int position) {
        CategoriesItem categoriesItem = categoriesItems.get(position);
        /*if (!TextUtils.isEmpty(categoriesItem.getPictureUrl()))
            Picasso.get().load(categoriesItem.getPictureUrl())
                    .into(binding.ivMenu);*/
        Glide.with(this).load(categoriesItem.getPictureUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivMenu);
        topMenuAdapter.setSelectedItemPosition(position);
    }


    @Override
    public void onMenuItemClick(TopMenuItem menuItem) {
        int position = sectionedRecyclerViewAdapter.getSectionPosition(menuItem.getName());
        scrollMainItemMenu(position);
    }

    private void scrollMainItemMenu(int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MenuDetailsActivity.this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(position);
        LinearLayoutManager manager = (LinearLayoutManager) binding.recyclerViewItems.getLayoutManager();
        manager.startSmoothScroll(smoothScroller);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<MyCartProduct> myCartProducts = DBManager.on().getAllProducts();
        if(menuItemSwipeAdapter != null) {
            menuItemSwipeAdapter.setSelected(myCartProducts);
        }

        showBottomView();

        if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
            //binding.layoutSignup.setVisibility(View.GONE);
            binding.realtiveLayoutAddress.setVisibility(View.VISIBLE);
        }else {
            binding.realtiveLayoutAddress.setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());

        boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
        boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
        if (isLivarsion) {
            binding.tvDeliveryType.setText("Livraison");
            binding.tvDeliveryInfo.setText("prévue pour " + currentTime);
        } else if (isExporter) {
            binding.tvDeliveryType.setText("A emporter");
            binding.tvDeliveryInfo.setText("prévue pour " + currentTime);
        } else {
            binding.tvDeliveryType.setText("Livraison");
            binding.tvDeliveryInfo.setText("prévue pour " + currentTime);
        }

        SearchPlace recentSearchPlace = PlaceUtil.getRecentSearchAddress();
        if (recentSearchPlace != null) {
            binding.tvLocationInfo.setText(recentSearchPlace.getAddress() + "-" + recentSearchPlace.getCity() + ", " + recentSearchPlace.getPostalCode());
            binding.tvDeliveryInfo.setText("prévue pour " + recentSearchPlace.getOrder().getSchedule());
        }
    }

    @Override
    protected void stopUI() {

    }


    private MenuItemSwipeAdapter.Listener selectListener = new MenuItemSwipeAdapter.Listener() {
        @Override
        public void onItemClick(ProductsItem item, ImageView imageView) {
            imageView.setVisibility(View.VISIBLE);
            //MenuPrefUtil.saveItem(item);
            DBManager.on().saveProductItem(item);
            showBottomView();
            new CircleAnimationUtil().attachActivity(MenuDetailsActivity.this)
                    .setTargetView(imageView)
                    .setCircleDuration(100)
                    .setMoveDuration(500)
                    .setDestView(binding.tvCount)
                    .setAnimationListener(
                            new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    imageView.setVisibility(View.GONE);
                                    ObjectAnimator
                                            .ofFloat(binding.tvCount, "translationX", 0, 25, -25, 25,
                                                    -25, 15, -15, 6, -6, 0)
                                            .setDuration(200)
                                            .start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).startAnimation();
        }

        @Override
        public void onItemDeselect(ProductsItem item) {
            //MenuPrefUtil.removeItem(item);
            DBManager.on().removeProduct(item);
            DBManager.on().deleteSelectedItemById(item.getIdProduct());
            showBottomView();
        }

        @Override
        public void onRefreshBottomView() {
            showBottomView();
            List<MyCartProduct> myCartProducts = DBManager.on().getAllProducts();
            menuItemSwipeAdapter.setSelected(myCartProducts);
        }
    };


    private void showBottomView() {
        //selectedProducts = MenuPrefUtil.getSaveItems();
        selectedProducts = DBManager.on().getAllProducts();

        if (selectedProducts.isEmpty()) {
            binding.priceLayout.setVisibility(View.GONE);
            return;
        }
        binding.priceLayout.setVisibility(View.VISIBLE);
        binding.tvCount.setText("" + getItemCount());
        binding.tvPrice.setText(getTotalPrice());
    }

    private List<String> getSelectedItemIds() {
        //selectedProducts = MenuPrefUtil.getSaveItems();
        selectedProducts = DBManager.on().getAllProducts();
        List<String> list = new ArrayList<>();
        for (MyCartProduct item : selectedProducts) {
            list.add(item.getProductId());
        }
        return list;
    }

    private int getItemCount() {
        int totalCount = 0;
        for (MyCartProduct item : selectedProducts) {
            totalCount = totalCount + item.getItemCount();
        }
        return totalCount;
    }

    private String getTotalPrice() {
        double total = 0.0;
        for (MyCartProduct item : selectedProducts) {
            total = total + (Double.valueOf(item.getPriceHt()) * item.getItemCount());
        }

        List<CrossSellingSelectedItem> sellingSelectedItems = DBManager.on().getAllCrossSellingItems();

        Log.e("Side_products", "Products count =" + sellingSelectedItems.size());
        for (CrossSellingSelectedItem item : sellingSelectedItems) {
            if (!item.isFree()) {
                total = total + Double.valueOf(item.getProductPrice());
            }
        }
        return Utils.getDecimalFormat(total) + " €";
    }

}
