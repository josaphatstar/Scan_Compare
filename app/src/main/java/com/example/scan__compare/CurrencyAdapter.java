package com.example.scan__compare;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CurrencyAdapter extends ArrayAdapter<CurrencyItem> {
    private int selectedPosition = 0;

    public CurrencyAdapter(Context context, List<CurrencyItem> items) {
        super(context, 0, items);
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, true);
    }

    private View createView(int position, View convertView, ViewGroup parent, boolean isDropdown) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_currency_dropdown, parent, false);
        }

        CurrencyItem item = getItem(position);
        TextView tvTitle = convertView.findViewById(R.id.tvCurrencyTitle);
        TextView tvSubtitle = convertView.findViewById(R.id.tvCurrencySubtitle);
        ImageView ivCheck = convertView.findViewById(R.id.ivCheck);
        View rootLayout = convertView.findViewById(R.id.rootLayout);

        if (tvTitle != null && item != null) {
            if (isDropdown) {
                tvTitle.setText(item.title);
            } else {
                // Pour l'affichage compact dans le bouton du Spinner
                tvTitle.setText(item.title); // ou item.symbol
            }
        }

        if (tvSubtitle != null && item != null) {
            if (isDropdown) {
                tvSubtitle.setText(item.subtitle);
                tvSubtitle.setVisibility(View.VISIBLE);
            } else {
                // Masquer le sous-titre dans le bouton fermé du Spinner
                tvSubtitle.setVisibility(View.GONE);
            }
        }

        if (isDropdown) {
            if (position == selectedPosition) {
                rootLayout.setBackgroundColor(Color.parseColor("#ECFDF5")); // Vert clair de sélection
                if (ivCheck != null) {
                    ivCheck.setVisibility(View.VISIBLE);
                    ivCheck.setColorFilter(Color.parseColor("#059669")); // Coche verte
                }
            } else {
                rootLayout.setBackgroundColor(Color.TRANSPARENT);
                if (ivCheck != null) ivCheck.setVisibility(View.GONE);
            }
        } else {
            rootLayout.setBackgroundColor(Color.TRANSPARENT);
            if (ivCheck != null) ivCheck.setVisibility(View.GONE);
        }

        return convertView;
    }
}
