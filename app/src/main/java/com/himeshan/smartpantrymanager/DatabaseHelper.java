package com.himeshan.smartpantrymanager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;


    private static final String TABLE_PANTRY = "pantry_items";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";

    private static final String TABLE_RECIPES = "recipes";

    private static final String RECIPE_COLUMN_ID = "id";
    private static final String RECIPE_COLUMN_NAME = "name";
    private static final String RECIPE_COLUMN_INSTRUCTIONS = "instructions";


    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    private static final String RI_COLUMN_ID = "id";
    private static final String RI_COLUMN_RECIPE_ID = "recipe_id";
    private static final String RI_COLUMN_INGREDIENT_NAME = "ingredient_name";
    private static final String RI_COLUMN_QUANTITY = "quantity_required";
    private static final String RI_COLUMN_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private void createRecipeTable(SQLiteDatabase db) {

        String createRecipeTable =
                "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                        RECIPE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_COLUMN_NAME + " TEXT NOT NULL, " +
                        RECIPE_COLUMN_INSTRUCTIONS + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipeTable);
    }

    private void createRecipeIngredientTable(SQLiteDatabase db) {

        String createRecipeIngredientTable =
                "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_INGREDIENTS + " (" +
                        RI_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RI_COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        RI_COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        RI_COLUMN_QUANTITY + " REAL NOT NULL, " +
                        RI_COLUMN_UNIT + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipeIngredientTable);
    }
    private long insertRecipe(
            SQLiteDatabase db,
            String name,
            String instructions
    ) {

        ContentValues values = new ContentValues();

        values.put(RECIPE_COLUMN_NAME, name);
        values.put(RECIPE_COLUMN_INSTRUCTIONS, instructions);

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    private void insertRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantityRequired,
            String unit
    ) {

        ContentValues values = new ContentValues();

        values.put(RI_COLUMN_RECIPE_ID, recipeId);
        values.put(RI_COLUMN_INGREDIENT_NAME, ingredientName);
        values.put(RI_COLUMN_QUANTITY, quantityRequired);
        values.put(RI_COLUMN_UNIT, unit);

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }
    private void addSeedRecipe(
            SQLiteDatabase db,
            String name,
            String instructions,
            String[] ingredientNames,
            double[] quantities,
            String[] units
    ) {
        long recipeId = insertRecipe(db, name, instructions);

        for (int i = 0; i < ingredientNames.length; i++) {
            insertRecipeIngredient(
                    db,
                    recipeId,
                    ingredientNames[i],
                    quantities[i],
                    units[i]
            );
        }
    }
    private void seedRecipes(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_RECIPES,
                null
        );

        int recipeCount = 0;

        if (cursor.moveToFirst()) {
            recipeCount = cursor.getInt(0);
        }

        cursor.close();


        if (recipeCount > 0) {
            return;
        }

        // 1. Cheese Omelette
        addSeedRecipe(
                db,
                "Cheese Omelette",
                "1. Beat the eggs and milk.\n" +
                        "2. Pour into a heated pan.\n" +
                        "3. Add cheese.\n" +
                        "4. Fold and cook until ready.",
                new String[]{"Eggs", "Cheese", "Milk"},
                new double[]{2, 50, 30},
                new String[]{"Item(s)", "g", "ml"}
        );

        // 2. Scrambled Eggs
        addSeedRecipe(
                db,
                "Scrambled Eggs",
                "1. Beat the eggs and milk.\n" +
                        "2. Melt butter in a pan.\n" +
                        "3. Add eggs and stir until cooked.",
                new String[]{"Eggs", "Milk", "Butter"},
                new double[]{2, 30, 10},
                new String[]{"Item(s)", "ml", "g"}
        );

        // 3. Tomato Sandwich
        addSeedRecipe(
                db,
                "Tomato Sandwich",
                "1. Butter the bread.\n" +
                        "2. Slice the tomato.\n" +
                        "3. Add tomato and cheese.\n" +
                        "4. Close the sandwich.",
                new String[]{"Bread", "Tomatoes", "Cheese", "Butter"},
                new double[]{2, 1, 30, 10},
                new String[]{"Item(s)", "Item(s)", "g", "g"}
        );

        // 4. Grilled Cheese Sandwich
        addSeedRecipe(
                db,
                "Grilled Cheese Sandwich",
                "1. Butter the bread.\n" +
                        "2. Add cheese between the slices.\n" +
                        "3. Grill until golden and the cheese melts.",
                new String[]{"Bread", "Cheese", "Butter"},
                new double[]{2, 50, 10},
                new String[]{"Item(s)", "g", "g"}
        );

        // 5. Tuna Sandwich
        addSeedRecipe(
                db,
                "Tuna Sandwich",
                "1. Mix tuna and mayonnaise.\n" +
                        "2. Spread onto bread.\n" +
                        "3. Close the sandwich and serve.",
                new String[]{"Bread", "Tuna", "Mayonnaise"},
                new double[]{2, 100, 1},
                new String[]{"Item(s)", "g", "Tablespoon(s)"}
        );

        // 6. Chicken Salad
        addSeedRecipe(
                db,
                "Chicken Salad",
                "1. Cook and slice the chicken.\n" +
                        "2. Chop the vegetables.\n" +
                        "3. Combine everything and serve.",
                new String[]{"Chicken", "Lettuce", "Tomatoes", "Cucumber"},
                new double[]{150, 100, 1, 1},
                new String[]{"g", "g", "Item(s)", "Item(s)"}
        );

        // 7. Pasta with Tomato Sauce
        addSeedRecipe(
                db,
                "Pasta with Tomato Sauce",
                "1. Cook the pasta.\n" +
                        "2. Fry onion and garlic.\n" +
                        "3. Add chopped tomatoes.\n" +
                        "4. Combine with pasta.",
                new String[]{"Pasta", "Tomatoes", "Onion", "Garlic"},
                new double[]{200, 2, 1, 2},
                new String[]{"g", "Item(s)", "Item(s)", "Item(s)"}
        );

        // 8. Chicken Pasta
        addSeedRecipe(
                db,
                "Chicken Pasta",
                "1. Cook the pasta.\n" +
                        "2. Cook chicken and garlic.\n" +
                        "3. Add cream.\n" +
                        "4. Combine with pasta.",
                new String[]{"Pasta", "Chicken", "Cream", "Garlic"},
                new double[]{200, 150, 100, 2},
                new String[]{"g", "g", "ml", "Item(s)"}
        );

        // 9. Vegetable Stir Fry
        addSeedRecipe(
                db,
                "Vegetable Stir Fry",
                "1. Chop the vegetables.\n" +
                        "2. Stir-fry until tender.\n" +
                        "3. Add soy sauce and mix.",
                new String[]{"Carrot", "Bell Pepper", "Broccoli", "Soy Sauce"},
                new double[]{1, 1, 150, 2},
                new String[]{"Item(s)", "Item(s)", "g", "Tablespoon(s)"}
        );

        // 10. Chicken Stir Fry
        addSeedRecipe(
                db,
                "Chicken Stir Fry",
                "1. Slice and cook the chicken.\n" +
                        "2. Add chopped vegetables.\n" +
                        "3. Add soy sauce and stir-fry.",
                new String[]{"Chicken", "Carrot", "Bell Pepper", "Soy Sauce"},
                new double[]{150, 1, 1, 2},
                new String[]{"g", "Item(s)", "Item(s)", "Tablespoon(s)"}
        );

        // 11. Fried Rice
        addSeedRecipe(
                db,
                "Fried Rice",
                "1. Cook the rice.\n" +
                        "2. Scramble the eggs.\n" +
                        "3. Add vegetables and rice.\n" +
                        "4. Add soy sauce and fry.",
                new String[]{"Rice", "Eggs", "Carrot", "Peas", "Soy Sauce"},
                new double[]{250, 2, 1, 100, 2},
                new String[]{"g", "Item(s)", "Item(s)", "g", "Tablespoon(s)"}
        );

        // 12. Pancakes
        addSeedRecipe(
                db,
                "Pancakes",
                "1. Mix flour, milk, eggs and sugar.\n" +
                        "2. Pour portions into a hot pan.\n" +
                        "3. Cook both sides until golden.",
                new String[]{"Flour", "Milk", "Eggs", "Sugar"},
                new double[]{200, 250, 2, 2},
                new String[]{"g", "ml", "Item(s)", "Tablespoon(s)"}
        );

        // 13. French Toast
        addSeedRecipe(
                db,
                "French Toast",
                "1. Beat eggs, milk and sugar.\n" +
                        "2. Dip bread into the mixture.\n" +
                        "3. Fry until golden.",
                new String[]{"Bread", "Eggs", "Milk", "Sugar"},
                new double[]{2, 2, 100, 1},
                new String[]{"Item(s)", "Item(s)", "ml", "Tablespoon(s)"}
        );

        // 14. Banana Smoothie
        addSeedRecipe(
                db,
                "Banana Smoothie",
                "1. Add all ingredients to a blender.\n" +
                        "2. Blend until smooth.\n" +
                        "3. Serve immediately.",
                new String[]{"Banana", "Milk", "Yogurt", "Honey"},
                new double[]{1, 250, 100, 1},
                new String[]{"Item(s)", "ml", "g", "Tablespoon(s)"}
        );

        // 15. Fruit Salad
        addSeedRecipe(
                db,
                "Fruit Salad",
                "1. Wash and chop the fruit.\n" +
                        "2. Combine in a bowl.\n" +
                        "3. Mix gently and serve.",
                new String[]{"Apple", "Banana", "Orange", "Grapes"},
                new double[]{1, 1, 1, 100},
                new String[]{"Item(s)", "Item(s)", "Item(s)", "g"}
        );

        // 16. Baked Potato
        addSeedRecipe(
                db,
                "Baked Potato",
                "1. Bake the potatoes until soft.\n" +
                        "2. Cut them open.\n" +
                        "3. Add butter and cheese.",
                new String[]{"Potatoes", "Butter", "Cheese"},
                new double[]{2, 20, 50},
                new String[]{"Item(s)", "g", "g"}
        );

        // 17. Mashed Potatoes
        addSeedRecipe(
                db,
                "Mashed Potatoes",
                "1. Boil potatoes until soft.\n" +
                        "2. Drain and mash.\n" +
                        "3. Mix in milk and butter.",
                new String[]{"Potatoes", "Milk", "Butter"},
                new double[]{4, 150, 30},
                new String[]{"Item(s)", "ml", "g"}
        );

        // 18. Tomato Soup
        addSeedRecipe(
                db,
                "Tomato Soup",
                "1. Fry onion and garlic.\n" +
                        "2. Add tomatoes and stock.\n" +
                        "3. Simmer until cooked.\n" +
                        "4. Blend until smooth.",
                new String[]{"Tomatoes", "Onion", "Garlic", "Stock"},
                new double[]{4, 1, 2, 500},
                new String[]{"Item(s)", "Item(s)", "Item(s)", "ml"}
        );

        // 19. Chicken Wrap
        addSeedRecipe(
                db,
                "Chicken Wrap",
                "1. Cook and slice the chicken.\n" +
                        "2. Add chicken and vegetables to tortillas.\n" +
                        "3. Roll tightly and serve.",
                new String[]{"Tortilla", "Chicken", "Lettuce", "Tomatoes"},
                new double[]{2, 150, 50, 1},
                new String[]{"Item(s)", "g", "g", "Item(s)"}
        );

        // 20. Yogurt Parfait
        addSeedRecipe(
                db,
                "Yogurt Parfait",
                "1. Add yogurt to a bowl or glass.\n" +
                        "2. Add granola and sliced banana.\n" +
                        "3. Drizzle with honey.",
                new String[]{"Yogurt", "Granola", "Banana", "Honey"},
                new double[]{200, 50, 1, 1},
                new String[]{"g", "g", "Item(s)", "Tablespoon(s)"}
        );
    }

    private String normalizeIngredientName(String name) {

        String normalized =
                name.trim()
                        .toLowerCase(Locale.ROOT)
                        .replaceAll("\\s+", " ");


        if (normalized.endsWith("oes")
                && normalized.length() > 3) {

            normalized =
                    normalized.substring(
                            0,
                            normalized.length() - 2
                    );
        }


        if (normalized.endsWith("ies")
                && normalized.length() > 3) {

            normalized =
                    normalized.substring(
                            0,
                            normalized.length() - 3
                    ) + "y";
        }


        if (normalized.endsWith("s")
                && !normalized.endsWith("ss")
                && normalized.length() > 1) {

            normalized =
                    normalized.substring(
                            0,
                            normalized.length() - 1
                    );
        }

        return normalized;
    }


    private String getUnitCategory(String unit) {

        String normalizedUnit =
                unit.trim().toLowerCase(Locale.ROOT);

        switch (normalizedUnit) {

            case "g":
            case "kg":
                return "mass";

            case "ml":
            case "l":
            case "tablespoon(s)":
            case "teaspoon(s)":
            case "cup(s)":
                return "volume";

            case "item(s)":
                return "count";

            default:
                return normalizedUnit;
        }
    }

    private double convertToBaseUnit(
            double quantity,
            String unit
    ) {

        String normalizedUnit =
                unit.trim().toLowerCase(Locale.ROOT);

        switch (normalizedUnit) {

            case "kg":
                return quantity * 1000;

            case "g":
                return quantity;

            case "l":
                return quantity * 1000;

            case "ml":
                return quantity;

            case "cup(s)":
                return quantity * 250;

            case "tablespoon(s)":
                return quantity * 15;

            case "teaspoon(s)":
                return quantity * 5;

            case "item(s)":
                return quantity;

            default:
                return quantity;
        }
    }
    public List<Recipe> getMatchingRecipes() {

        List<Recipe> matchingRecipes =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Map<String, Double> pantryTotals =
                new HashMap<>();


        Cursor pantryCursor = db.query(
                TABLE_PANTRY,
                new String[]{
                        COLUMN_NAME,
                        COLUMN_QUANTITY,
                        COLUMN_UNIT
                },
                null,
                null,
                null,
                null,
                null
        );

        while (pantryCursor.moveToNext()) {

            String name =
                    pantryCursor.getString(
                            pantryCursor.getColumnIndexOrThrow(
                                    COLUMN_NAME
                            )
                    );

            double quantity =
                    pantryCursor.getDouble(
                            pantryCursor.getColumnIndexOrThrow(
                                    COLUMN_QUANTITY
                            )
                    );

            String unit =
                    pantryCursor.getString(
                            pantryCursor.getColumnIndexOrThrow(
                                    COLUMN_UNIT
                            )
                    );

            String key =
                    normalizeIngredientName(name)
                            + "|"
                            + getUnitCategory(unit);

            double baseQuantity =
                    convertToBaseUnit(
                            quantity,
                            unit
                    );

            double existingQuantity =
                    pantryTotals.containsKey(key)
                            ? pantryTotals.get(key)
                            : 0;

            pantryTotals.put(
                    key,
                    existingQuantity + baseQuantity
            );
        }

        pantryCursor.close();

        // Load  recipes
        Cursor recipeCursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_COLUMN_NAME + " ASC"
        );

        while (recipeCursor.moveToNext()) {

            int recipeId =
                    recipeCursor.getInt(
                            recipeCursor.getColumnIndexOrThrow(
                                    RECIPE_COLUMN_ID
                            )
                    );

            String recipeName =
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow(
                                    RECIPE_COLUMN_NAME
                            )
                    );

            String instructions =
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow(
                                    RECIPE_COLUMN_INSTRUCTIONS
                            )
                    );

            boolean canMakeRecipe = true;

            Cursor ingredientCursor = db.query(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    RI_COLUMN_RECIPE_ID + " = ?",
                    new String[]{
                            String.valueOf(recipeId)
                    },
                    null,
                    null,
                    null
            );

            while (ingredientCursor.moveToNext()) {

                String ingredientName =
                        ingredientCursor.getString(
                                ingredientCursor.getColumnIndexOrThrow(
                                        RI_COLUMN_INGREDIENT_NAME
                                )
                        );

                double quantityRequired =
                        ingredientCursor.getDouble(
                                ingredientCursor.getColumnIndexOrThrow(
                                        RI_COLUMN_QUANTITY
                                )
                        );

                String requiredUnit =
                        ingredientCursor.getString(
                                ingredientCursor.getColumnIndexOrThrow(
                                        RI_COLUMN_UNIT
                                )
                        );

                String key =
                        normalizeIngredientName(
                                ingredientName
                        )
                                + "|"
                                + getUnitCategory(
                                requiredUnit
                        );

                double availableQuantity =
                        pantryTotals.containsKey(key)
                                ? pantryTotals.get(key)
                                : 0;

                double requiredQuantity =
                        convertToBaseUnit(
                                quantityRequired,
                                requiredUnit
                        );



                if (availableQuantity < requiredQuantity) {

                    canMakeRecipe = false;
                    break;
                }
            }

            ingredientCursor.close();

            if (canMakeRecipe) {

                matchingRecipes.add(
                        new Recipe(
                                recipeId,
                                recipeName,
                                instructions
                        )
                );
            }
        }

        recipeCursor.close();
        db.close();

        return matchingRecipes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT" +
                        ")";

        db.execSQL(createPantryTable);
        createRecipeTable(db);
        createRecipeIngredientTable(db);
    }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (oldVersion < 2) {
                createRecipeTable(db);
                createRecipeIngredientTable(db);
            }
        }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        seedRecipes(db);
    }

    public long addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, ingredient.getName());
        values.put(COLUMN_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_UNIT, ingredient.getUnit());
        values.put(COLUMN_EXPIRY_DATE, ingredient.getExpiryDate());

        long result = db.insert(TABLE_PANTRY, null, values);

        db.close();

        return result;
    }


    public List<Ingredient> getAllIngredients() {

        List<Ingredient> ingredientList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
                );

                Ingredient ingredient = new Ingredient(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                ingredientList.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredientList;
    }

    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {

        List<RecipeIngredient> recipeIngredients =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RI_COLUMN_RECIPE_ID + " = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    RI_COLUMN_ID
                            )
                    );

            String ingredientName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    RI_COLUMN_INGREDIENT_NAME
                            )
                    );

            double quantityRequired =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    RI_COLUMN_QUANTITY
                            )
                    );

            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    RI_COLUMN_UNIT
                            )
                    );

            RecipeIngredient ingredient =
                    new RecipeIngredient(
                            id,
                            recipeId,
                            ingredientName,
                            quantityRequired,
                            unit
                    );

            recipeIngredients.add(ingredient);
        }

        cursor.close();
        db.close();

        return recipeIngredients;
    }
    public int updateIngredient(Ingredient ingredient) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, ingredient.getName());
        values.put(COLUMN_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_UNIT, ingredient.getUnit());
        values.put(COLUMN_EXPIRY_DATE, ingredient.getExpiryDate());

        int result = db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(ingredient.getId())}
        );

        db.close();

        return result;
    }


    public int deleteIngredient(int ingredientId) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(ingredientId)}
        );

        db.close();

        return result;
    }
}