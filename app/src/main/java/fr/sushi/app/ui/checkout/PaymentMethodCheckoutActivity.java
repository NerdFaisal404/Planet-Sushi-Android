package fr.sushi.app.ui.checkout;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.adyen.checkout.core.CheckoutException;
import com.adyen.checkout.core.PaymentMethodHandler;
import com.adyen.checkout.core.PaymentResult;
import com.adyen.checkout.core.StartPaymentParameters;
import com.adyen.checkout.core.handler.StartPaymentParametersHandler;
import com.adyen.checkout.ui.CheckoutController;
import com.adyen.checkout.ui.CheckoutSetupParameters;
import com.adyen.checkout.ui.CheckoutSetupParametersHandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.helper.GsonHelper;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.ProfileAddressModel;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.data.model.restuarents.ResponseItem;
import fr.sushi.app.databinding.ActivityPaymentCheckoutBinding;
import fr.sushi.app.ui.checkout.accompagnements.AccompagnementsFragment;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import fr.sushi.app.ui.checkout.model.PaymentSessionModel;
import fr.sushi.app.ui.checkout.model.payment_success.PaymentSuccessResponse;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.menu.model.CrossSellingSelectedItem;
import fr.sushi.app.util.DataCacheUtil;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.SideProduct;
import fr.sushi.app.util.Utils;

public class PaymentMethodCheckoutActivity extends AppCompatActivity {
    private ActivityPaymentCheckoutBinding binding;
    private PagerAdapter pagerAdapter;
    private List<MyCartProduct> selectedProducts = new ArrayList<>();

    private double totalPrice;
    private int freeSaucesCount;


    private CheckoutViewModel checkoutViewModel;

    private int REQUEST_CODE_CHECKOUT = 555;

    public static boolean isAdyenSelected = true;
    public static boolean isCashPayment;
    public static boolean isDeliveryPayment;

