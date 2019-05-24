package fr.sushi.app.ui.home.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import com.ligl.android.widget.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.CommonUtility;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.databinding.FramentHomeBinding;
import fr.sushi.app.ui.adressPicker.AddressPickerActivity;
import fr.sushi.app.ui.adressPicker.bottom.AddressNameAdapter;
import fr.sushi.app.ui.adressPicker.bottom.SliderLayoutManager;
import fr.sushi.app.ui.adressPicker.bottom.WheelTimeAdapter;
import fr.sushi.app.ui.base.BaseFragment;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.home.data.HomeConfigurationData;
import fr.sushi.app.ui.home.data.HomeSlidesItem;
import fr.sushi.app.ui.home.viewmodel.HomeViewModel;
import fr.sushi.app.ui.main.MainActivity;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.HandlerUtil;
import fr.sushi.app.util.PicassoUtil;
import fr.sushi.app.util.ScheduleParser;
import fr.sushi.app.util.ScreenUtil;
import fr.sushi.app.util.Utils;
import okhttp3.ResponseBody;

public class HomeFragment extends BaseFragment {
    private Button buttonAddAddres;
    // private BottomSheetDialog dialog;
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

    private List<CategoriesItem> categoriesItems = new ArrayList<>();

    private ErrorResponse errorResponse;
    private AddressResponse addressResponse;

    private SearchPlace currentSearchPlace;

    private boolean isDeafultAddressPress;

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
            //binding.layoutSignup.setVisibility(View.GONE);
            binding.layoutWithoutLogin.setVisibility(View.GONE);
            binding.layoutRecentAddress.setVisibility(View.VISIBLE);

            SearchPlace recentSearchPlace = PlaceUtil.getRecentSearchAddress();
            SearchPlace defaultSearchAddress = PlaceUtil.getDefaultSearchAddress();


            if (defaultSearchAddress != null) {
                binding.addressOne.setVisibility(View.VISIBLE);
                binding.recentAddrTv.setText(defaultSearchAddress.getPostalCode() + " " + defaultSearchAddress.getCity());
                binding.tvAddresTwo.setText(defaultSearchAddress.getAddress());

            } else {
                binding.addressOne.setVisibility(View.GONE);
                binding.viewSingle.setVisibility(View.GONE);
            }

            if (recentSearchPlace != null) {
                binding.addressOneTwo.setVisibility(View.VISIBLE);
                binding.recentAddrTvTwo.setText(recentSearchPlace.getPostalCode() + " " + recentSearchPlace.getCity());
                binding.tvAddresTwoText.setText(recentSearchPlace.getAddress());
                if (recentSearchPlace != null) {
                    binding.viewSingle.setVisibility(View.VISIBLE);
                } else {
                    binding.viewSingle.setVisibility(View.GONE);
                }

            } else {
                binding.viewSingle.setVisibility(View.GONE);
                binding.addressOneTwo.setVisibility(View.GONE);
            }

