package fr.sushi.app.ui.cart;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.library.FocusResizeScrollListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.databinding.FragmentCartBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.cart.adapter.FoodMenuAdapterFocus;
import fr.sushi.app.ui.cart.viewmodel.FoodMenuViewModel;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.util.DataCacheUtil;

public class FoodMenuFragment extends BaseFragment implements FoodMenuAdapterFocus.Listener {
    List<String> dummyData = new ArrayList<>();
    private FragmentCartBinding binding;
    private FoodMenuViewModel foodMenuViewModel;
    private List<CategoriesItem> categoriesItems;

    //private FoodMenuAdapter foodMenuAdapter;
    private FoodMenuAdapterFocus foodMenuAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart;
    }


    @Override
    protected void startUI() {
        binding = (FragmentCartBinding) getViewDataBinding();
        observeData();

        Date currentTime = Calendar.getInstance().getTime();

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

        List<SearchPlace> recentSearchPlace = PlaceUtil.getSearchPlace();
        if (!recentSearchPlace.isEmpty()) {
            SearchPlace place = recentSearchPlace.get(0);
            binding.tvLocationInfo.setText(place.getAddress() + "-" + place.getCity() + ", " + place.getPostalCode());
            binding.tvDeliveryInfo.setText("prévue pour " + place.getTime());

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
        foodMenuAdapter = new FoodMenuAdapterFocus(getActivity(), this, (int) getResources().getDimension(R.dimen.custom_item_height));
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
