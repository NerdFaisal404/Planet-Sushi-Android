package fr.sushi.app.ui.adressPicker;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.sushi.app.R;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.databinding.ActivityAdressPickerBinding;
import fr.sushi.app.ui.adressPicker.adapter.PlaceAutocompleteAdapter;
import fr.sushi.app.ui.adressPicker.bottom.AddressNameAdapter;
import fr.sushi.app.ui.adressPicker.bottom.SliderLayoutManager;
import fr.sushi.app.ui.adressPicker.bottom.WheelTimeAdapter;
import fr.sushi.app.ui.menu.SectionedRecyclerViewAdapter;
import fr.sushi.app.util.ScheduleParser;
import fr.sushi.app.util.ScreenUtil;
import fr.sushi.app.util.Utils;

public class AdressPickerActivity extends AppCompatActivity implements
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
    ShopAddressAdapter addressAdapter;
    private boolean isLivarsion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleClient();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adress_picker);
        mGeocoder = new Geocoder(this, Locale.FRANCE);

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

        getIntentValue();
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        isLivarsion = intent.getBooleanExtra(IntentKey.KEY_IS_LIVARSION, false);
        if (isLivarsion) {
            showPlaceAddress();
        } else {
            loadShopAddressList();
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

            if (addressAdapter != null && !isLivarsion) {
                Map<String, List<ResponseItem>> responseItemGroup = prepareGroup();

                List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

                int section = 0;

                for (Map.Entry<String, List<ResponseItem>> items : responseItemGroup.entrySet()) {
                    sections.add(new SectionedRecyclerViewAdapter.Section(section, items.getKey()));
                    section = section + items.getValue().size();
                }

                addressAdapter = new ShopAddressAdapter(this, responseItemList, listener);
                sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(this, R.layout.item_menu_section,
                        R.id.section_tv, binding.recyclerView, addressAdapter);

                binding.recyclerView.setAdapter(sectionedRecyclerViewAdapter);
                setSections(sections);
                addressAdapter.addNewList(responseItemList);
            }
        });


        viewModel.getTakeWayAddressLiveData().observe(this, response -> {
            if (response != null) {

                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                    } else {
                        addressResponse = new Gson().fromJson(responseObject.toString(), AddressResponse.class);
                        addressResponse = ScheduleParser.parseSchedule(responseObject, addressResponse);
                        prepareDataForBottomSheet();
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
                        errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                    } else {
                        addressResponse = new Gson().fromJson(responseObject.toString(), AddressResponse.class);
                        addressResponse = ScheduleParser.parseSchedule(responseObject, addressResponse);
                        Log.e("Order_item", "List size =" + addressResponse.getResponse().getSchedules().getOrderList().size());
                        prepareDataForBottomSheet();
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
            showPlaceAddress();
        });

        aemporterView.setOnClickListener(view -> {
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


        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (i2 > 0) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        binding.recyclerView.setAdapter(mAdapter);
                    }
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
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
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {

        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        try {
                            if (places.getCount() == 1) {
                                LatLng latLng = places.get(0).getLatLng();
                                List<Address> addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                String zipCode = addresses.get(0).getPostalCode();
                                String city = addresses.get(0).getLocality();
                                String address = addresses.get(0).getThoroughfare();
                                Log.e("Place_cliec", "code =" + zipCode);
                                Log.e("Place_cliec", "city =" + city);
                                Log.e("Place_cliec", "address =" + address);
                                viewModel.setDeliveryAddress(address, zipCode, city);
                            } else {
                                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
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

        int section = 0;

        for (Map.Entry<String, List<ResponseItem>> item : responseItemGroup.entrySet()) {
            sections.add(new SectionedRecyclerViewAdapter.Section(section, item.getKey()));
            section = section + item.getValue().size();
        }

        addressAdapter = new ShopAddressAdapter(this, responseItemList, listener);
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
        viewModel.setTakeawayStore(responseItem.getIdStore());
    };

    private Map<String, List<String>> scheduleOrderMap = new HashMap<>();

    private void prepareDataForBottomSheet() {
        List<Order> schedulesList = addressResponse.getResponse().getSchedules().getOrderList();
        for (Order item : schedulesList) {
            String[] displayValue = item.getDisplayValue().split(" ");

            List<String> existList = scheduleOrderMap.get(displayValue[0]);

            Log.e("Orders", "value =" + item.getDisplayValue()+" time ="+item.getSchedule());

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
    void showSavedAddressBottomSheet() {
        View bottomSheet = getLayoutInflater().inflate(R.layout.view_item_bottom_sheet_time_picker, null);
        titleRv = bottomSheet.findViewById(R.id.rv_horizontal_picker);
        timeRv = bottomSheet.findViewById(R.id.rv_time_picker);
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
                wheelTimeAdapter.setNewDataList(scheduleOrderMap.get(data.get(position)));
            }
        });
        addressNameAdapter.setListener(new AddressNameAdapter.Listener() {
            @Override
            public void onItemClick(int position, String item) {
                titleRv.smoothScrollToPosition(position);
            }
        });

        titleRv.setLayoutManager(sliderLayoutManager);
        titleRv.setAdapter(addressNameAdapter);


        //Wheel time adapter

        timeRv.setPadding(padding, 0, padding, 0);
        SliderLayoutManager timeSliderLayoutManger = new SliderLayoutManager(this);

        List<String> timeList = scheduleOrderMap.get(data.get(0));

        wheelTimeAdapter = new WheelTimeAdapter(this, timeList);
        timeSliderLayoutManger.initListener(new SliderLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                wheelTimeAdapter.setSelectedPosition(position);
                String time = wheelTimeAdapter.getSelectedTime(position);
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


        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }



}
