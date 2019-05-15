package fr.sushi.app.ui.cart;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.databinding.FragmentCartBinding;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.cart.adapter.FoodMenuAdapterFocus;
import fr.sushi.app.ui.cart.viewmodel.FoodMenuViewModel;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.focuslib.FocusResizeScrollListener;

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
        setAdapter();
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
        foodMenuAdapter = new FoodMenuAdapterFocus(getActivity(), this,(int) getResources().getDimension(R.dimen.custom_item_height));
        binding.featuredRecyclerView.setHasFixedSize(true);
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
