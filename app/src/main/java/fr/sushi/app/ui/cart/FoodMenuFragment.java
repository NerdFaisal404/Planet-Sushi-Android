package fr.sushi.app.ui.cart;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

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

        binding.layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog(true);
            }
        });


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

    void showBottomDialog(boolean showAddressLayout) {

        View bottomSheet = getLayoutInflater().inflate(R.layout.view_bottom_sheet_pickup_delivery, null);
        RadioButton radioButtonLivraison = bottomSheet.findViewById(R.id.radioButtonLivraison);
        RadioButton radioButtonEmporter = bottomSheet.findViewById(R.id.radioButtonEmporter);
        Button buttonAddAddres = bottomSheet.findViewById(R.id.buttonAddAddres);
        TextView textViewModifier = bottomSheet.findViewById(R.id.textViewModifier);
        TextView recentAddress = bottomSheet.findViewById(R.id.recent_addr_tv);
        View viewDivider = bottomSheet.findViewById(R.id.view_divider);
        textViewModifier.setOnClickListener(this);
        LinearLayout linearLayoutAddress = bottomSheet.findViewById(R.id.linearLayoutAddress);
        if (showAddressLayout) {

            viewDivider.setVisibility(View.VISIBLE);
            boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
            boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);

            if (isLivarsion) {
                radioButtonLivraison.setChecked(true);
                radioButtonEmporter.setChecked(false);
            } else if (isExporter) {
                radioButtonLivraison.setChecked(false);
                radioButtonEmporter.setChecked(true);
            }else {
                radioButtonLivraison.setChecked(true);
                radioButtonEmporter.setChecked(false);
            }

            linearLayoutAddress.setVisibility(View.VISIBLE);
            buttonAddAddres.setVisibility(View.VISIBLE);
            buttonAddAddres.setText("VALIDER");
            //recentAddress.setText(PlaceUtil.getCurrentSearchPlace().getAddress());
        } else {
            linearLayoutAddress.setVisibility(View.GONE);
        }

        radioButtonLivraison.setOnClickListener(this);
        radioButtonEmporter.setOnClickListener(this);
        buttonAddAddres.setOnClickListener(this);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }
}
