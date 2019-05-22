package fr.sushi.app.ui.checkout.paiement;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.FragmentPaiementBinding;
import fr.sushi.app.ui.adressPicker.AddressPickerActivity;
import fr.sushi.app.ui.adressPicker.bottom.AddressNameAdapter;
import fr.sushi.app.ui.adressPicker.bottom.SliderLayoutManager;
import fr.sushi.app.ui.adressPicker.bottom.WheelTimeAdapter;
import fr.sushi.app.ui.checkout.PaymentMethodCheckoutActivity;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MenuDetailsActivity;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.ScheduleParser;
import fr.sushi.app.util.ScreenUtil;
import fr.sushi.app.util.Utils;


public class PaiementFragment extends Fragment implements OnMapReadyCallback {
    private FragmentPaiementBinding binding;
    private GoogleMap mGoogleMap;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    private BottomSheetDialog dialog;
    private ErrorResponse errorResponse;
    private AddressResponse addressResponse;

    private SearchPlace currentSearchPlace;
    private PaimentViewModel paimentViewModel;


    public PaiementFragment() {
        // Required empty public constructor
    }

    private void observeData() {

        paimentViewModel = ViewModelProviders.of(this).get(PaimentViewModel.class);

        paimentViewModel.getDeliveryAddressLiveData().observe(this, response -> {

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
                            currentSearchPlace.setOrder(selectedOrder);
                            currentSearchPlace.setTime(selectedOrder.getSchedule());
                            if (SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false)) {
                                currentSearchPlace.setType("Livraison");
                            } else if (SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false)) {
                                currentSearchPlace.setType("A emporter");
                            } else {
                                currentSearchPlace.setType("Livraison");
                            }

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paiement, container, false);
        View view = binding.getRoot();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressPickerActivity.class));
            }
        });

        observeData();
        initRadioListener();

        binding.tvName.setText(SharedPref.read(PrefKey.USER_NAME, ""));
        binding.tvMobileNo.setText(SharedPref.read(PrefKey.USER_PHONE, ""));

        return view;
    }


    private void initRadioListener() {

        binding.layoutCartPayment.setOnClickListener(v -> {

            binding.radioCart.setChecked(true);
            binding.radioRestaurent.setChecked(false);
            binding.radioLivarsion.setChecked(false);
            PaymentMethodCheckoutActivity.isAdyenSelected = true;
            PaymentMethodCheckoutActivity.isCashPayment = false;
            PaymentMethodCheckoutActivity.isDeliveryPayment = false;

        });

        binding.layoutLivarsion.setOnClickListener(v -> {

            binding.radioLivarsion.setChecked(true);
            binding.radioRestaurent.setChecked(false);
            binding.radioCart.setChecked(false);
            PaymentMethodCheckoutActivity.isAdyenSelected = false;
            PaymentMethodCheckoutActivity.isCashPayment = true;
            PaymentMethodCheckoutActivity.isDeliveryPayment = false;

        });

        binding.layoutRestuarent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDialog();


            }
        });

        binding.layoutAddress.setOnClickListener(v -> {
            DialogUtils.showDialog(getActivity());
            List<SearchPlace> currentSearchPlaces = PlaceUtil.getSearchPlace();
            if (!currentSearchPlaces.isEmpty()) {
                SearchPlace latestSearchPlace = currentSearchPlaces.get(1);
                currentSearchPlace = new SearchPlace(latestSearchPlace.getPostalCode(),
                        latestSearchPlace.getCity(), latestSearchPlace.getAddress(), latestSearchPlace.getLat(), latestSearchPlace.getLng());
                paimentViewModel.setDeliveryAddress(latestSearchPlace.getAddress(), latestSearchPlace.getPostalCode(),
                        latestSearchPlace.getCity());
            }
        });
    }


    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_to_deliveryman, null);

        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView buttonSpecifyAnAmount = bottomSheet.findViewById(R.id.buttonSpecifyAnAmount);
        TextView buttonNoChange = bottomSheet.findViewById(R.id.buttonNoChange);

        buttonSpecifyAnAmount.setOnClickListener(v -> {
            dialog.dismiss();
            showDialogSpecifyAmount();
        });

        buttonNoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                binding.radioRestaurent.setChecked(true);
                binding.radioLivarsion.setChecked(false);
                binding.radioCart.setChecked(false);
                PaymentMethodCheckoutActivity.isAdyenSelected = false;
                PaymentMethodCheckoutActivity.isCashPayment = false;
                PaymentMethodCheckoutActivity.isDeliveryPayment = true;
            }
        });


    }

    private void showDialogSpecifyAmount() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bottomSheet = inflater.inflate(R.layout.bottom_sheet_specify_an_amount, null);

        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        TextView tvClose = bottomSheet.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(getActivity());
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            mGoogleMap = googleMap;
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.custom_style_json));
            if (!success) {
                Log.e("MapFragment", "Style parsing failed.");
            }

            List<SearchPlace> currentSearchPlace = PlaceUtil.getSearchPlace();
            if (!currentSearchPlace.isEmpty()) {
                SearchPlace latestSearchPlace = currentSearchPlace.get(0);
                if (latestSearchPlace.getLat() != 0.0 && latestSearchPlace.getLng() != 0.0) {
                    LatLng latLng = new LatLng(latestSearchPlace.getLat(), latestSearchPlace.getLng());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    //markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    //move map camera
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
            }
            //checkPermissionAndPrepareClient();
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        List<SearchPlace> currentSearchPlace = PlaceUtil.getSearchPlace();
        if (!currentSearchPlace.isEmpty()) {
            SearchPlace latestSearchPlace = currentSearchPlace.get(0);
            binding.tvCountryCode.setText(latestSearchPlace.getPostalCode() + " " + latestSearchPlace.getCity());
            binding.tvAddress.setText(latestSearchPlace.getAddress());
            if (!TextUtils.isEmpty(latestSearchPlace.getTime())) {
                String time = latestSearchPlace.getTime().replace(":", "h");
                binding.tvDeliveryTime.setText("Heure de " + latestSearchPlace.getType());
                binding.tvTime.setText(time);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

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
        tvClose.setOnClickListener(v -> dialog.dismiss());
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
            dialog.dismiss();
            currentSearchPlace.setOrder(selectedOrder);
            currentSearchPlace.setTime(selectedOrder.getSchedule());
            if (SharedPref.readBoolean(PrefKey.IS_LIBRATION_PRESSED, false)) {
                currentSearchPlace.setType("Livraison");
            } else if (SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false)) {
                currentSearchPlace.setType("A emporter");
            } else {
                currentSearchPlace.setType("Livraison");
            }
            onResume();
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


        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        dialog.setContentView(bottomSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


}
