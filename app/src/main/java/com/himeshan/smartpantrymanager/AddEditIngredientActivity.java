package com.himeshan.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;
    private Spinner spinnerUnit;
    private Button btnSaveIngredient;
    private Button btnCancel;
    private TextView tvFormTitle;

    private DatabaseHelper databaseHelper;

    private boolean isEditMode = false;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);

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


        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        btnCancel =
                findViewById(R.id.btnCancel);

        tvFormTitle =
                findViewById(R.id.tvFormTitle);

        databaseHelper =
                new DatabaseHelper(this);


        String[] units = {
                "Select Unit",
                "g",
                "kg",
                "ml",
                "L",
                "Item(s)",
                "Cup(s)",
                "Tablespoon(s)",
                "Teaspoon(s)"
        };

        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(unitAdapter);


        if (getIntent().hasExtra("ingredient_id")) {

            isEditMode = true;

            ingredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String ingredientName =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double ingredientQuantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            0
                    );

            String ingredientUnit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String ingredientExpiry =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );


            tvFormTitle.setText("Edit Ingredient");

            btnSaveIngredient.setText(
                    "Update Ingredient"
            );


            etIngredientName.setText(
                    ingredientName
            );

            etQuantity.setText(
                    String.valueOf(
                            ingredientQuantity
                    )
            );

            if (ingredientExpiry != null) {
                etExpiryDate.setText(
                        ingredientExpiry
                );
            }


            for (int i = 0; i < units.length; i++) {

                if (units[i].equals(ingredientUnit)) {
                    spinnerUnit.setSelection(i);
                    break;
                }
            }
        }

        // Save / Update button
        btnSaveIngredient.setOnClickListener(
                v -> validateIngredient()
        );

        // Cancel
        btnCancel.setOnClickListener(
                v -> finish()
        );
    }

    private void validateIngredient() {

        String ingredientName =
                etIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity
                        .getText()
                        .toString()
                        .trim();

        // Validate ingredient name
        if (ingredientName.isEmpty()) {

            etIngredientName.setError(
                    "Ingredient name is required"
            );

            etIngredientName.requestFocus();

            return;
        }


        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Quantity is required"
            );

            etQuantity.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Enter a valid quantity"
            );

            etQuantity.requestFocus();

            return;
        }


        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than 0"
            );

            etQuantity.requestFocus();

            return;
        }


        if (spinnerUnit.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String unit =
                spinnerUnit
                        .getSelectedItem()
                        .toString();

        String expiryDate =
                etExpiryDate
                        .getText()
                        .toString()
                        .trim();


        if (isEditMode) {

            Ingredient ingredient =
                    new Ingredient(
                            ingredientId,
                            ingredientName,
                            quantity,
                            unit,
                            expiryDate.isEmpty()
                                    ? null
                                    : expiryDate
                    );

            int result =
                    databaseHelper
                            .updateIngredient(
                                    ingredient
                            );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }


        else {

            Ingredient ingredient =
                    new Ingredient(
                            ingredientName,
                            quantity,
                            unit,
                            expiryDate.isEmpty()
                                    ? null
                                    : expiryDate
                    );

            long result =
                    databaseHelper
                            .addIngredient(
                                    ingredient
                            );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient added successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to add ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}