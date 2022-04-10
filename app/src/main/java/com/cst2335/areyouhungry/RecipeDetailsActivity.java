package com.cst2335.areyouhungry;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * This class would contains the frame layout which would be replaced by the recipe details from the API results.
 * @author Abundance Esim
 * @version 1.0.0
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //getting results from search activity
        Intent fromSearchActivity = getIntent();
        String recipeTitle = fromSearchActivity.getStringExtra("title");
        String recipeURL = fromSearchActivity.getStringExtra("url");
        String recipeIngredients = fromSearchActivity.getStringExtra("ingredients");

        //starting fragment transaction to replace frame layout with fragment
        RecipeFragment recipeFragment = new RecipeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame2, recipeFragment);    // add Fragment
        ft.commit();

        //sending arguments to fragment to be used to edit layout
        Bundle bundle = new Bundle();
        bundle.putString("title", recipeTitle);
        bundle.putString("url", recipeURL);
        bundle.putString("ingredients", recipeIngredients);
        recipeFragment.setArguments(bundle);
    }
}