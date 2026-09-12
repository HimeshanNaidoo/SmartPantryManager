package com.himeshan.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;
    private final OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(
            List<Recipe> recipeList,
            OnRecipeClickListener listener
    ) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {

        Recipe recipe = recipeList.get(position);

        holder.tvRecipeName.setText(
                recipe.getName()
        );

        holder.tvRecipeStatus.setText(
                "You have all required ingredients"
        );

        holder.btnViewRecipe.setOnClickListener(v ->
                listener.onRecipeClick(recipe)
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeStatus;
        Button btnViewRecipe;

        public RecipeViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );

            tvRecipeStatus =
                    itemView.findViewById(
                            R.id.tvRecipeStatus
                    );

            btnViewRecipe =
                    itemView.findViewById(
                            R.id.btnViewRecipe
                    );
        }
    }
}
