package com.cst2335.areyouhungry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent fromSearchActivity = getIntent();
        String recipeTitle = fromSearchActivity.getStringExtra("title");
        String recipeURL = fromSearchActivity.getStringExtra("url");


        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_frame, RecipeFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("recipe").commit();*/

        RecipeFragment recipeFragment = new RecipeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame2, recipeFragment);    // add Fragment
        ft.commit();

        Bundle bundle = new Bundle();
        bundle.putString("title", recipeTitle);
        bundle.putString("url", recipeURL);
        recipeFragment.setArguments(bundle);
    }
}