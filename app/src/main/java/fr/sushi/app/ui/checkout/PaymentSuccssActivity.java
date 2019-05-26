package fr.sushi.app.ui.checkout;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.sushi.app.R;
import fr.sushi.app.data.local.intentkey.IntentKey;
import fr.sushi.app.databinding.ActivityPaymentSuccssBinding;
import fr.sushi.app.ui.main.MainActivity;

public class PaymentSuccssActivity extends AppCompatActivity {
    private ActivityPaymentSuccssBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_succss);
        binding.tvClose.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        Intent intent = getIntent();
        String successCard = intent.getStringExtra(IntentKey.KEY_ORDER_ID);
        if (successCard != null) {
            binding.tvOrderId.setText("Votre commade #" + successCard + " a bien ete enregistree!!");
        }
    }
}
