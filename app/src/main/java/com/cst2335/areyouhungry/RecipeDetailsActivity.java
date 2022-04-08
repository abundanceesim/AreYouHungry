package com.cst2335.areyouhungry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent fromSearchActivity = getIntent();
        String recipeTitle = fromSearchActivity.getStringExtra("title");


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_frame, RecipeFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("recipe").commit();
        Bundle bundle = new Bundle();
        bundle.putString("title", recipeTitle);

        fragmentManager.set
    }
}