package com.cst2335.areyouhungry;

import android.content.Intent;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

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
}