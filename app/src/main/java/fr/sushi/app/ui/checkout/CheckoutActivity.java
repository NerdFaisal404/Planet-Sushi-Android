package fr.sushi.app.ui.checkout;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adyen.core.PaymentRequest;
import com.adyen.core.interfaces.HttpResponseCallback;
import com.adyen.core.interfaces.PaymentDataCallback;
import com.adyen.core.interfaces.PaymentRequestListener;
import com.adyen.core.models.Payment;
import com.adyen.core.models.PaymentRequestResult;
import com.adyen.core.utils.AsyncHttpClient;
import com.badoualy.stepperindicator.StepperIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import fr.sushi.app.databinding.ActivityCheckoutBinding;
import fr.sushi.app.ui.home.PlaceUtil;
import fr.sushi.app.ui.home.SearchPlace;
import fr.sushi.app.ui.menu.MenuPrefUtil;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.ui.payment.Global.Constants;
import fr.sushi.app.util.Utils;

public class CheckoutActivity extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private PagerAdapter pagerAdapter;
    private List<MyCartProduct> selectedProducts = new ArrayList<>();

    private double totalPrice;

    /*Related to payment Adyen*/
    private static final String SETUP = Constants.CHECKOUT_SETUP;
    private static final String VERIFY = Constants.CHECKOUT_VERIFY;

  // Add the URL for your server here; or you can use the demo server of Adyen: https://checkoutshopper-test.adyen.com/checkoutshopper/demoserver/
    private String merchantServerUrl = Constants.CHECKOUT_MERCHANT_SERVER_URL;

    // Add the api secret key for your server here; you can retrieve this key from customer area.
    private String merchantApiSecretKey = Constants.CHECKOUT_MERCHANT_API_SECRET_KEY;

    // Add the header key for merchant server api secret key here; e.g. "x-demo-server-api-key"
    private String merchantApiHeaderKeyForApiSecretKey = Constants.CHECKOUT_MERCHANT_API_HEADER_KEY_FOR_API_SECRET_KEY;

    private PaymentRequest paymentRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);

        assert binding.viewpager != null;
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(pagerAdapter);

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
                paymentRequest = new PaymentRequest(CheckoutActivity.this, paymentRequestListener);
                paymentRequest.start();
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

    private final PaymentRequestListener paymentRequestListener = new PaymentRequestListener() {
        @Override
        public void onPaymentDataRequested(@NonNull PaymentRequest paymentRequest, @NonNull String token,
                                           @NonNull final PaymentDataCallback paymentDataCallback) {
            final Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put(merchantApiHeaderKeyForApiSecretKey, merchantApiSecretKey);

            AsyncHttpClient.post(merchantServerUrl, headers, getSetupDataString(token), new HttpResponseCallback() {
                @Override
                public void onSuccess(final byte[] response) {
                    paymentDataCallback.completionWithPaymentData(response);
                }

                @Override
                public void onFailure(final Throwable e) {
                    Log.e("HTTP Response problem: ", "" + e);

                }
            });
        }

        @Override
        public void onPaymentResult(@NonNull PaymentRequest paymentRequest,
                                    @NonNull PaymentRequestResult paymentRequestResult) {
            if (paymentRequestResult.isProcessed() && (
                    paymentRequestResult.getPayment().getPaymentStatus()
                            == Payment.PaymentStatus.AUTHORISED
                            || paymentRequestResult.getPayment().getPaymentStatus()
                            == Payment.PaymentStatus.RECEIVED)) {
                verifyPayment(paymentRequestResult.getPayment());
//                Intent intent = new Intent(context, SuccessActivity.class);
//                startActivity(intent);
                Toast.makeText(CheckoutActivity.this, getString(R.string.check_out_payment_success), Toast.LENGTH_SHORT).show();
                //  finish();
            } else {
//                Intent intent = new Intent(context, FailureActivity.class);
//                startActivity(intent);
                Toast.makeText(CheckoutActivity.this, getString(R.string.check_out_payment_failure), Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    };

    /*******************Related to payment*********************/

    private String getSetupDataString(final String token) {
        final JSONObject jsonObject = new JSONObject();
        try {

            List<SearchPlace> recentSearchPlace = PlaceUtil.getSearchPlace();
            if (!recentSearchPlace.isEmpty()) {
                SearchPlace place = recentSearchPlace.get(0);

                jsonObject.put("order_date", "2019-05-21 12:15:00");
                jsonObject.put("id_delivery_zone", "235");
            }
            jsonObject.put("id_customer", SharedPref.read(PrefKey.USER_ID, ""));
            jsonObject.put("id_store", "71");
            jsonObject.put("app_token", token);
            jsonObject.put("returnUrl", "planetsushi://");

            jsonObject.put("channel", "android");
            jsonObject.put("amount", Utils.getDecimalFormat(totalPrice));
            Log.e("PaymentJSON:", jsonObject.toString());

          /*  "id_customer": AppUserDefault.getUserID()!,
                    "id_store": OrderAndDataController.sharedInstance.selectedStoreId ?? "",
                    "order_date": OrderAndDataController.sharedInstance.selectedDate ?? "",
                    "id_delivery_zone": "235", // 71 is an id_store. get id_delivery_zone in the store schedule (use this for test : 235)
                    "returnUrl": "planetsushi://",
                    "channel": "ios",
                    "amount": 100*OrderAndDataController.sharedInstance.totalPriceWithSideProducts,
                    "app_token": token
            */
        } catch (final JSONException jsonException) {
            Log.e("Setup failed", "" + jsonException);
        }
        return jsonObject.toString();
    }


    private void verifyPayment(final Payment payment) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payload", payment.getPayload());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.check_out_adyen_payment_verify_failure), Toast.LENGTH_LONG).show();
            return;
        }
        String verifyString = jsonObject.toString();

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put(merchantApiHeaderKeyForApiSecretKey, merchantApiSecretKey);

        AsyncHttpClient.post(merchantServerUrl + VERIFY, headers, verifyString, new HttpResponseCallback() {
            String resultString = "";

            @Override
            public void onSuccess(final byte[] response) {
                try {
                    JSONObject jsonVerifyResponse = new JSONObject(new String(response, Charset.forName("UTF-8")));
                    String authResponse = jsonVerifyResponse.getString("authResponse");
                    if (authResponse.equalsIgnoreCase(payment.getPaymentStatus().toString())) {
                        resultString = getString(R.string.check_out_adyen_payment_verify_success_1) + payment.getPaymentStatus().toString().toLowerCase(Locale.getDefault()) + getString(R.string.check_out_adyen_payment_verify_success_2);
                        Intent intent = new Intent(CheckoutActivity.this, CheckoutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // finish();

                    } else {
                        resultString = getString(R.string.check_out_adyen_payment_verify_failure);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    resultString = getString(R.string.check_out_adyen_payment_verify_failure);
                }
                Toast.makeText(CheckoutActivity.this, resultString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(final Throwable e) {
                Toast.makeText(CheckoutActivity.this, resultString, Toast.LENGTH_LONG).show();
            }
        });
    }
}
