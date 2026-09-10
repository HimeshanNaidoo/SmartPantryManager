package com.himeshan.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredientList;
    private final OnIngredientActionListener listener;

    public interface OnIngredientActionListener {
        void onEditClick(Ingredient ingredient);
        void onDeleteClick(Ingredient ingredient);
    }

    public IngredientAdapter(
            List<Ingredient> ingredientList,
            OnIngredientActionListener listener
    ) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position
    ) {

        Ingredient ingredient = ingredientList.get(position);

        holder.tvIngredientName.setText(ingredient.getName());

        String quantityText =
                ingredient.getQuantity() + " " + ingredient.getUnit();

        holder.tvIngredientQuantity.setText(quantityText);

        String expiryDate = ingredient.getExpiryDate();

        if (expiryDate == null || expiryDate.isEmpty()) {
            holder.tvIngredientExpiry.setText("Expiry: No expiry date");
        } else {
            holder.tvIngredientExpiry.setText("Expiry: " + expiryDate);
        }

        holder.btnEditIngredient.setOnClickListener(v ->
                listener.onEditClick(ingredient)
        );

        holder.btnDeleteIngredient.setOnClickListener(v ->
                listener.onDeleteClick(ingredient)
        );
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

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName =
                    itemView.findViewById(R.id.tvIngredientName);

            tvIngredientQuantity =
                    itemView.findViewById(R.id.tvIngredientQuantity);

            tvIngredientExpiry =
                    itemView.findViewById(R.id.tvIngredientExpiry);

            btnEditIngredient =
                    itemView.findViewById(R.id.btnEditIngredient);

            btnDeleteIngredient =
                    itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}