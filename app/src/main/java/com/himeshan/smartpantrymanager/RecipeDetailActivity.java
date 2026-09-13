package com.himeshan.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeInstructions;
    private Button btnBack;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        tvRecipeName =
                findViewById(R.id.tvRecipeName);

        tvRecipeIngredients =
                findViewById(R.id.tvRecipeIngredients);

        tvRecipeInstructions =
                findViewById(R.id.tvRecipeInstructions);

        btnBack =
                findViewById(R.id.btnBack);

        databaseHelper =
                new DatabaseHelper(this);


        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        String recipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        String recipeInstructions =
                getIntent().getStringExtra(
                        "recipe_instructions"
                );

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Unable to load recipe",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }


        tvRecipeName.setText(recipeName);


        tvRecipeInstructions.setText(
                recipeInstructions
        );


        List<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(
                        recipeId
                );

        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient.getIngredientName())
                    .append(" — ")
                    .append(formatQuantity(
                            ingredient.getQuantityRequired()
                    ))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        tvRecipeIngredients.setText(
                ingredientText.toString().trim()
        );


        btnBack.setOnClickListener(v ->
                finish()
        );
    }

    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {
            return String.valueOf(
                    (long) quantity
            );
        }

        return String.valueOf(quantity);
    }
}