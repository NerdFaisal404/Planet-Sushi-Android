package fr.sushi.app.ui.checkout;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.data.model.address_picker.error.ErrorResponse;
import fr.sushi.app.databinding.ActivityPaymentCheckoutBinding;
import fr.sushi.app.ui.checkout.model.PaymentModel;
import fr.sushi.app.ui.checkout.model.PaymentSessionModel;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.util.DialogUtils;
import fr.sushi.app.util.Utils;

public class PaymentCheckoutActivity extends AppCompatActivity {
    private ActivityPaymentCheckoutBinding binding;
    private PagerAdapter pagerAdapter;
    private List<MyCartProduct> selectedProducts = new ArrayList<>();

    private double totalPrice;


    private CheckoutViewModel checkoutViewModel;

    private int REQUEST_CODE_CHECKOUT=555;


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
               /* paymentRequest = new PaymentRequest(PaymentCheckoutActivity.this, paymentRequestListener);
                paymentRequest.start();*/
                //sendPayment();

                CheckoutController.startPayment(/*Activity*/ this, new CheckoutSetupParametersHandler() {
                    @Override
                    public void onRequestPaymentSession(@NonNull CheckoutSetupParameters checkoutSetupParameters) {
                        Log.d("Adyenb_log", checkoutSetupParameters.toString());
                        // TODO: Forward to your own server and request the payment session from Adyen with the given CheckoutSetupParameters.
                        sendAdyenPayment(checkoutSetupParameters.getSdkToken());
                    }

                    @Override
                    public void onError(@NonNull CheckoutException checkoutException) {
                        // TODO: Handle error.
                    }
                });
            }

        });

    }


    private void createPaymentSession(String session) {
        CheckoutController.handlePaymentSessionResponse(this, session, new StartPaymentParametersHandler() {
            @Override
            public void onPaymentInitialized(@NonNull StartPaymentParameters startPaymentParameters) {
                PaymentMethodHandler paymentMethodHandler = CheckoutController.getCheckoutHandler(startPaymentParameters);
                paymentMethodHandler.handlePaymentMethodDetails(/* Activity */ PaymentCheckoutActivity.this, REQUEST_CODE_CHECKOUT);

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

    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        binding.totalPriceTv.setText(Utils.getDecimalFormat(totalPrice) + "€");
    }

    public void setPriceWithSideProducts(double priceSideProducts) {
        binding.totalPriceTv.setText(Utils.getDecimalFormat(totalPrice + priceSideProducts) + "€");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHECKOUT) {
            if (resultCode == PaymentMethodHandler.RESULT_CODE_OK) {
                PaymentResult paymentResult = PaymentMethodHandler.Util.getPaymentResult(data);
                // Handle PaymentResult.
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
