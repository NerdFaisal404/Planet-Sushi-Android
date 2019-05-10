package fr.sushi.app.ui.checkout;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ActivityCheckoutBinding;

public class CheckoutActivity extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private PagerAdapter pagerAdapter;

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
        binding.indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                binding.viewpager.setCurrentItem(step, true);
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvStepOne.setOnClickListener(v -> binding.viewpager.setCurrentItem(0));
        binding.tvStepTwo.setOnClickListener(v -> binding.viewpager.setCurrentItem(1));
        binding.tvStepThree.setOnClickListener(v -> binding.viewpager.setCurrentItem(2));
    }
}
