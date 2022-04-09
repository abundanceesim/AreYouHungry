package com.cst2335.areyouhungry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * Recipe Fragment class which would contain information about each recipe.
 * @author Abundance Esim
 * @version 1.0.0
 */
public class RecipeFragment extends Fragment {

    Toolbar tBar;
    Switch fav_switch;
    TextView recipeTitle;
    String title;
    TextView recipeURL;
    String url;
    TextView recipeIngredients;
    String ingredients;
    boolean isFavourite;
    Button URLButton;

    String Shared_prefs = "SharedPreferencesRecipeDetails";
    String TITLE = "title";
    String INGREDIENTS = "ingredients";
    String URL = "url";

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SharedPreferences preferences = context.getSharedPreferences(Shared_prefs, Context.MODE_PRIVATE);
    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        tBar = (Toolbar)view.findViewById(R.id.toolBar);
        fav_switch = (Switch)view.findViewById(R.id.fav_switch);
        recipeTitle = (TextView)view.findViewById(R.id.recipe_text) ;
        recipeURL = (TextView)view.findViewById(R.id.recipe_url);
        recipeIngredients = (TextView)view.findViewById(R.id.ingredients_text);
        URLButton = (Button)view.findViewById(R.id.goToURL);

        tBar.setTitle("Recipe Details v1.0.0 by Abundance");

        if(savedInstanceState == null) {
            // Get back arguments that were passed into the Bundle
            if (getArguments() != null) {
                title = getArguments().getString("title", "");
                recipeTitle.setText(title);

                url = getArguments().getString("url", "");
                recipeURL.setText("Recipe URL: " + url);

                ingredients = getArguments().getString("ingredients", "");
                recipeIngredients.setText(ingredients);

                /*If the strings aren't empty, put them in the shared preferences file */
                if((!title.isEmpty() ) && (!ingredients.isEmpty()) && (!url.isEmpty())){
                    SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Shared_prefs, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TITLE, title);
                    editor.putString(INGREDIENTS, ingredients);
                    editor.putString(URL, url);
                    editor.apply();
                }

            }
        }

        URLButton.setOnClickListener( click ->{
            url = getArguments().getString("url", "");
            Intent goToURL = new Intent(Intent.ACTION_VIEW);
            goToURL.setData( Uri.parse(url) );
            startActivity(goToURL);
        });

        fav_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                isFavourite = true;
                Snackbar snack = Snackbar.make(fav_switch, "Added to favourites", Snackbar.LENGTH_SHORT);
                snack.show();
                //Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Removed from favourites", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Shared_prefs, Context.MODE_PRIVATE);
        String recentTitle = sharedPreferences.getString(TITLE, "");
        String recentIngredients = sharedPreferences.getString(INGREDIENTS, "");
        String recentURL = sharedPreferences.getString(URL, "");
        if (recentTitle != null){
            recipeTitle.setText(recentTitle);
        }
        if (recentIngredients != null){
            recipeIngredients.setText(recentIngredients);
        }
        if (recipeURL != null){
            recipeURL.setText(recentURL);
        }

    }

}