package com.himeshan.smartpantrymanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredientList;
    private final OnIngredientActionListener listener;
    private final boolean expiryAlertsEnabled;

    public interface OnIngredientActionListener {
        void onEditClick(Ingredient ingredient);

        void onDeleteClick(Ingredient ingredient);
    }

    public IngredientAdapter(
            List<Ingredient> ingredientList,
            boolean expiryAlertsEnabled,
            OnIngredientActionListener listener
    ) {
        this.ingredientList = ingredientList;
        this.expiryAlertsEnabled = expiryAlertsEnabled;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_ingredient,
                        parent,
                        false
                );

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position
    ) {

        Ingredient ingredient =
                ingredientList.get(position);


        holder.tvIngredientName.setText(
                ingredient.getName()
        );


        String quantityText =
                ingredient.getQuantity()
                        + " "
                        + ingredient.getUnit();

        holder.tvIngredientQuantity.setText(
                quantityText
        );


        String expiryDate =
                ingredient.getExpiryDate();


        holder.tvIngredientExpiry.setTextColor(
                Color.parseColor("#777777")
        );

        if (expiryDate == null || expiryDate.isEmpty()) {

            holder.tvIngredientExpiry.setText(
                    "Expiry: No expiry date"
            );

        } else if (!expiryAlertsEnabled) {

            holder.tvIngredientExpiry.setText(
                    "Expiry: " + expiryDate
            );

        } else {

            showExpiryStatus(
                    holder.tvIngredientExpiry,
                    expiryDate
            );
        }

        // Edit button
        holder.btnEditIngredient.setOnClickListener(v ->
                listener.onEditClick(ingredient)
        );

        // Delete button
        holder.btnDeleteIngredient.setOnClickListener(v ->
                listener.onDeleteClick(ingredient)
        );
    }

    private void showExpiryStatus(
            TextView textView,
            String expiryDate
    ) {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        dateFormat.setLenient(false);

        try {

            Date expiry =
                    dateFormat.parse(expiryDate);

            Date today =
                    dateFormat.parse(
                            dateFormat.format(
                                    new Date()
                            )
                    );

            if (expiry == null || today == null) {

                textView.setText(
                        "Expiry: " + expiryDate
                );

                return;
            }

            long difference =
                    expiry.getTime()
                            - today.getTime();

            long daysUntilExpiry =
                    TimeUnit.MILLISECONDS
                            .toDays(difference);

            if (daysUntilExpiry < 0) {

                textView.setText(
                        "Expiry: "
                                + expiryDate
                                + " • Expired"
                );

                textView.setTextColor(
                        Color.parseColor("#D32F2F")
                );

            } else if (daysUntilExpiry <= 3) {

                textView.setText(
                        "Expiry: "
                                + expiryDate
                                + " • Expires soon"
                );

                textView.setTextColor(
                        Color.parseColor("#F57C00")
                );

            } else {

                textView.setText(
                        "Expiry: " + expiryDate
                );
            }

        } catch (ParseException e) {

            textView.setText(
                    "Expiry: " + expiryDate
            );
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientQuantity;
        TextView tvIngredientExpiry;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public IngredientViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvIngredientName =
                    itemView.findViewById(
                            R.id.tvIngredientName
                    );

            tvIngredientQuantity =
                    itemView.findViewById(
                            R.id.tvIngredientQuantity
                    );

            tvIngredientExpiry =
                    itemView.findViewById(
                            R.id.tvIngredientExpiry
                    );

            btnEditIngredient =
                    itemView.findViewById(
                            R.id.btnEditIngredient
                    );

            btnDeleteIngredient =
                    itemView.findViewById(
                            R.id.btnDeleteIngredient
                    );
        }
    }
}