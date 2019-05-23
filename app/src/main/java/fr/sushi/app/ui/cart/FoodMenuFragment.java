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
                showBottomDialog();
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

    void showBottomDialog() {

        View bottomSheet = getLayoutInflater().inflate(R.layout.view_bottom_sheet_pickup_delivery, null);
        RadioButton radioButtonLivraison = bottomSheet.findViewById(R.id.radioButtonLivraison);
        RadioButton radioButtonEmporter = bottomSheet.findViewById(R.id.radioButtonEmporter);
        TextView textViewModifier = bottomSheet.findViewById(R.id.textViewModifier);
        TextView tvClose = bottomSheet.findViewById(R.id.tvClose);
        View viewDivider = bottomSheet.findViewById(R.id.view_divider);
        RecyclerView recyclerViewAddress = bottomSheet.findViewById(R.id.rvUserAddress);
        textViewModifier.setOnClickListener(this);


        viewDivider.setVisibility(View.VISIBLE);
        boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
        boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);

        if (isLivarsion) {
            radioButtonLivraison.setChecked(true);
            radioButtonEmporter.setChecked(false);
        } else if (isExporter) {
            radioButtonLivraison.setChecked(false);
            radioButtonEmporter.setChecked(true);
        } else {
            radioButtonLivraison.setChecked(true);
            radioButtonEmporter.setChecked(false);
        }


        //setAddress adapter . Now it is comment out.

        /*recyclerViewAddress.setHasFixedSize(true);
        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
        AddressAdapter addressAdapter = new AddressAdapter();
        recyclerViewAddress.setAdapter(addressAdapter);

        String addressJson = SharedPref.read(PrefKey.USER_ADDRESS, "");
        List<ProfileAddressModel> addressList = GsonHelper.on().convertJsonToNormalAddress(addressJson);
        addressAdapter.clear();
        addressAdapter.addItem(addressList);

        addressAdapter.setItemClickListener((view, item) -> {
            ProfileAddressModel model = (ProfileAddressModel) item;
            // we have to send model in server

            foodMenuViewModel.setDeliveryAddress(model.getLocation(),model.getZipCode(),model.getCity());

            foodMenuViewModel.getDeliveryAddressLiveData().observe(this, new Observer<ResponseBody>() {
                @Override
                public void onChanged(@Nullable ResponseBody responseBody) {

                }
            });
        });*/

        radioButtonLivraison.setOnClickListener(view -> {
            if (radioButtonLivraison.isChecked()) {
                Intent intent = new Intent(getActivity(), AddressPickerActivity.class);
                intent.putExtra(IntentKey.KEY_FROM_FOOD_CATEGORY, true);
                startActivity(intent);
            }
        });
        radioButtonEmporter.setOnClickListener(this);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
