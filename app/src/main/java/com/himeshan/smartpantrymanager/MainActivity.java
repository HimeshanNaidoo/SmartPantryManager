package com.himeshan.smartpantrymanager;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
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


        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnPantry = findViewById(R.id.btnPantry);
        btnRecipes = findViewById(R.id.btnRecipes);
        btnSettings = findViewById(R.id.btnSettings);


        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);


        databaseHelper = new DatabaseHelper(this);


        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );


        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });


        btnPantry.setOnClickListener(v -> {

        });


        btnRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });


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

            SharedPreferences sharedPreferences =
                    getSharedPreferences(
                            "SmartPantrySettings",
                            MODE_PRIVATE
                    );
            boolean expiryAlertsEnabled =
                    sharedPreferences.getBoolean(
                            "expiry_alerts_enabled",
                            true
                    );
            ingredientAdapter = new IngredientAdapter(
                    ingredientList,
                    expiryAlertsEnabled,
                    new IngredientAdapter.OnIngredientActionListener() {

                        @Override
                        public void onEditClick(Ingredient ingredient) {

                            Intent intent = new Intent(
                                    MainActivity.this,
                                    AddEditIngredientActivity.class
                            );

                            intent.putExtra("ingredient_id", ingredient.getId());
                            intent.putExtra("ingredient_name", ingredient.getName());
                            intent.putExtra("ingredient_quantity", ingredient.getQuantity());
                            intent.putExtra("ingredient_unit", ingredient.getUnit());
                            intent.putExtra("ingredient_expiry", ingredient.getExpiryDate());

                            startActivity(intent);
                        }

                        @Override
                        public void onDeleteClick(Ingredient ingredient) {

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Delete Ingredient")
                                    .setMessage(
                                            "Are you sure you want to delete "
                                                    + ingredient.getName()
                                                    + "?"
                                    )
                                    .setPositiveButton("Delete", (dialog, which) -> {

                                        int result =
                                                databaseHelper.deleteIngredient(
                                                        ingredient.getId()
                                                );

                                        if (result > 0) {

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "Ingredient deleted successfully",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            loadPantryItems();

                                        } else {

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "Failed to delete ingredient",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    }
            );

            recyclerViewPantry.setAdapter(ingredientAdapter);
        }
    }
}