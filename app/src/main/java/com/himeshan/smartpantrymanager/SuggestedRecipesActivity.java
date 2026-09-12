package com.himeshan.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView tvNoRecipes;

    private Button btnPantry;
    private Button btnRecipes;
    private Button btnSettings;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

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

        recyclerViewRecipes =
                findViewById(R.id.recyclerViewRecipes);

        tvNoRecipes =
                findViewById(R.id.tvNoRecipes);

        btnPantry =
                findViewById(R.id.btnPantry);

        btnRecipes =
                findViewById(R.id.btnRecipes);

        btnSettings =
                findViewById(R.id.btnSettings);

        databaseHelper =
                new DatabaseHelper(this);

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );


        btnPantry.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });


        btnRecipes.setOnClickListener(v -> {
            // No action required
        });


        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {

        List<Recipe> matchingRecipes =
                databaseHelper.getMatchingRecipes();

        if (matchingRecipes.isEmpty()) {

            recyclerViewRecipes.setVisibility(View.GONE);
            tvNoRecipes.setVisibility(View.VISIBLE);

        } else {

            recyclerViewRecipes.setVisibility(View.VISIBLE);
            tvNoRecipes.setVisibility(View.GONE);

            recipeAdapter = new RecipeAdapter(
                    matchingRecipes,
                    recipe -> {

                        Intent intent = new Intent(
                                SuggestedRecipesActivity.this,
                                RecipeDetailActivity.class
                        );

                        intent.putExtra(
                                "recipe_id",
                                recipe.getId()
                        );

                        intent.putExtra(
                                "recipe_name",
                                recipe.getName()
                        );

                        intent.putExtra(
                                "recipe_instructions",
                                recipe.getInstructions()
                        );

                        startActivity(intent);
                    }
            );

            recyclerViewRecipes.setAdapter(
                    recipeAdapter
            );
        }
    }
}