    private String payementMethod = "Adyen";
    private String paymentTotalPrice = "0";
    private String returnMoney = "0";
    public static String payemntChangeAmount = "0";
    String idAddress = null;
    private ProfileAddressModel model;
    private String adyenPayload = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_checkout);

        assert binding.viewpager != null;
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(pagerAdapter);

        observeData();
        // We keep last page for a "finishing" page
        binding.indicator.setViewPager(binding.viewpager, true);
        binding.indicator.setStepCount(3);
        binding.indicator.addOnStepClickListener(step -> binding.viewpager.setCurrentItem(step, true));

        binding.ivBack.setOnClickListener(v -> finish());

        binding.tvStepOne.setOnClickListener(v -> binding.viewpager.setCurrentItem(0));
        binding.tvStepTwo.setOnClickListener(v -> binding.viewpager.setCurrentItem(1));
        binding.tvStepThree.setOnClickListener(v -> binding.viewpager.setCurrentItem(2));

        //selectedProducts = MenuPrefUtil.getSaveItems();
        selectedProducts = DBManager.on().getAllProducts();

        if (selectedProducts.size() < 1) {
            binding.viewpager.setVisibility(View.GONE);
            binding.tvStepOne.setClickable(false);
            binding.tvStepTwo.setClickable(false);
            binding.tvStepThree.setClickable(false);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
            binding.layoutBottomCheckout.setVisibility(View.GONE);
        } else {
            binding.viewpager.setVisibility(View.VISIBLE);
            binding.tvStepOne.setClickable(true);
            binding.tvStepTwo.setClickable(true);
            binding.tvStepThree.setClickable(true);
            binding.layoutEmpty.setVisibility(View.GONE);
            binding.layoutBottomCheckout.setVisibility(View.VISIBLE);
        }


        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 1) {
                    AccompagnementsFragment fragment = (AccompagnementsFragment) pagerAdapter.getItem(position);
                    fragment.clearPreviousSelectedItem();
                }
                if (position == 2) {
                    binding.totalPriceTv.setVisibility(View.GONE);
                    binding.midline.setVisibility(View.GONE);
                    binding.tvSubmit.setGravity(Gravity.CENTER);
                    binding.tvSubmit.setText("PAYER " + Utils.getDecimalFormat(totalPrice) + "€");
                } else {
                    binding.totalPriceTv.setVisibility(View.VISIBLE);
                    binding.midline.setVisibility(View.VISIBLE);
                    binding.tvSubmit.setVisibility(View.VISIBLE);
                    binding.tvSubmit.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    binding.tvSubmit.setText("CONTINUER");
                }
            }
        });

        binding.layoutBottomCheckout.setOnClickListener(v -> {
            int position = binding.viewpager.getCurrentItem();
            if (position == 0) {
                binding.viewpager.setCurrentItem(1);
            } else if (position == 1) {
                binding.viewpager.setCurrentItem(2);
            } else if (position == 2) {


                if (isAdyenSelected) {
                    payementMethod = "Adyen";
                    paymentTotalPrice = totalPrice + "";
                    returnMoney = "0";
                    CheckoutController.startPayment(/*Activity*/ this, new CheckoutSetupParametersHandler() {
                        @Override
                        public void onRequestPaymentSession(@NonNull CheckoutSetupParameters checkoutSetupParameters) {
                            // TODO: Forward to your own server and request the payment session from Adyen with the given CheckoutSetupParameters.

                            sendAdyenPayment(checkoutSetupParameters.getSdkToken());
                        }

                        @Override
                        public void onError(@NonNull CheckoutException checkoutException) {
                            // TODO: Handle error.
                        }
                    });
                } else if (isDeliveryPayment) {
                    payementMethod = "CardOnDelivery";
                    paymentTotalPrice = "0";
                    returnMoney = payemntChangeAmount;
                    adyenPayload = "false";
                    sendPayment();


                } else if (isCashPayment) {

                    payementMethod = "Cash";
                    paymentTotalPrice = "0";
                    returnMoney = "0";
                    adyenPayload = "false";
                    sendPayment();

                }


            }

        });

    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        int minimumPrice = (int) this.totalPrice;
        if (minimumPrice < 25) {
            binding.layoutBottomCheckout.setEnabled(false);
            binding.layoutBottomCheckout.setClickable(false);
        } else {
            binding.layoutBottomCheckout.setEnabled(true);
            binding.layoutBottomCheckout.setClickable(true);
        }
    }

    private void sendPayment() {
        SearchPlace latestSearchPlace = PlaceUtil.getRecentSearchAddress();
        // SearchPlace defaultSearchAddress = PlaceUtil.getDefaultSearchAddress();


        if (latestSearchPlace == null || latestSearchPlace.getOrder().getSchedule() == null) {
            Utils.showAlert(this, "Alert!", "Veuillez choisir un jour");
            return;
        } else if (latestSearchPlace == null || latestSearchPlace.getOrder().getStoreId() == null) {
            Utils.showAlert(this, "Alert!", "Veuillez choisir un restaurant");
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

            model = new ProfileAddressModel();
            model.setId(UUID.randomUUID().toString());


            model.setLocation(latestSearchPlace.getAddress());
            model.setCity(latestSearchPlace.getCity());
            model.setZipCode(latestSearchPlace.getPostalCode());
            model.setAddressType(latestSearchPlace.getType());

            checkoutViewModel.addOrUpdateAddressInServer(model);
            return;

        }


       /* if selectedDate == nil {
            DVAlertViewController.showCommonAlert(title: "Alert!", message: "Veuillez choisir un jour", controller: self.topViewController)
            return nil
        }else if selectedTime == nil {
            DVAlertViewController.showCommonAlert(title: "Alert!", message: "Veuillez choisir un horaire", controller: self.topViewController)
            return nil
        }else if selectedStoreId == nil {
            DVAlertViewController.showCommonAlert(title: "Alert!", message: "Veuillez choisir un restaurant", controller: self.topViewController)
            return nil
        }else if defaultDeliveryAddress == nil {
            DVAlertViewController.showCommonAlert(title: "Alert!", message: "Veuillez choisir une adresse", controller: self.topViewController)
            return nil
        }else if selectedDeliveryZoneId == nil {
            DVAlertViewController.showCommonAlert(title: "Alert!", message: "Adresse de livraison non disponible", controller: self.topViewController)
            return nil
        }

        if let defaultAddress = realm.objects(MyAddress.self).first(where: { $0.address == defaultDeliveryAddress?.address }) {

            if defaultAddress.city == defaultDeliveryAddress?.city && defaultAddress.postcode == defaultDeliveryAddress?.postcode {
                address_id = defaultAddress.address_id
            }
        }

        if address_id == nil && orderType == .delivery {
            updateCustomerAddress(defaultDeliveryAddress!)
            return nil
        }
*/


        DialogUtils.showDialog(this);


        JsonObject mainObject = new JsonObject();

        mainObject.addProperty("token", SharedPref.read(PrefKey.USER_TOKEN, ""));
        mainObject.addProperty("email", SharedPref.read(PrefKey.USER_EMAIL, ""));
        mainObject.addProperty("id_customer", SharedPref.read(PrefKey.USER_ID, ""));

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

        JsonObject paymentObject = new JsonObject();
        paymentObject.addProperty("payment_method", payementMethod);
        paymentObject.addProperty("adyen_payload", adyenPayload);
        paymentObject.addProperty("total_paid", paymentTotalPrice);
        paymentObject.addProperty("return_money", returnMoney);


        mainObject.add("Cart", cartJsonObject);
        mainObject.add("Payment", paymentObject);


        checkoutViewModel.sendSavePaymentOrder(mainObject);
    }


    private void createPaymentSession(String session) {
        CheckoutController.handlePaymentSessionResponse(/*Activity*/ this, session, new StartPaymentParametersHandler() {
            @Override
            public void onPaymentInitialized(@NonNull StartPaymentParameters startPaymentParameters) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PaymentMethodHandler paymentMethodHandler = CheckoutController.getCheckoutHandler(startPaymentParameters);
                        paymentMethodHandler.handlePaymentMethodDetails(/* Activity */ PaymentMethodCheckoutActivity.this, REQUEST_CODE_CHECKOUT);

                    }
                });

            }


            @Override
            public void onError(@NonNull CheckoutException checkoutException) {
                // TODO: Handle error.
            }
        });
    }


    private void sendAdyenPayment(String token) {

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setOrderDate("2019-05-21 12:15:00");
        paymentModel.setIdDeliveryZone("235");
        paymentModel.setIdCustomer(SharedPref.read(PrefKey.USER_ID, ""));
        paymentModel.setIdStore("71");
        paymentModel.setAppToken(token);
        paymentModel.setReturnUrl("planetsushi://");
        paymentModel.setChannel("android");
        paymentModel.setAmount(totalPrice);

        DialogUtils.showDialog(this);
        checkoutViewModel.sendAdyenPayment(paymentModel);


    }

    private void observeData() {

        checkoutViewModel = ViewModelProviders.of(this).get(CheckoutViewModel.class);
        checkoutViewModel.getPaymentMutableLiveData().observe(this, responseBody -> {
            if (responseBody != null) {
                DialogUtils.hideDialog();

                JSONObject responseObject = null;
                try {
                    responseObject = new JSONObject(responseBody.string());

                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(this, "Erreur!", errorResponse.getErrorString());
                    } else {
                        PaymentSessionModel paymentSessionModel = new Gson().fromJson(responseObject.toString(), PaymentSessionModel.class);

                        createPaymentSession(paymentSessionModel.getResponse().getPaymentSession());


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        checkoutViewModel.getPaymentOrderMutableLiveData().observe(this, responseBody -> {
            if (responseBody != null) {
                DialogUtils.hideDialog();

                JSONObject responseObject = null;
                try {
                    responseObject = new JSONObject(responseBody.string());

                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    Log.e("JsonObject", "value =" + responseObject.toString());
                    if (error == true) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(this, "Erreur!", errorResponse.getErrorString());
                    } else {

                        DBManager.on().clearMyCartProduct();
                        DataCacheUtil.removeSideProducts();
                        JSONObject res = responseObject.getJSONObject("response");
                        String idOrder = res.optString("id_order");
                        Intent intent = new Intent(new Intent(this, PaymentSuccssActivity.class));
                        intent.putExtra(IntentKey.KEY_ORDER_ID, idOrder);
                        startActivity(intent);

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        checkoutViewModel.getAddressLiveData().observe(this, responseBody -> {
            DialogUtils.hideDialog();
            if (responseBody != null) {
                try {
                    String response = responseBody.string();

                    JSONObject responseObject = new JSONObject(response);
                    boolean error = Boolean.parseBoolean(responseObject.getString("error"));
                    if (error) {
                        ErrorResponse errorResponse = new Gson().fromJson(responseObject.toString(), ErrorResponse.class);
                        Utils.showAlert(this, "Erreur!", errorResponse.getErrorString());
                    } else {
                        JSONObject res = responseObject.getJSONObject("response");
                        idAddress = res.optString("id_address");
                        model.setId(idAddress);
                        checkoutViewModel.updateAddress(model);
                        sendPayment();

                    }

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("AddressUpdateResponse", "Response body null");
            }

        });

    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        this.freeSaucesCount = (int) totalPrice / 10;
        binding.totalPriceTv.setText(Utils.getDecimalFormat(totalPrice) + "€");
        int minimumPrice = (int) this.totalPrice;
        if (minimumPrice < 25) {
            binding.layoutBottomCheckout.setEnabled(false);
            binding.layoutBottomCheckout.setClickable(false);
        } else {
            binding.layoutBottomCheckout.setEnabled(true);
            binding.layoutBottomCheckout.setClickable(true);
        }
    }

    public void setPriceWithSideProducts(double priceSideProducts) {
        binding.totalPriceTv.setText(Utils.getDecimalFormat(totalPrice + priceSideProducts) + "€");
    }

    public int getFreeSaucesCount() {
        return freeSaucesCount;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHECKOUT) {
            if (resultCode == PaymentMethodHandler.RESULT_CODE_OK) {
                PaymentResult paymentResult = PaymentMethodHandler.Util.getPaymentResult(data);
                // Handle PaymentResult.
                adyenPayload = paymentResult.getPayload();
                sendPayment();
            } else {
                CheckoutException checkoutException = PaymentMethodHandler.Util.getCheckoutException(data);

                if (resultCode == PaymentMethodHandler.RESULT_CODE_CANCELED) {
                    // Handle cancellation and optional CheckoutException.
                } else {
                    // Handle CheckoutException.
                }
            }
        }
    }
}
