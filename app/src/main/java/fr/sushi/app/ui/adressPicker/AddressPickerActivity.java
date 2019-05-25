package fr.sushi.app.ui.adressPicker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.ligl.android.widget.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.BaseAddress;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.data.model.food_menu.CategoriesItem;
import fr.sushi.app.data.model.food_menu.ProductsItem;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.databinding.ActivityAdressPickerBinding;
import fr.sushi.app.ui.adressPicker.adapter.PlaceAutocompleteAdapter;
import fr.sushi.app.ui.adressPicker.bottom.AddressNameAdapter;
import fr.sushi.app.ui.adressPicker.bottom.SliderLayoutManager;
import fr.sushi.app.ui.adressPicker.bottom.WheelTimeAdapter;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.ui.menu.SectionedRecyclerViewAdapter;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.ScheduleParser;
import fr.sushi.app.util.ScreenUtil;
import fr.sushi.app.util.Utils;
import okhttp3.ResponseBody;

public class AddressPickerActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private GoogleApiClient mGoogleApiClient;
    ActivityAdressPickerBinding binding;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    private PlaceAutocompleteAdapter mAdapter;
    private BottomSheetDialog dialog;
    private AddressPickerViewModel viewModel;
    private List<ResponseItem> responseItemList = new ArrayList<>();
    private Geocoder mGeocoder;
    private ErrorResponse errorResponse;
    private AddressResponse addressResponse;
    private ShopAddressAdapter addressAdapter;
    private boolean isExporter;

    private SearchPlace currentSearchPlace;

    private boolean isFromMenuCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleClient();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adress_picker);
        mGeocoder = new Geocoder(this, Locale.FRANCE);

        parseIntent();

        viewModel = ViewModelProviders.of(this).get(AddressPickerViewModel.class);
        initViewmodel();
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 20, null);
        setUpAdapter();
        binding.tvDelivery.setOnClickListener(v -> {
            //startActivity(new Intent(this, ActivityBottonSheet.class));
            showBottomSheet();
        });
        binding.ivClose.setOnClickListener(v -> {
                    Utils.hideSoftKeyboard(this);
                    finish();
                }
        );

        binding.editTextSearch.requestFocus();
        Utils.showSoftKeyboard(this);

        initIntentValue();
    }

    private void initIntentValue() {
        Intent intent = getIntent();
        isExporter = intent.getBooleanExtra(IntentKey.KEY_IS_TAKEWAY, false);
        if (isExporter) {
            loadShopAddressList();

        } else {
            showPlaceAddress();
        }
    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

    }

    private void initViewmodel() {
        viewModel.getShopList();

        viewModel.getRestuarentListMutableLiveData().observe(this, restuarentsResponse -> {
            //this.responseItemList = restuarentsResponse.getResponse();
            responseItemList.clear();
            Log.e("Configdata", "config =" + restuarentsResponse.getResponse().size());
            for (ResponseItem item : restuarentsResponse.getResponse()) {
                if (TextUtils.isEmpty(item.getCity()) || TextUtils.isEmpty(item.getAddress())) {
                    continue;
                }
                responseItemList.add(item);

            }

            if (isExporter) {
                SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, false);
                SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, true);
            }else {
                SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, true);
                SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, false);
            }

            if (addressAdapter != null && isExporter) {
                Map<String, List<ResponseItem>> responseItemGroup = prepareGroup();

                List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
                List<ResponseItem> sectionsItemList = new ArrayList<>();
                int section = 0;

                for (Map.Entry<String, List<ResponseItem>> items : responseItemGroup.entrySet()) {
                    sections.add(new SectionedRecyclerViewAdapter.Section(section, items.getKey()));
                    section = section + items.getValue().size();
                    sectionsItemList.addAll(items.getValue());
                }

                addressAdapter = new ShopAddressAdapter(this, sectionsItemList, listener);
                sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(this, R.layout.item_menu_section,
                        R.id.section_tv, binding.recyclerView, addressAdapter);

                binding.recyclerView.setAdapter(sectionedRecyclerViewAdapter);
                setSections(sections);
                addressAdapter.addNewList(responseItemList);
            }
        });


        viewModel.getTakeWayAddressLiveData().observe(this, response -> {
            if (response != null) {
                // DialogUtils.hideDialog();
                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        DialogUtils.hideDialog();
                        errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(this, "Erreur!", "Nous sommes desole, Planet Sushi ne delivre actuellement pas cette zone.");

                    } else {
                        addressResponse = new Gson().fromJson(responseObject.toString(), AddressResponse.class);
                        addressResponse = ScheduleParser.parseSchedule(responseObject, addressResponse);
                        prepareDataForBottomSheet();
                        if (currentSearchPlace != null) {
                            //currentSearchPlace.setTitle(selectedTitle);
                            //currentSearchPlace.setTime(selectedTime);
                            currentSearchPlace.setOrder(selectedOrder);
                            currentSearchPlace.setTime(selectedOrder.getSchedule());
                            currentSearchPlace.setType(binding.tvDelivery.getText().toString());
                            PlaceUtil.saveRecentSearchAddress(currentSearchPlace);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        });
        viewModel.getDeliveryAddressLiveData().observe(this, response -> {

            if (response != null) {
                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));

                    Log.e("JsonObject", "" + responseObject.toString());
                    if (error == true) {
                        DialogUtils.hideDialog();
                        errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(this, "Erreur!", "Nous sommes desole, Planet Sushi ne delivre actuellement pas cette zone.");

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
                            PlaceUtil.saveRecentSearchAddress(currentSearchPlace);
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


    private void showBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_liversion_botton_sheet, null);

        LinearLayout liversionView = bottomSheet.findViewById(R.id.livrasion);
        LinearLayout aemporterView = bottomSheet.findViewById(R.id.aemporter);
        liversionView.setOnClickListener(view -> {
           /* SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, true);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, false);*/
            isExporter = false;
            showPlaceAddress();
        });

        aemporterView.setOnClickListener(view -> {
          /*  SharedPref.write(PrefKey.IS_LIBRATION_PRESSED, false);
            SharedPref.write(PrefKey.IS_EMPORTER_PRESSED, true);*/
            isExporter = true;
            loadShopAddressList();
        });

        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showPlaceAddress() {
        if (dialog != null) {
            dialog.dismiss();
        }
        binding.tvDelivery.setText("Livraison");
        Drawable img = getResources().getDrawable(R.drawable.ic_delivery);
        Drawable rightImage = getResources().getDrawable(R.drawable.ic_down_arrow);
        binding.tvDelivery.setCompoundDrawablesWithIntrinsicBounds(img, null, rightImage, null);
        binding.shadowView.setVisibility(View.VISIBLE);
        setUpAdapter();
    }

    private void setUpAdapter() {
        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("FR").build();
        mAdapter = new PlaceAutocompleteAdapter(this,
                mGoogleApiClient, BOUNDS, filter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.bg_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        binding.recyclerView.addItemDecoration(horizontalDecoration);
        binding.recyclerView.setAdapter(mAdapter);
        String addressJson = SharedPref.read(PrefKey.USER_ADDRESS, "");
        List<ProfileAddressModel> addressList = GsonHelper.on().convertJsonToNormalAddress(addressJson);
        ArrayList<BaseAddress> addresses = new ArrayList<>(addressList);
        mAdapter.addItems(addresses);


        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
               /* if (i2 > 0) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        binding.recyclerView.setAdapter(mAdapter);
                    }
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                }*/
                if (mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onPlaceClick(BaseAddress address) {
        if (address != null) {
            if (address instanceof PlaceAutocompleteAdapter.PlaceAutocomplete) {
                try {
                    final String placeId = String.valueOf(((PlaceAutocompleteAdapter.PlaceAutocomplete) address).placeId);
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {

                            new IOSDialog.Builder(AddressPickerActivity.this)
                                    .setTitle("Voulez-vous changer d'adresse ?")
                                    .setMessage("En changeant d'adresse, votre panier actuel va devoir être vidé")
                                    .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            DBManager.on().clearMyCartProduct();
                                            DataCacheUtil.removeSideProducts();
                                            try {
                                                if (places.getCount() == 1) {
                                                    LatLng latLng = places.get(0).getLatLng();
                                                    List<Address> addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                                    String zipCode = addresses.get(0).getPostalCode();
                                                    String city = addresses.get(0).getLocality();
                                                    String address = addresses.get(0).getThoroughfare();
                                                    String featureName = addresses.get(0).getFeatureName();
                                                    Log.e("Place_cliec", "code =" + zipCode);
                                                    Log.e("Place_cliec", "city =" + city);
                                                    Log.e("Place_cliec", "address =" + address);
                                                    DialogUtils.showDialog(AddressPickerActivity.this);
                                                    viewModel.setDeliveryAddress(address, zipCode, city);
                                                    currentSearchPlace = new SearchPlace(zipCode, city, featureName + " " + address, latLng.latitude, latLng.longitude);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (IOException e) {
                                            }
                                        }
                                    })
                                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                new IOSDialog.Builder(AddressPickerActivity.this)
                        .setTitle("Voulez-vous changer d'adresse ?")
                        .setMessage("En changeant d'adresse, votre panier actuel va devoir être vidé")
                        .setPositiveButton("Confirmer ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DBManager.on().clearMyCartProduct();
                                DataCacheUtil.removeSideProducts();
                                ProfileAddressModel model = (ProfileAddressModel) address;
                                viewModel.setDeliveryAddress(model.getLocation(), model.getZipCode(), model.getCity());
                                currentSearchPlace = new SearchPlace(model.getZipCode(), model.getCity(), model.getLocation());
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.blank, R.anim.top_to_bottom);
    }

    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    private void loadShopAddressList() {
        if (dialog != null) {
            dialog.dismiss();
        }
        binding.shadowView.setVisibility(View.GONE);
        binding.tvDelivery.setText("A emporter");
        Drawable img = getResources().getDrawable(R.drawable.ic_pickup);
        Drawable rightImage = getResources().getDrawable(R.drawable.ic_down_arrow);
        binding.tvDelivery.setCompoundDrawablesWithIntrinsicBounds(img, null, rightImage, null);
        Map<String, List<ResponseItem>> responseItemGroup = prepareGroup();

        List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        List<ResponseItem> sectionsItemList = new ArrayList<>();
        int section = 0;

        for (Map.Entry<String, List<ResponseItem>> item : responseItemGroup.entrySet()) {
            sections.add(new SectionedRecyclerViewAdapter.Section(section, item.getKey()));
            section = section + item.getValue().size();
            sectionsItemList.addAll(item.getValue());
        }

        addressAdapter = new ShopAddressAdapter(this, sectionsItemList, listener);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(this, R.layout.item_menu_section,
                R.id.section_tv, binding.recyclerView, addressAdapter);

        binding.recyclerView.setAdapter(sectionedRecyclerViewAdapter);
        setSections(sections);

    }

    private void setSections(List<SectionedRecyclerViewAdapter.Section> sections) {
        SectionedRecyclerViewAdapter.Section[] sectionArray = new SectionedRecyclerViewAdapter.Section[sections.size()];
        sectionedRecyclerViewAdapter.setSections(sections.toArray(sectionArray));

    }

    private Map<String, List<ResponseItem>> prepareGroup() {
        Map<String, List<ResponseItem>> responseItemMap = new HashMap<>();
        for (ResponseItem item : responseItemList) {

            if (TextUtils.isEmpty(item.getCity()) || TextUtils.isEmpty(item.getAddress())) {
                continue;
            }

            Log.e("Prepare-group", "city =" + item.getCity() + " Code=" + item.getPostcode() + " Name ="
                    + item.getName());

            List<ResponseItem> responseItems = responseItemMap.get(item.getCity());
            if (responseItems == null) {
                List<ResponseItem> itemList = new ArrayList<>();
                itemList.add(item);
                responseItemMap.put(item.getCity(), itemList);
            } else {
                responseItems.add(item);
            }
        }
        return responseItemMap;
    }

    private ShopAddressAdapter.Listener listener = responseItem -> {
        DialogUtils.showDialog(this);
        viewModel.setTakeawayStore(responseItem.getIdStore());
        currentSearchPlace = new SearchPlace(responseItem.getPostcode(),
                responseItem.getCity(), responseItem.getAddress(), responseItem.getLat(), responseItem.getLng());
    };

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
        tvClose.setOnClickListener(v -> dialog.dismiss());
        int padding = ScreenUtil.getScreenWidth(this) / 2 - ScreenUtil.dpToPx(this, 40);
        titleRv.setPadding(padding, 0, padding, 0);
        SliderLayoutManager sliderLayoutManager = new SliderLayoutManager(this);

        //Title adapter
        List<String> data = new ArrayList<>(scheduleOrderMap.keySet());

        addressNameAdapter = new AddressNameAdapter(this, data);
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
            currentSearchPlace.setOrder(selectedOrder);
            currentSearchPlace.setTime(selectedOrder.getSchedule());
            currentSearchPlace.setType(binding.tvDelivery.getText().toString());
            PlaceUtil.saveRecentSearchAddress(currentSearchPlace);
            Intent intent = new Intent(AddressPickerActivity.this,
                    MenuDetailsActivity.class);
            intent.putExtra(SearchPlace.class.getName(), currentSearchPlace);

            // we are calling store address
            DialogUtils.showDialog(AddressPickerActivity.this);
            viewModel.getStoreProducts(selectedOrder.getStoreId());
            viewModel.getStoreProductLiveData().observe(this, new Observer<ResponseBody>() {
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
                            Utils.showAlert(AddressPickerActivity.this, "Error!", "Nous sommes desole, Planet Sushi ne delivre actuellement pas cette zone.");

                        } else {

                            JSONArray productArray = jsonObject.getJSONArray("response");
                            adjustItemList(productArray);
                            if (!isFromMenuCat) {
                                startActivity(intent);
                            }
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            dialog.dismiss();

        });

        //Wheel time adapter

        //Wheel time adapter
        int wheelPaddingRight = ScreenUtil.getScreenWidth(this) / 2 - ScreenUtil.dpToPx(this, 15);
        int wheelPaddingLeft = ScreenUtil.getScreenWidth(this) / 2 - ScreenUtil.dpToPx(this, 20);
        timeRv.setPadding(wheelPaddingLeft, 0, wheelPaddingRight, 0);
        SliderLayoutManager timeSliderLayoutManger = new SliderLayoutManager(this);


        List<Order> timeList = scheduleOrderMap.get(data.get(0));
        selectedOrder = timeList.get(0);
        wheelTimeAdapter = new WheelTimeAdapter(this, timeList);
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


        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(IntentKey.KEY_FROM_FOOD_CATEGORY)) {
            isFromMenuCat = intent.getBooleanExtra(IntentKey.KEY_FROM_FOOD_CATEGORY, false);
        }
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
