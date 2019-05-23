package fr.sushi.app.ui.checkout;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.databinding.ActivityPaymentSuccssBinding;

public class PaymentSuccssActivity extends AppCompatActivity {
    private ActivityPaymentSuccssBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_succss);
        binding.tvClose.setOnClickListener(v -> finish());
    }
}
