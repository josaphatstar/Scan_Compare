package com.example.scan__compare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activity_verdict extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verdict);

        TextView tvWinnerName = findViewById(R.id.tvWinnerName);
        TextView tvWinnerPrice = findViewById(R.id.tvWinnerPrice);
        TextView tvLoserName = findViewById(R.id.tvLoserName);
        TextView tvLoserPrice = findViewById(R.id.tvLoserPrice);
        TextView tvSavingsLine1 = findViewById(R.id.tvSavingsLine1);
        TextView tvSavingsLine2 = findViewById(R.id.tvSavingsLine2);
        Button btnNewComparison = findViewById(R.id.btnNewComparison);

        Intent intent = getIntent();
        String winnerName = intent.getStringExtra("WINNER_NAME");
        double winnerPrice = intent.getDoubleExtra("WINNER_PRICE", 0.0);
        String loserName = intent.getStringExtra("LOSER_NAME");
        double loserPrice = intent.getDoubleExtra("LOSER_PRICE", 0.0);
        double savingsPercent = intent.getDoubleExtra("SAVINGS_PERCENT", 0.0);
        double moneySaved = intent.getDoubleExtra("MONEY_SAVED", 0.0);
        String unitLabel = intent.getStringExtra("UNIT_LABEL");
        String currency = intent.getStringExtra("CURRENCY");

        if (unitLabel == null) unitLabel = "kg";
        if (currency == null) currency = "€";

        if (winnerName != null) {
            tvWinnerName.setText(winnerName);
            tvWinnerPrice.setText(String.format("%.2f %s / %s", winnerPrice, currency, unitLabel));

            tvLoserName.setText(loserName);
            tvLoserPrice.setText(String.format("%.2f %s / %s", loserPrice, currency, unitLabel));

            tvSavingsLine1.setText(String.format("💡 Vous économisez %.1f%%", savingsPercent));
            tvSavingsLine2.setText(String.format("soit %.2f %s / %s avec le %s", moneySaved, currency, unitLabel, winnerName));
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnNewComparison.setOnClickListener(v -> {
            Intent mainIntent = new Intent(activity_verdict.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });
    }
}
