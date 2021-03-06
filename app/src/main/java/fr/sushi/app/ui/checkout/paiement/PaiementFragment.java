package fr.sushi.app.ui.checkout.paiement;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.address_picker.AddressResponse;
import fr.sushi.app.data.model.address_picker.Order;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.data.model.restuarents.RestuarentsResponse;
import fr.sushi.app.databinding.FragmentPaiementBinding;
import fr.sushi.app.ui.adressPicker.bottom.AddressNameAdapter;
import fr.sushi.app.ui.adressPicker.bottom.SliderLayoutManager;
import fr.sushi.app.ui.adressPicker.bottom.WheelTimeAdapter;
import fr.sushi.app.ui.checkout.PaymentMethodCheckoutActivity;
import fr.sushi.app.ui.checkout.paiement.model.CartDiscountsItem;
import fr.sushi.app.ui.checkout.paiement.model.DiscountResponse;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem;
import fr.sushi.app.ui.profileaddress.AddressAddActivity;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.ScheduleParser;
import fr.sushi.app.util.ScreenUtil;
import fr.sushi.app.util.SideProduct;
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
    private String returnAmount = "0";
    private String idAddress;
    private String discountCode = "";
    private DiscountResponse discountResponse;
    private String discountPrice;
    AlertDialog alerDialog = null;


    public PaiementFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            RestuarentsResponse restuarentsResponse = DataCacheUtil.getRestuarentsResponses();

            if (restuarentsResponse != null && restuarentsResponse.getResponse() != null) {
                List<ResponseItem> response = restuarentsResponse.getResponse();
                SearchPlace searchPlace = PlaceUtil.getRecentSearchAddress();
                for (ResponseItem responseItem : response) {
                    if (searchPlace != null) {
                        if (searchPlace.getOrder().getStoreId().equalsIgnoreCase(responseItem.getIdStore())) {
                            if (responseItem.getActiveOnlinePayment().equalsIgnoreCase("0")) {
                                binding.layoutCartPayment.setVisibility(View.GONE);
                                binding.radioLivarsion.setChecked(true);
                                binding.radioRestaurent.setChecked(false);
                                binding.radioCart.setChecked(false);
                                PaymentMethodCheckoutActivity.isAdyenSelected = false;
                                PaymentMethodCheckoutActivity.isCashPayment = true;
                                PaymentMethodCheckoutActivity.isDeliveryPayment = false;
                            } else {
                                binding.layoutCartPayment.setVisibility(View.VISIBLE);
                                binding.radioLivarsion.setChecked(false);
                                binding.radioRestaurent.setChecked(false);
                                binding.radioCart.setChecked(true);
                                PaymentMethodCheckoutActivity.isAdyenSelected = true;
                                PaymentMethodCheckoutActivity.isCashPayment = false;
                                PaymentMethodCheckoutActivity.isDeliveryPayment = false;
                            }
                        }
                    }
                }
            }
            PaymentMethodCheckoutActivity.ID_CART = "false";
            binding.tvDiscountAmount.setText("");
            binding.tvDiscount.setText("+ Ajouter un code réduction");
        }

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
                        Utils.showAlert(getActivity(), "Erreur!", errorResponse.getErrorString());

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

        paimentViewModel.getDiscountrMutableLiveData().observe(this, response -> {
            DialogUtils.hideDialog();
            if (response != null) {
                try {
                    JSONObject responseObject = new JSONObject(response.string());
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));

                    Log.e("JsonObject", "" + responseObject.toString());
                    if (error == true) {
                        errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(getActivity(), "Erreur!", errorResponse.getErrorString());

                    } else {
                        discountResponse = new Gson().fromJson(responseObject.toString(), DiscountResponse.class);

                        if (discountResponse != null) {
                            CartDiscountsItem cartDiscountsItem = discountResponse.getResponse().getCartDiscounts().get(0);
                            discountPrice = cartDiscountsItem.getDisplayAmount();
                            int discountValue = Integer.parseInt(cartDiscountsItem.getValue());
                            binding.tvDiscountAmount.setText("(" + discountPrice + ")");
                            binding.tvDiscount.setText(cartDiscountsItem.getName());
                            double totalPrice = ((PaymentMethodCheckoutActivity) getActivity()).getProductTotalPrice() - discountValue;
                            PaymentMethodCheckoutActivity.discountPrice = discountValue;
                            PaymentMethodCheckoutActivity.ID_CART = String.valueOf(discountResponse.getResponse().getIdCart());
                            ((PaymentMethodCheckoutActivity) getActivity()).showDiscountPrice(totalPrice, true);
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

        binding.layoutFullAddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPlace latestSearchPlace = PlaceUtil.getRecentSearchAddress();
                SearchPlace defaultSearchAddress = PlaceUtil.getDefaultSearchAddress();
                ProfileAddressModel model = new ProfileAddressModel();
                if (latestSearchPlace != null && defaultSearchAddress != null) {
                    if (latestSearchPlace.getAddress().equalsIgnoreCase(defaultSearchAddress.getAddress())) {
                        model.setLocation(defaultSearchAddress.getAddress());
                        model.setCity(defaultSearchAddress.getCity());
                        model.setZipCode(defaultSearchAddress.getPostalCode());
                        model.setAccessCode(defaultSearchAddress.getAccessCode());
                        model.setInterphone(defaultSearchAddress.getInterphone());
                        model.setFloor(defaultSearchAddress.getFloor());
                    } else {
                        model.setLocation(latestSearchPlace.getAddress());
                        model.setCity(latestSearchPlace.getCity());
                        model.setZipCode(latestSearchPlace.getPostalCode());
                    }
                } else {
                    if (latestSearchPlace != null) {
                        model.setLocation(latestSearchPlace.getAddress());
                        model.setCity(latestSearchPlace.getCity());
                        model.setZipCode(latestSearchPlace.getPostalCode());
                    }
                }
                Intent intent = new Intent(getActivity(), AddressAddActivity.class);
                intent.putExtra(IntentKey.ADDRESS_MODEL, model);
                intent.putExtra(IntentKey.KEY_IS_FROM_PAIEMENT_PAGE, true);
                startActivity(intent);
            }
        });

        observeData();
        initRadioListener();

        binding.tvName.setText(SharedPref.read(PrefKey.USER_NAME, ""));
        String phoneNumber = Utils.getFormatedPhoneNumber(SharedPref.read(PrefKey.USER_PHONE, ""), getActivity());
        binding.tvMobileNo.setText(phoneNumber);


        return view;
    }

    private void sendPayment() {
        SearchPlace latestSearchPlace = PlaceUtil.getRecentSearchAddress();
        // SearchPlace defaultSearchAddress = PlaceUtil.getDefaultSearchAddress();


        if (latestSearchPlace == null || latestSearchPlace.getOrder().getSchedule() == null) {
            Utils.showAlert(getActivity(), "Alert!", "Veuillez choisir un jour");
            return;
        } else if (latestSearchPlace == null || latestSearchPlace.getOrder().getStoreId() == null) {
            Utils.showAlert(getActivity(), "Alert!", "Veuillez choisir un restaurant");
            return;
        }

        String json = SharedPref.read(PrefKey.USER_ADDRESS, "");
        List<ProfileAddressModel> itemList = GsonHelper.on().convertJsonToNormalAddress(json);

        if (!itemList.isEmpty()) {
            for (ProfileAddressModel item : itemList) {
                if (item.getLocation().equalsIgnoreCase(latestSearchPlace.getAddress())) {
                    idAddress = item.getId();
                }
            }
        }


        if (TextUtils.isEmpty(idAddress)) {

            ProfileAddressModel model = new ProfileAddressModel();
            model.setId(UUID.randomUUID().toString());


            model.setLocation(latestSearchPlace.getAddress());
            model.setCity(latestSearchPlace.getCity());
            model.setZipCode(latestSearchPlace.getPostalCode());
            model.setAddressType(latestSearchPlace.getType());

            paimentViewModel.addOrUpdateAddressInServer(model);
            return;

        }


        DialogUtils.showDialog(getActivity());


        JsonObject mainObject = new JsonObject();

        mainObject.addProperty("token", SharedPref.read(PrefKey.USER_TOKEN, ""));
        mainObject.addProperty("email", SharedPref.read(PrefKey.USER_EMAIL, ""));
        mainObject.addProperty("id_customer", SharedPref.read(PrefKey.USER_ID, ""));
        mainObject.addProperty("discount", discountCode);

        JsonObject cartJsonObject = new JsonObject();
        cartJsonObject.addProperty("id_cart", "false");
        cartJsonObject.addProperty("order_date", latestSearchPlace.getOrder().getOrderData());

        cartJsonObject.addProperty("id_store", latestSearchPlace.getOrder().getStoreId());

        cartJsonObject.addProperty("id_delivery_zone", latestSearchPlace.getOrder().getDeliveryId());

        cartJsonObject.addProperty("is_delivery", "1");
        cartJsonObject.addProperty("id_address", idAddress);

        List<MyCartProduct> myCartProducts = DBManager.on().getMyCartProductsWithCrossSellingItems();

        JsonArray productsArray = new JsonArray();

        for (MyCartProduct item : myCartProducts) {
            JsonObject product = new JsonObject();
            product.addProperty("id_product", item.getProductId());
            product.addProperty("quantity", item.getItemCount());

            List<CrossSellingSelectedItem> crossSellingList = item.getCrossSellingSelectedItems();

            JsonArray crossSellingArray = new JsonArray();
            if (crossSellingList != null && !crossSellingList.isEmpty()) {
                for (CrossSellingSelectedItem cItem : crossSellingList) {
                    JsonObject crossSellJson = new JsonObject();
                    crossSellJson.addProperty("id_product", cItem.getMainProductId());
                    crossSellJson.addProperty("id_product_cross_selling", cItem.getProductId());
                    crossSellJson.addProperty("quantity", cItem.getProductCount());
                    crossSellingArray.add(crossSellJson);
                }
            }
            product.add("accessories", crossSellingArray);

            productsArray.add(product);
        }

        List<SideProduct> sideProducts = DataCacheUtil.getSideProductList();

        for (SideProduct sideProductItem : sideProducts) {
            JsonObject sideProduct = new JsonObject();
            sideProduct.addProperty("id_product", sideProductItem.getProductId());
            sideProduct.addProperty("quantity", sideProductItem.getItemCount());
            productsArray.add(sideProduct);
        }


        mainObject.add("Products", productsArray);


        mainObject.add("Cart", cartJsonObject);


        paimentViewModel.addCartDiscount(mainObject);
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

        binding.layoutDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflaters = getLayoutInflater();
                View dialogView = inflaters.inflate(R.layout.layout_discount_alert, null);

                final EditText editText = dialogView.findViewById(R.id.edtCode);
                TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
                TextView tvOk = dialogView.findViewById(R.id.tvOk);

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        alerDialog.dismiss();
                        discountCode = editText.getText().toString();
                    }
                });

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        Utils.hideSoftKeyboard(getActivity());
                        alerDialog.dismiss();
                        discountCode = editText.getText().toString();
                        sendPayment();

                    }
                });

                dialogBuilder.setView(dialogView);
                alerDialog = dialogBuilder.create();
                alerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                alerDialog.show();

            }
        });

        binding.layoutAddress.setOnClickListener(v -> {
            DialogUtils.showDialog(getActivity());
            SearchPlace latestSearchPlace = PlaceUtil.getRecentSearchAddress();
            if (latestSearchPlace != null) {
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
            // showDialogSpecifyAmount();

            final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
            LayoutInflater inflaters = this.getLayoutInflater();
            View dialogView = inflaters.inflate(R.layout.layout_custom_alert, null);

            final EditText editText = dialogView.findViewById(R.id.edtExchange);
            TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
            TextView tvOk = dialogView.findViewById(R.id.tvOk);

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.radioRestaurent.setChecked(true);
                    binding.radioLivarsion.setChecked(false);
                    binding.radioCart.setChecked(false);
                    PaymentMethodCheckoutActivity.isAdyenSelected = false;
                    PaymentMethodCheckoutActivity.isCashPayment = false;
                    PaymentMethodCheckoutActivity.isDeliveryPayment = true;
                    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    dialogBuilder.dismiss();
                    returnAmount = editText.getText().toString();
                    if (!TextUtils.isEmpty(returnAmount)) {
                        PaymentMethodCheckoutActivity.payemntChangeAmount = returnAmount;
                    }

                    binding.tvReaustrantInfo.setText("Espèce - prevoir " + Utils.getDecimalFormat(Double.parseDouble(PaymentMethodCheckoutActivity.payemntChangeAmount)) + " €");
                }
            });
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    binding.radioRestaurent.setChecked(true);
                    binding.radioLivarsion.setChecked(false);
                    binding.radioCart.setChecked(false);
                    PaymentMethodCheckoutActivity.isAdyenSelected = false;
                    PaymentMethodCheckoutActivity.isCashPayment = false;
                    PaymentMethodCheckoutActivity.isDeliveryPayment = true;
                    Utils.hideSoftKeyboard(getActivity());
                    dialogBuilder.dismiss();
                    returnAmount = editText.getText().toString();
                    PaymentMethodCheckoutActivity.payemntChangeAmount = returnAmount;
                    binding.tvReaustrantInfo.setText("Espèce - prevoir " + Utils.getDecimalFormat(Double.parseDouble(returnAmount)) + " €");
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialogBuilder.show();

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
                PaymentMethodCheckoutActivity.payemntChangeAmount = "0";
                binding.tvReaustrantInfo.setText("Espèce - prevoir " + Utils.getDecimalFormat(Double.parseDouble(PaymentMethodCheckoutActivity.payemntChangeAmount)) + " €");

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
            LatLng latLng = new LatLng(48.8566, 2.3522);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            //markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        SearchPlace latestSearchPlace = PlaceUtil.getRecentSearchAddress();
        SearchPlace defaultSearchAddress = PlaceUtil.getDefaultSearchAddress();
        if (latestSearchPlace != null) {
            binding.tvCountryCode.setText(latestSearchPlace.getPostalCode() + " " + latestSearchPlace.getCity());
            binding.tvAddress.setText(latestSearchPlace.getAddress());
            if (!TextUtils.isEmpty(latestSearchPlace.getTime())) {
                String time = latestSearchPlace.getTime().replace(":", "h");
                binding.tvDeliveryTime.setText("Heure de " + latestSearchPlace.getType());
                binding.tvTime.setText(time);
            }
        }

        if (latestSearchPlace != null && defaultSearchAddress != null) {
            if (latestSearchPlace.getAddress().equalsIgnoreCase(defaultSearchAddress.getAddress())) {
                // `Code `+ address.accessCode+`, Interphone `+ address.interphone+`, Étage `+  address.floor}"
                String fullText = null;
                if (!TextUtils.isEmpty(defaultSearchAddress.getAccessCode())) {
                    fullText = "Code " + defaultSearchAddress.getAccessCode();
                }

                if (!TextUtils.isEmpty(defaultSearchAddress.getInterphone())) {
                    if (fullText != null && fullText.contains("Code")) {
                        fullText += ", Interphone " + defaultSearchAddress.getInterphone();
                    } else {
                        fullText = "Interphone " + defaultSearchAddress.getInterphone();
                    }

                }

                if (!TextUtils.isEmpty(defaultSearchAddress.getFloor())) {
                    if (fullText != null && (fullText.contains("Code") || fullText.contains("interphone"))) {
                        fullText += ", Etage " + defaultSearchAddress.getFloor();
                    } else {
                        fullText = "Etage " + defaultSearchAddress.getFloor();
                    }

                }

                if (!TextUtils.isEmpty(fullText)) {
                    binding.tvAddressHouse.setText(fullText);
                    binding.tvAddressHouse.setTextColor(Color.parseColor("#394F61"));
                } else {
                    binding.tvAddressHouse.setText("Ajouter un code, étage, interphone");
                    binding.tvAddressHouse.setTextColor(Color.parseColor("#EA148A"));
                }


            } else {
                binding.tvAddressHouse.setText("Ajouter un code,étage,interphone");
                binding.tvAddressHouse.setTextColor(Color.parseColor("#EA148A"));
            }
        } else {
            binding.tvAddressHouse.setText("Ajouter un code,étage,interphone");
            binding.tvAddressHouse.setTextColor(Color.parseColor("#EA148A"));
        }

        if (SharedPref.readBoolean(PrefKey.IS_EMPORTER_PRESSED, false)) {
            binding.tvAddressTitle.setText("Retrait");
            binding.tvAddressHouse.setTextColor(Color.parseColor("#394F61"));
            binding.layoutFullAddres.setClickable(false);
            binding.layoutFullAddres.setEnabled(false);
            RestuarentsResponse restuarentsResponse = DataCacheUtil.getRestuarentsResponses();

            if (restuarentsResponse != null & restuarentsResponse.getResponse() != null) {
                List<ResponseItem> response = restuarentsResponse.getResponse();
                SearchPlace searchPlace = PlaceUtil.getRecentSearchAddress();
                for (ResponseItem responseItem : response) {
                    if (searchPlace != null) {
                        if (searchPlace.getOrder().getStoreId().equalsIgnoreCase(responseItem.getIdStore())) {
                            binding.tvAddressHouse.setText(responseItem.getName());
                        }
                    }
                }
            }

        } else {
            binding.tvAddressTitle.setText("Informations de livraison");
            binding.tvAddressHouse.setTextColor(Color.parseColor("#EA148A"));
            binding.layoutFullAddres.setClickable(true);
            binding.layoutFullAddres.setEnabled(true);
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

            if (!TextUtils.isEmpty(selectedOrder.getSchedule())) {
                String time = selectedOrder.getSchedule().replace(":", "h");
                binding.tvTime.setText(time);
            }

        });

        //Wheel time adapter

        int wheelPaddingRight = ScreenUtil.getScreenWidth(getActivity()) / 2 - ScreenUtil.dpToPx(getActivity(), 15);
        int wheelPaddingLeft = ScreenUtil.getScreenWidth(getActivity()) / 2 - ScreenUtil.dpToPx(getActivity(), 20);
        timeRv.setPadding(wheelPaddingLeft, 0, wheelPaddingRight, 0);
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
