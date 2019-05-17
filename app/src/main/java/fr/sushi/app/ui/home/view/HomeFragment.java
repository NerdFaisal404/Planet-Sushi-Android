package fr.sushi.app.ui.home.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.databinding.FramentHomeBinding;
import fr.sushi.app.ui.adressPicker.AddressPickerActivity;
import fr.sushi.app.ui.adressPicker.bottom.AddressNameAdapter;
import fr.sushi.app.ui.adressPicker.bottom.SliderLayoutManager;
import fr.sushi.app.ui.adressPicker.bottom.WheelTimeAdapter;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.createaccount.CreateAccountActivity;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.home.data.HomeConfigurationData;
import fr.sushi.app.ui.home.data.HomeSlidesItem;
import fr.sushi.app.ui.home.viewmodel.HomeViewModel;
import fr.sushi.app.ui.login.LoginActivity;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.HandlerUtil;
import fr.sushi.app.util.PicassoUtil;
import fr.sushi.app.util.ScheduleParser;
import fr.sushi.app.util.ScreenUtil;
import fr.sushi.app.util.Utils;

public class HomeFragment extends BaseFragment {
    private Button buttonAddAddres;
    private BottomSheetDialog dialog;
    private LinearLayout linearLayoutAddress;
    private View viewDivider;
    private final int PALACE_SEARCH_ACTION = 100;
    private FramentHomeBinding binding;
    private HomeViewModel mHomeViewModel;
    private RestuarentsResponse restuarentsResponse;

    private HomeConfigurationData homeConfigurationData;
    private volatile int nextImageIndex = 0;
    private String imageBaseUrl;
    private List<HomeSlidesItem> homeSlidesItemList;

    private List<SearchPlace> recentSearchPlace;

    private List<CategoriesItem> categoriesItems = new ArrayList<>();

    private ErrorResponse errorResponse;
    private AddressResponse addressResponse;

    private SearchPlace currentSearchPlace;

    @Override
    protected int getLayoutId() {
        return R.layout.frament_home;
    }

    @Override
    protected void startUI() {
        binding = (FramentHomeBinding) getViewDataBinding();
        binding.layoutAddress.setOnClickListener(this::onClick);
        binding.addressOne.setOnClickListener(this::onClick);
        binding.addressOneTwo.setOnClickListener(this::onClick);
        observeData();

        initListener();
        applyAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SharedPref.readBoolean(PrefKey.IS_LOGINED, false)) {
            binding.layoutSignup.setVisibility(View.GONE);
            binding.layoutRecentAddress.setVisibility(View.VISIBLE);
        } else {
            binding.layoutSignup.setVisibility(View.VISIBLE);
            binding.layoutRecentAddress.setVisibility(View.GONE);
        }

        recentSearchPlace = PlaceUtil.getSearchPlace();

        if (!recentSearchPlace.isEmpty()) {

            if (recentSearchPlace.size() == 1) {
                binding.addressOne.setVisibility(View.VISIBLE);
                binding.addressOneTwo.setVisibility(View.GONE);
                binding.viewSingle.setVisibility(View.GONE);

                SearchPlace place = recentSearchPlace.get(0);
                binding.recentAddrTv.setText(place.getPostalCode() + " " + place.getCity());
                binding.tvAddresTwo.setText(place.getAddress());

            } else {
                binding.addressOne.setVisibility(View.VISIBLE);
                binding.addressOneTwo.setVisibility(View.VISIBLE);
                binding.viewSingle.setVisibility(View.VISIBLE);

                SearchPlace place = recentSearchPlace.get(0);
                binding.recentAddrTv.setText(place.getPostalCode() + " " + place.getCity());
                binding.tvAddresTwo.setText(place.getAddress());


                SearchPlace place2 = recentSearchPlace.get(1);
                if (place.getAddress().equalsIgnoreCase(place2.getAddress())) {
                    binding.viewSingle.setVisibility(View.GONE);
                    binding.addressOneTwo.setVisibility(View.GONE);
                } else {
                    binding.recentAddrTvTwo.setText(place2.getPostalCode() + " " + place2.getCity());
                    binding.tvAddresTwoText.setText(place2.getAddress());
                }


            }
        } else {
            binding.addressOne.setVisibility(View.GONE);
            binding.viewSingle.setVisibility(View.GONE);
            binding.addressOneTwo.setVisibility(View.GONE);
        }

        boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
        boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
        if (isLivarsion) {
            showImporter();
        } else if (isExporter) {
            showExporter();
        }
    }

    private void initListener() {
        binding.layoutRegistraion.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                CreateAccountActivity.class)));
        binding.layoutLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                LoginActivity.class)));
        binding.tvDelivery.setOnClickListener(v -> {
            showBottomSheet();
        });
    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // findViews(view);
    }

    private void observeData() {

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        mHomeViewModel.getRestuarentListMutableLiveData().observe(this, restuarentsResponse -> {
            this.restuarentsResponse = restuarentsResponse;
            //Log.e("Configdata", "config =" + restuarentsResponse.getResponse().size());
        });

        mHomeViewModel.getHomeConfigLiveData().observe(this, homeConfigResponse -> {
            //Log.e("Configdata", "config =" + homeConfigResponse.getResponse().getImgBaseUrl());
            this.homeConfigurationData = homeConfigResponse;
            this.imageBaseUrl = homeConfigResponse.getResponse().getImgBaseUrl();
            this.homeSlidesItemList = homeConfigResponse.getResponse().getHomeSlides();
            showImageWithDelay();
        });

        mHomeViewModel.getShopList();
        mHomeViewModel.getHomeConfigData();
        mHomeViewModel.getFoodMenu();

        mHomeViewModel.getFoodMenuListMutableLiveData().observe(this, foodMenuResponse -> {
            //this.foodMenuResponse = foodMenuResponse;
            categoriesItems = foodMenuResponse.getResponse().getCategories();
            CommonUtility.currentMenuResponse = foodMenuResponse;
            DataCacheUtil.addCategoryItemInCache(categoriesItems);
        });

        mHomeViewModel.getDeliveryAddressLiveData().observe(this, response -> {

            if (response != null) {
                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));

                    Log.e("JsonObject", "" + responseObject.toString());
                    if (error == true) {
                        DialogUtils.hideDialog();
                        errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(getActivity(), "Error!", "Nous sommes desole, Planet Sushi ne delivre actuellement pas cette zone.");

                    } else {
                        addressResponse = new Gson().fromJson(responseObject.toString(), AddressResponse.class);
                        addressResponse = ScheduleParser.parseSchedule(responseObject, addressResponse);
                        Log.e("Order_item", "List size =" + addressResponse.getResponse().getSchedules().getOrderList().size());
                        prepareDataForBottomSheet();
                        if (currentSearchPlace != null) {
                            currentSearchPlace.setTitle(selectedTitle);
                            currentSearchPlace.setTime(selectedTime);
                            currentSearchPlace.setType(binding.tvDelivery.getText().toString());
                            PlaceUtil.saveCurrentPlace(currentSearchPlace);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void applyAnimation() {
        if (Utils.isOneTimeLaunched()) {
            ViewAnimator
                    .animate(binding.tvDelivery)
                    .translationY(200, 0)
                    .alpha(0, 1)
                    .duration(1000)
                    .start();
            Utils.setOneTimeLaunched(false);
        }
    }

    private void showImageWithDelay() {

        if (homeSlidesItemList == null) return;
        String path = imageBaseUrl + homeSlidesItemList.get(nextImageIndex).getPicture();
        //Log.e("Configdata", "path =" + path);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PicassoUtil.loadImage(path, binding.topLayout);
            }
        });

        if (homeSlidesItemList.size() > 1) {
            HandlerUtil.postBackground(delayRunnable, 5000);
        }
    }

    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            nextImageIndex++;
            if (nextImageIndex >= homeSlidesItemList.size()) {
                nextImageIndex = 0;
            }
            showImageWithDelay();

        }
    };


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.layoutAddress:
                startActivityForResult(new Intent(getActivity(), AddressPickerActivity.class), PALACE_SEARCH_ACTION);
                getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);
                break;
            case R.id.addressOne:
                //Toast.makeText(getActivity(),"Item 1", Toast.LENGTH_SHORT).show();
                DialogUtils.showDialog(getActivity());
                currentSearchPlace = new SearchPlace(recentSearchPlace.get(0).getPostalCode(), recentSearchPlace.get(0).getCity(), recentSearchPlace.get(0).getAddress());
                mHomeViewModel.setDeliveryAddress(recentSearchPlace.get(0).getAddress(), recentSearchPlace.get(0).getPostalCode(), recentSearchPlace.get(0).getCity());
               /* Intent intent = new Intent(getActivity(), MenuDetailsActivity.class);
                intent.putExtra(SearchPlace.class.getName(),recentSearchPlace.get(0));
                startActivity(intent);*/
                break;
            case R.id.addressOneTwo:
                //Toast.makeText(getActivity(),"Item 2", Toast.LENGTH_SHORT).show();
                DialogUtils.showDialog(getActivity());
                currentSearchPlace = new SearchPlace(recentSearchPlace.get(1).getPostalCode(), recentSearchPlace.get(1).getCity(), recentSearchPlace.get(1).getAddress());
                mHomeViewModel.setDeliveryAddress(recentSearchPlace.get(1).getAddress(), recentSearchPlace.get(1).getPostalCode(), recentSearchPlace.get(1).getCity());
              /*  intent = new Intent(getActivity(), MenuDetailsActivity.class);
                intent.putExtra(SearchPlace.class.getName(),recentSearchPlace.get(1));
                startActivity(intent);*/
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    private void showBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_liversion_botton_sheet, null);

        LinearLayout liversionView = bottomSheet.findViewById(R.id.livrasion);
        LinearLayout aemporterView = bottomSheet.findViewById(R.id.aemporter);
        liversionView.setOnClickListener(view -> {
            dialog.dismiss();
            showImporter();
            Intent intent = new Intent(getActivity(), AddressPickerActivity.class);
            SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, true);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, false);
            startActivity(intent);

        });

        aemporterView.setOnClickListener(view -> {
            dialog.dismiss();
            showExporter();
            Intent intent = new Intent(getActivity(), AddressPickerActivity.class);
            SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, false);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, true);
            startActivity(intent);
        });

        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showImporter() {
        binding.tvDelivery.setText("Livraison");
        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_scooter_white);
        Drawable rightImage = getResources().getDrawable(R.drawable.ic_white_down_arrow);
        binding.tvDelivery.setCompoundDrawablesWithIntrinsicBounds(img, null, rightImage, null);
    }

    private void showExporter() {
        binding.tvDelivery.setText("A emporter");
        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_emporter_white);
        Drawable rightImage = getResources().getDrawable(R.drawable.ic_white_down_arrow);
        binding.tvDelivery.setCompoundDrawablesWithIntrinsicBounds(img, null, rightImage, null);
    }

    private Map<String, List<String>> scheduleOrderMap = new HashMap<>();

    private void prepareDataForBottomSheet() {
        List<Order> schedulesList = addressResponse.getResponse().getSchedules().getOrderList();
        for (Order item : schedulesList) {
            String[] displayValue = item.getDisplayValue().split(" ");

            List<String> existList = scheduleOrderMap.get(displayValue[0]);

            Log.e("Orders", "value =" + item.getDisplayValue() + " time =" + item.getSchedule());

            if (existList == null) {
                List<String> newList = new ArrayList<>();
                newList.add(item.getSchedule());
                scheduleOrderMap.put(displayValue[0], newList);
            } else {
                existList.add(item.getSchedule());
            }


        }
        showSavedAddressBottomSheet();
    }

    private AddressNameAdapter addressNameAdapter;
    private WheelTimeAdapter wheelTimeAdapter;
    private RecyclerView titleRv, timeRv;
    private String selectedTitle, selectedTime;

    void showSavedAddressBottomSheet() {
        DialogUtils.hideDialog();
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_item_bottom_sheet_time_picker, null);
        titleRv = bottomSheet.findViewById(R.id.rv_horizontal_picker);
        timeRv = bottomSheet.findViewById(R.id.rv_time_picker);
        TextView tvValider = bottomSheet.findViewById(R.id.tvValider);
        TextView tvClose = bottomSheet.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(v -> dialog.dismiss());
        int padding = ScreenUtil.getScreenWidth(getActivity()) / 2 - ScreenUtil.dpToPx(getActivity(), 40);
        titleRv.setPadding(padding, 0, padding, 0);
        SliderLayoutManager sliderLayoutManager = new SliderLayoutManager(getActivity());

        //Title adapter
        List<String> data = new ArrayList<>(scheduleOrderMap.keySet());

        selectedTitle = data.get(0);

        addressNameAdapter = new AddressNameAdapter(getActivity(), data);
        sliderLayoutManager.initListener(new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                addressNameAdapter.setSelectedPosition(position);
                Log.e("Selected_item", "Selected title =" + data.get(position));
                List<String> timeList = scheduleOrderMap.get(data.get(position));

                wheelTimeAdapter.setNewDataList(timeList);
                String title = addressNameAdapter.getItem(position);
                selectedTime = timeList.get(0);
                selectedTitle = title;
            }
        });
        addressNameAdapter.setListener((position, item) -> titleRv.smoothScrollToPosition(position));

        titleRv.setLayoutManager(sliderLayoutManager);
        titleRv.setAdapter(addressNameAdapter);


        tvValider.setOnClickListener(v -> {
            currentSearchPlace.setTitle(selectedTitle);
            currentSearchPlace.setTime(selectedTime);
            currentSearchPlace.setType(binding.tvDelivery.getText().toString());
            PlaceUtil.saveCurrentPlace(currentSearchPlace);
            Intent intent = new Intent(getActivity(),
                    MenuDetailsActivity.class);
            intent.putExtra(SearchPlace.class.getName(), currentSearchPlace);
            startActivity(intent);
            dialog.dismiss();
        });

        //Wheel time adapter

        timeRv.setPadding(padding, 0, padding, 0);
        SliderLayoutManager timeSliderLayoutManger = new SliderLayoutManager(getActivity());

        List<String> timeList = scheduleOrderMap.get(data.get(0));
        selectedTime = timeList.get(0);
        wheelTimeAdapter = new WheelTimeAdapter(getActivity(), timeList);
        timeSliderLayoutManger.initListener(new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                wheelTimeAdapter.setSelectedPosition(position);
                String time = wheelTimeAdapter.getSelectedTime(position);
                selectedTime = time;
            }
        });
        wheelTimeAdapter.setListener(new WheelTimeAdapter.Listener() {
            @Override
            public void onItemClick(int position, String item) {
                timeRv.smoothScrollToPosition(position);
            }
        });
        timeRv.setLayoutManager(timeSliderLayoutManger);
        timeRv.setAdapter(wheelTimeAdapter);


        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

}
