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

public class MainActivity extends AppCompatActivity {

    private Button btnAddIngredient;
    private Button btnPantry;
    private Button btnRecipes;
    private Button btnSettings;

    private RecyclerView recyclerViewPantry;
    private TextView tvEmptyPantry;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

        // Connect buttons
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnPantry = findViewById(R.id.btnPantry);
        btnRecipes = findViewById(R.id.btnRecipes);
        btnSettings = findViewById(R.id.btnSettings);

        // Connect pantry list
        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);

        // Create database helper
        databaseHelper = new DatabaseHelper(this);

        // RecyclerView layout
        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Add Ingredient
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });


        btnPantry.setOnClickListener(v -> {
            // No action required
        });


        btnRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        // Settings
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPantryItems();
    }

    private void loadPantryItems() {

        List<Ingredient> ingredientList =
                databaseHelper.getAllIngredients();

        if (ingredientList.isEmpty()) {

            recyclerViewPantry.setVisibility(View.GONE);
            tvEmptyPantry.setVisibility(View.VISIBLE);

        } else {

            recyclerViewPantry.setVisibility(View.VISIBLE);
            tvEmptyPantry.setVisibility(View.GONE);

            ingredientAdapter =
                    new IngredientAdapter(ingredientList);

            recyclerViewPantry.setAdapter(ingredientAdapter);
        }
    }
}