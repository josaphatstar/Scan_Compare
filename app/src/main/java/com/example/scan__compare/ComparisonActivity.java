package com.example.scan__compare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ComparisonActivity extends AppCompatActivity {

    private String category = "POIDS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        if (getIntent() != null && getIntent().hasExtra("CATEGORY")) {
            category = getIntent().getStringExtra("CATEGORY");
        }

        TextView tvTitle = findViewById(R.id.tvTitle);
        if (category.equals("VOLUME")) {
            tvTitle.setText("Comparaison au Volume");
        } else if (category.equals("UNITE")) {
            tvTitle.setText("Comparaison à l'unité");
        } else {
            tvTitle.setText("Comparaison au Poids");
        }

        EditText etPriceA = findViewById(R.id.etPriceA);
        EditText etQuantityA = findViewById(R.id.etQuantityA);
        Spinner spinnerUnitA = findViewById(R.id.spinnerUnitA);

        EditText etPriceB = findViewById(R.id.etPriceB);
        EditText etQuantityB = findViewById(R.id.etQuantityB);
        Spinner spinnerUnitB = findViewById(R.id.spinnerUnitB);

        TextView tvLabelQtyA = findViewById(R.id.tvLabelQtyA);
        TextView tvLabelQtyB = findViewById(R.id.tvLabelQtyB);

        Button btnCompare = findViewById(R.id.btnCompare);

        String[] units;
        if (category.equals("VOLUME")) {
            units = new String[]{"ml", "L"};
        } else if (category.equals("UNITE")) {
            units = new String[]{"unité", "pack"};
        } else {
            units = new String[]{"g", "kg"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnitA.setAdapter(adapter);
        spinnerUnitB.setAdapter(adapter);
        spinnerUnitB.setSelection(1);

        // Dynamisme UX pour les packs
        if (category.equals("UNITE")) {
            spinnerUnitA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getItemAtPosition(position).toString();
                    if (selected.equals("pack")) {
                        tvLabelQtyA.setText("Unités par pack");
                    } else {
                        tvLabelQtyA.setText("Quantité");
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            spinnerUnitB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getItemAtPosition(position).toString();
                    if (selected.equals("pack")) {
                        tvLabelQtyB.setText("Unités par pack");
                    } else {
                        tvLabelQtyB.setText("Quantité");
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnCompare.setOnClickListener(v -> {
            String priceStrA = etPriceA.getText().toString().trim();
            String qtyStrA = etQuantityA.getText().toString().trim();

            String priceStrB = etPriceB.getText().toString().trim();
            String qtyStrB = etQuantityB.getText().toString().trim();

            String unitA = spinnerUnitA.getSelectedItem().toString();
            String unitB = spinnerUnitB.getSelectedItem().toString();

            if (priceStrA.isEmpty() || qtyStrA.isEmpty() || priceStrB.isEmpty() || qtyStrB.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double priceA = Double.parseDouble(priceStrA);
                double qtyA = Double.parseDouble(qtyStrA);
                double priceB = Double.parseDouble(priceStrB);
                double qtyB = Double.parseDouble(qtyStrB);

                double qtyInStdA, qtyInStdB;
                if (category.equals("UNITE")) {
                    qtyInStdA = qtyA;
                    qtyInStdB = qtyB;
                } else {
                    qtyInStdA = unitA.equals("g") || unitA.equals("ml") ? qtyA / 1000.0 : qtyA;
                    qtyInStdB = unitB.equals("g") || unitB.equals("ml") ? qtyB / 1000.0 : qtyB;
                }

                if (qtyInStdA <= 0 || qtyInStdB <= 0) {
                    Toast.makeText(this, "La quantité doit être supérieure à zéro", Toast.LENGTH_SHORT).show();
                    return;
                }

                double unitPriceA = priceA / qtyInStdA;
                double unitPriceB = priceB / qtyInStdB;

                String winnerName, loserName;
                double winnerPrice, loserPrice;

                if (unitPriceA < unitPriceB) {
                    winnerName = "Produit A";
                    winnerPrice = unitPriceA;
                    loserName = "Produit B";
                    loserPrice = unitPriceB;
                } else {
                    winnerName = "Produit B";
                    winnerPrice = unitPriceB;
                    loserName = "Produit A";
                    loserPrice = unitPriceA;
                }

                double savingsPercent = ((loserPrice - winnerPrice) / loserPrice) * 100.0;
                double moneySaved = loserPrice - winnerPrice;

                String unitLabel;
                if (category.equals("VOLUME")) {
                    unitLabel = "L";
                } else if (category.equals("UNITE")) {
                    unitLabel = "unité";
                } else {
                    unitLabel = "kg";
                }

                // Récupérer la devise choisie dans MainActivity via SharedPreferences
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                String currency = prefs.getString("CURRENCY", "€");

                Intent intent = new Intent(ComparisonActivity.this, activity_verdict.class);
                intent.putExtra("WINNER_NAME", winnerName);
                intent.putExtra("WINNER_PRICE", winnerPrice);
                intent.putExtra("LOSER_NAME", loserName);
                intent.putExtra("LOSER_PRICE", loserPrice);
                intent.putExtra("SAVINGS_PERCENT", savingsPercent);
                intent.putExtra("MONEY_SAVED", moneySaved);
                intent.putExtra("UNIT_LABEL", unitLabel);
                intent.putExtra("CURRENCY", currency);
                startActivity(intent);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Veuillez entrer des nombres valides", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
