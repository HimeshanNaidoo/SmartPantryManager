# Smart Pantry Manager

Smart Pantry Manager is a Java Android application developed for Mobile App Development 700.

The app helps users reduce food waste by tracking ingredients currently available in their pantry and suggesting recipes that can be made using only those ingredients.

## Database

This project uses SQLite for local data storage.

SQLite was chosen because it provides persistent on-device storage, works without an internet connection,
and is suitable for storing pantry items and recipe data for this application.

Aplication uses the following main database tables such as:

- pantry_items
- recipes
- recipe_ingredients

## Current Features

- Add pantry ingredients
- Store ingredient name, quantity, unit, and optional expiry date
- Display pantry items using a RecyclerView
- Persistent SQLite storage
- Input validation
- Navigation between core application screens
- Expiry date format and calendar validation
- There is navigation between pantry, recipes and settings screens

## strict Recipe Matching

A recipe is only suggested when every required ingredient is present in the user's pantry in a sufficient quantity.

For example, if a recipe requires:

- 2 Eggs
- 50 g Cheese
- 30 ml Milk

recipe will only appear if all three requirments are satisfied.

Matching system also handles basic real- world differendces such as:

- Tomato and Tomatoes
- Egg and Eggs
- Compatible measurement units such as L and ml

## Setup and Run

1. Open the project in Android Studio.
2. Allow Gradle to sync.
3. Start an Android emulator or connect an Android device.
4. Run the app configuration.
5. The application will launch on the selected device.

## Technology

- Java
- Android Studio
- SQLite
- RecyclerView
- XML layouts

## Main Screens

- Pantry
- Add/Edit Ingredient
- Suggested Recipes
- Recipe Detail
- Settings

## Purpose

The purpoes of Smart Pantry manger is to help users make use of the current ingredients and redecue unncessary perchases.
