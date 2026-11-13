package com.example.scan__compare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            int basePadding = (int) (24 * v.getResources().getDisplayMetrics().density);

            v.setPadding(
                    basePadding + systemBars.left,
                    basePadding + systemBars.top,
                    basePadding + systemBars.right,
                    basePadding + systemBars.bottom
            );
            return insets;
        });

        // Configuration du Spinner de devise avec le design Figma
        Spinner spinnerCurrency = findViewById(R.id.spinnerCurrency);

        List<CurrencyItem> currencyList = new ArrayList<>();
        currencyList.add(new CurrencyItem("EUR • €", "Euro", "€"));
        currencyList.add(new CurrencyItem("USD • $", "Dollar américain", "$"));
        currencyList.add(new CurrencyItem("XOF • F CFA", "Franc CFA - Afrique de l'Ouest", "F CFA"));
        currencyList.add(new CurrencyItem("MAD • DH", "Dirham marocain", "DH"));

        CurrencyAdapter currencyAdapter = new CurrencyAdapter(this, currencyList);
        spinnerCurrency.setAdapter(currencyAdapter);

        // Charger la devise précédemment enregistrée
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedSymbol = prefs.getString("CURRENCY", "€");
        for (int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).symbol.equals(savedSymbol)) {
                spinnerCurrency.setSelection(i);
                currencyAdapter.setSelectedPosition(i);
                break;
            }
        }

        // Sauvegarder lors du changement de devise
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyItem selectedItem = currencyList.get(position);
                prefs.edit().putString("CURRENCY", selectedItem.symbol).apply();
                currencyAdapter.setSelectedPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        findViewById(R.id.cardPoids).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ComparisonActivity.class);
            intent.putExtra("CATEGORY", "POIDS");
            startActivity(intent);
        });

        findViewById(R.id.cardVolume).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ComparisonActivity.class);
            intent.putExtra("CATEGORY", "VOLUME");
            startActivity(intent);
        });

        findViewById(R.id.cardUnite).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ComparisonActivity.class);
            intent.putExtra("CATEGORY", "UNITE");
            startActivity(intent);
        });
    }
}