            if (recentSearchPlace != null && defaultSearchAddress != null) {
                if (recentSearchPlace.getAddress().equalsIgnoreCase(defaultSearchAddress.getAddress())) {
                    binding.viewSingle.setVisibility(View.GONE);
                    binding.addressOneTwo.setVisibility(View.GONE);
                } else {
                    binding.addressOneTwo.setVisibility(View.VISIBLE);
                    binding.viewSingle.setVisibility(View.VISIBLE);
                }
            }

        } else {
            binding.layoutWithoutLogin.setVisibility(View.VISIBLE);
            // binding.layoutSignup.setVisibility(View.VISIBLE);
            binding.layoutRecentAddress.setVisibility(View.GONE);

        }


        boolean isLivarsion = SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false);
        boolean isExporter = SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false);
        if (isLivarsion) {
            showImporter();
            binding.destinationTv.setText("Choisir une adresse");
        } else if (isExporter) {
            showExporter();
            binding.destinationTv.setText("Chercher un restaurant");
        } else {
            binding.destinationTv.setText("Choisir une adresse");
        }
    }

    private void initListener() {
       /* binding.layoutRegistraion.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                CreateAccountActivity.class)));
        binding.layoutLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                LoginActivity.class)));*/
        binding.tvDelivery.setOnClickListener(v -> {
            showBottomSheet();
        });

        binding.layoutWithoutLogin.setOnClickListener(v -> {
            ((MainActivity) getActivity()).openRegistrationFragment();
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
            String path = imageBaseUrl + homeSlidesItemList.get(0).getPicture();

            PicassoUtil.loadImage(path, binding.topLayout);

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
                            //currentSearchPlace.setTitle(selectedTitle);
                            //currentSearchPlace.setTime(selectedTime);
                            currentSearchPlace.setOrder(selectedOrder);
                            currentSearchPlace.setTime(selectedOrder.getSchedule());
                            currentSearchPlace.setType(binding.tvDelivery.getText().toString());
                            if (isDeafultAddressPress) {
                                PlaceUtil.saveDefaultSearchPlace(currentSearchPlace);
                            } else {
                                PlaceUtil.saveRecentSearchAddress(currentSearchPlace);
                            }

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

                new IOSDialog.Builder(getActivity())
                        .setTitle("Voulez-vous changer d'adresse ?")
                        .setMessage("En changeant d'adresse, votre panier actuel va devoir être vidé")
                        .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DBManager.on().clearMyCartProduct();
                                DataCacheUtil.removeSideProducts();
                                isDeafultAddressPress = true;
                                DialogUtils.showDialog(getActivity());
                                SearchPlace searchPlace = PlaceUtil.getDefaultSearchAddress();
                                currentSearchPlace = new SearchPlace(searchPlace.getPostalCode(),
                                        searchPlace.getCity(), searchPlace.getAddress(), searchPlace.getLat(), searchPlace.getLng());
                                mHomeViewModel.setDeliveryAddress(searchPlace.getAddress(), searchPlace.getPostalCode(),
                                        searchPlace.getCity());
                            }
                        })
                        .setNegativeButton("Annuler ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


                break;
            case R.id.addressOneTwo:
                //Toast.makeText(getActivity(),"Item 2", Toast.LENGTH_SHORT).show();
                new IOSDialog.Builder(getActivity())
                        .setTitle("Voulez-vous changer d'adresse ?")
                        .setMessage("En changeant d'adresse, votre panier actuel va devoir être vidé")
                        .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DBManager.on().clearMyCartProduct();
                                DataCacheUtil.removeSideProducts();
                                isDeafultAddressPress = false;
                                DialogUtils.showDialog(getActivity());
                                SearchPlace searchPlace = PlaceUtil.getRecentSearchAddress();
                                currentSearchPlace = new SearchPlace(searchPlace.getPostalCode(), searchPlace.getCity(),
                                        searchPlace.getAddress(), searchPlace.getLat(), searchPlace.getLng());
                                mHomeViewModel.setDeliveryAddress(searchPlace.getAddress(), searchPlace.getPostalCode(),
                                        searchPlace.getCity());
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    private void showBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_liversion_botton_sheet, null);

        //binding.bottomsheet.setContentView(bottomSheet);

        LinearLayout liversionView = bottomSheet.findViewById(R.id.livrasion);
        LinearLayout aemporterView = bottomSheet.findViewById(R.id.aemporter);
        liversionView.setOnClickListener(view -> {
            binding.bottomsheet.dismissSheet();
            showImporter();
            Intent intent = new Intent(getActivity(), AddressPickerActivity.class);
            SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, true);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);


        });

        aemporterView.setOnClickListener(view -> {
            binding.bottomsheet.dismissSheet();
            showExporter();
            Intent intent = new Intent(getActivity(), AddressPickerActivity.class);
            SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, false);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, true);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.blank);

        });

       /* dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();*/
        binding.bottomsheet.showWithSheetView(bottomSheet);
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

    private Map<String, List<Order>> scheduleOrderMap = new TreeMap<>();

    private void prepareDataForBottomSheet() {
        List<Order> schedulesList = addressResponse.getResponse().getSchedules().getOrderList();
        for (Order item : schedulesList) {
            String[] displayValue = item.getDisplayValue().split("à");

            List<Order> existList = scheduleOrderMap.get(displayValue[0]);

            Log.e("Orders", "value =" + item.getDisplayValue() + " time =" + item.getSchedule());

            if (existList == null) {
                List<Order> newList = new ArrayList<>();
                newList.add(item);
                scheduleOrderMap.put(displayValue[0], newList);
            } else {
                existList.add(item);
            }


        }
        showSavedAddressBottomSheet();
    }

    private AddressNameAdapter addressNameAdapter;
    private WheelTimeAdapter wheelTimeAdapter;
    private RecyclerView titleRv, timeRv;
    private Order selectedOrder;

    void showSavedAddressBottomSheet() {

        DialogUtils.hideDialog();
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_item_bottom_sheet_time_picker, null);
        titleRv = bottomSheet.findViewById(R.id.rv_horizontal_picker);
        timeRv = bottomSheet.findViewById(R.id.rv_time_picker);
        TextView tvValider = bottomSheet.findViewById(R.id.tvValider);
        TextView tvClose = bottomSheet.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(v -> binding.bottomsheet.dismissSheet());
        int padding = ScreenUtil.getScreenWidth(getActivity()) / 2 - ScreenUtil.dpToPx(getActivity(), 40);
        titleRv.setPadding(padding, 0, padding, 0);
        SliderLayoutManager sliderLayoutManager = new SliderLayoutManager(getActivity());

        //Title adapter
        List<String> data = new ArrayList<>(scheduleOrderMap.keySet());


        addressNameAdapter = new AddressNameAdapter(getActivity(), data);
        sliderLayoutManager.initListener(new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                addressNameAdapter.setSelectedPosition(position);
                Log.e("Selected_item", "Selected title =" + data.get(position));
                List<Order> timeList = scheduleOrderMap.get(data.get(position));

                wheelTimeAdapter.setNewDataList(timeList);
                selectedOrder = timeList.get(0);

            }
        });
        addressNameAdapter.setListener((position, item) -> titleRv.smoothScrollToPosition(position));

        titleRv.setLayoutManager(sliderLayoutManager);
        titleRv.setAdapter(addressNameAdapter);


        tvValider.setOnClickListener(v -> {
            //currentSearchPlace.setTitle(selectedTitle);
            //currentSearchPlace.setTime(selectedTime);
            binding.bottomsheet.dismissSheet();
            currentSearchPlace.setOrder(selectedOrder);
            currentSearchPlace.setTime(selectedOrder.getSchedule());
            currentSearchPlace.setType(binding.tvDelivery.getText().toString());
            PlaceUtil.saveRecentSearchAddress(currentSearchPlace);
            Intent intent = new Intent(getActivity(),
                    MenuDetailsActivity.class);
            intent.putExtra(SearchPlace.class.getName(), currentSearchPlace);
            // startActivity(intent);
            // we are calling store address
            DialogUtils.showDialog(getActivity());
            mHomeViewModel.getStoreProducts(selectedOrder.getStoreId());
            mHomeViewModel.getStoreProductLiveData().observe(this, new Observer<ResponseBody>() {
                @Override
                public void onChanged(@Nullable ResponseBody responseBody) {
                    DialogUtils.hideDialog();
                    try {
                        String response = responseBody.string();
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = Boolean.parseBoolean(jsonObject.getString("error"));
                        if (error == true) {
                            DialogUtils.hideDialog();
                            errorResponse = new Gson().fromJson(response.toString(), ErrorResponse.class);
                            Utils.showAlert(getActivity(), "Error!", "Nous sommes desole, Planet Sushi ne delivre actuellement pas cette zone.");

                        } else {

                            JSONArray productArray = jsonObject.getJSONArray("response");
                            adjustItemList(productArray);

                            startActivity(intent);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        });


        //Wheel time adapter

        timeRv.setPadding(padding, 0, padding, 0);
        SliderLayoutManager timeSliderLayoutManger = new SliderLayoutManager(getActivity());

        List<Order> timeList = scheduleOrderMap.get(data.get(0));
        selectedOrder = timeList.get(0);
        wheelTimeAdapter = new WheelTimeAdapter(getActivity(), timeList);
        timeSliderLayoutManger.initListener(new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                wheelTimeAdapter.setSelectedPosition(position);
                selectedOrder = wheelTimeAdapter.getSelectedOrder(position);

            }
        });
        wheelTimeAdapter.setListener(new WheelTimeAdapter.Listener() {
            @Override
            public void onItemClick(int position, Order item) {
                timeRv.smoothScrollToPosition(position);
            }
        });
        timeRv.setLayoutManager(timeSliderLayoutManger);
        timeRv.setAdapter(wheelTimeAdapter);

        binding.bottomsheet.showWithSheetView(bottomSheet);

      /*  dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();*/

    }

    private void adjustItemList(JSONArray itemArray) {
        if (itemArray.length() > 0) {
            List<CategoriesItem> categoryList = DataCacheUtil.getCategoryItemFromCache();
            for (int i = 0; i < itemArray.length(); i++) {
                try {
                    JSONObject itemObj = itemArray.getJSONObject(i);
                    String productId = itemObj.optString("id_product");
                    String price = itemObj.optString("price_ttc");
                    String activeDelivery = itemObj.optString("active_delivery");

                    for (CategoriesItem categoriesItem : categoryList) {
                        List<ProductsItem> productList = categoriesItem.getProducts();
                        for (ProductsItem product : productList) {
                            if (product.getIdProduct().equals(productId)) {
                                product.setActiveDelivery(activeDelivery);
                                product.setPriceTtc(price);

                                break;
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
