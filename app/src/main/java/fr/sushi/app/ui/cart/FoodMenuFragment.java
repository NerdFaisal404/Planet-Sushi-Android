package fr.sushi.app.ui.cart;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.databinding.FragmentCartBinding;
import fr.sushi.app.ui.adressPicker.AddressPickerActivity;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.base.ItemClickListener;
import fr.sushi.app.ui.cart.adapter.AddressAdapter;
import fr.sushi.app.ui.cart.adapter.FoodMenuAdapterFocus;
import fr.sushi.app.ui.cart.viewmodel.FoodMenuViewModel;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.focuslib.FocusResizeScrollListener;
import okhttp3.ResponseBody;

public class FoodMenuFragment extends BaseFragment implements FoodMenuAdapter.Listener {
    List<String> dummyData = new ArrayList<>();
    private FragmentCartBinding binding;
    private FoodMenuViewModel foodMenuViewModel;
    private List<CategoriesItem> categoriesItems;

    private FoodMenuAdapter foodMenuAdapter;
    //private FoodMenuAdapterFocus foodMenuAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart;
    }


    @Override
    protected void startUI() {
        binding = (FragmentCartBinding) getViewDataBinding();
        observeData();

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

            binding.tvDeliveryInfo.setText("prévue pour " + recentSearchPlace.getTime());

        }

        binding.layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showBottomDialog();
                Intent intent = new Intent(getActivity(), AddressPickerActivity.class);
                boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
                boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
                if (isLivarsion) {
                    intent.putExtra(IntentKey.KEY_IS_TAKEWAY,false);
                }else if (isExporter){
                    intent.putExtra(IntentKey.KEY_IS_TAKEWAY,true);
                }
                intent.putExtra(IntentKey.KEY_FROM_FOOD_CATEGORY, true);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
            //binding.layoutSignup.setVisibility(View.GONE);
            binding.layoutAddress.setVisibility(View.VISIBLE);
        }else {
            binding.layoutAddress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void stopUI() {

    }

    private void observeData() {

        foodMenuViewModel = ViewModelProviders.of(this).get(FoodMenuViewModel.class);
        foodMenuViewModel.getFoodMenu();

        foodMenuViewModel.getFoodMenuListMutableLiveData().observe(this, foodMenuResponse -> {
            //this.foodMenuResponse = foodMenuResponse;
            CommonUtility.currentMenuResponse = foodMenuResponse;
            setAdapter();
            categoriesItems = foodMenuResponse.getResponse().getCategories();
            DataCacheUtil.addCategoryItemInCache(categoriesItems);
            foodMenuAdapter.swapData(categoriesItems);
            binding.progressCircular.setVisibility(View.GONE);
            //Log.e("Food_menu","Count =="+foodMenuResponse.getResponse().getCategories().size());

        });


    }

    void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.featuredRecyclerView.setLayoutManager(linearLayoutManager);
        foodMenuAdapter = new FoodMenuAdapter(getActivity(), this, (int) getResources().getDimension(R.dimen.custom_item_height));
        //  binding.featuredRecyclerView.setHasFixedSize(true);
        binding.featuredRecyclerView.setAdapter(foodMenuAdapter);
        binding.featuredRecyclerView.addOnScrollListener(new FocusResizeScrollListener<>(foodMenuAdapter, linearLayoutManager));
    }

    @Override
    public void onItemClicked(int index, CategoriesItem categoriesItem) {
        Intent intent = new Intent(getActivity(), MenuDetailsActivity.class);
        intent.putExtra("index", index);
        //intent.putExtra("items",(ArrayList)categoriesItems);
        startActivity(intent);
    }

}
