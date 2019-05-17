package fr.sushi.app.ui.checkout;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;

import java.util.ArrayList;
import java.util.List;

import fr.sushi.app.R;
import fr.sushi.app.data.db.DBManager;
import fr.sushi.app.databinding.ActivityCheckoutBinding;
import fr.sushi.app.ui.menu.MenuPrefUtil;
import fr.sushi.app.ui.menu.MyCartProduct;
import fr.sushi.app.util.Utils;

public class CheckoutActivity extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private PagerAdapter pagerAdapter;
    private List<MyCartProduct> selectedProducts = new ArrayList<>();

    private double totalPrice;


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
            binding.tvEmptyView.setVisibility(View.VISIBLE);
            binding.layoutBottomCheckout.setVisibility(View.GONE);
        } else {
            binding.viewpager.setVisibility(View.VISIBLE);
            binding.tvStepOne.setClickable(true);
            binding.tvStepTwo.setClickable(true);
            binding.tvStepThree.setClickable(true);
            binding.tvEmptyView.setVisibility(View.GONE);
            binding.layoutBottomCheckout.setVisibility(View.VISIBLE);
        }


    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        binding.totalPriceTv.setText(Utils.getDecimalFormat(totalPrice) + "€");
    }

    public void setPriceWithSideProducts(double priceSideProducts) {
        binding.totalPriceTv.setText(Utils.getDecimalFormat(totalPrice + priceSideProducts) + "€");
    }
}
