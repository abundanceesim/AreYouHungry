package com.cst2335.areyouhungry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

public class SearchActivity extends AppCompatActivity {
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    EditText recentReceipes;

    public static final String Shared_prefs = "sharedprefs";
    public static final String TEXT = "Recent Receipes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Button searchBtn = findViewById(R.id.search_button);
        Toolbar tBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.recipe_activity);
        navigationView = findViewById(R.id.nav_view);
        recentReceipes=findViewById(R.id.search_recipe);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("Recipe Search Page");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

      searchBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // search code to display output of the receipe searched
          }
      });


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_item:
                    Intent goToHome = new Intent(this, HomeActivity.class);
                    startActivity(goToHome);
                    break;
                case R.id.favourites_item:
                    Intent goToFav = new Intent(this, FavouritesActivity.class);
                    startActivity(goToFav);
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        String receipeSearch=sharedPreferences.getString(TEXT,"");
        recentReceipes.setText(receipeSearch);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(TEXT,recentReceipes.getText().toString());
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_item) {
            Intent goToHome = new Intent(this, HomeActivity.class);
            startActivity(goToHome);
        } else if (id == R.id.search_item) {
            Intent goToSearch = new Intent(this, SearchActivity.class);
            startActivity(goToSearch);

        } else if (id == R.id.about_item) {
            AlertDialog.Builder alertD = new AlertDialog.Builder(this);
            alertD.setTitle("Extremely smart and handsome creators")
                    .setMessage("This app is a product of the minds of prodigies \n Abundance Esim, Harshang Shah & Shreyansh Kothari. \n We hope you enjoy and don't sleep on a hungry stomach :)")
                    .setNegativeButton("Okay, cool.", (v, args) -> {
                    })
                    .create().show();
        } else if (id == R.id.help_overflow) {
            AlertDialog.Builder alertD2 = new AlertDialog.Builder(this);
            alertD2.setTitle("Instructions")
                    .setMessage("The home icon sends you to the home page, if you're already on the home page, " + "\n " +
                            "then congrats, you've made it champ! " + "\n " +
                            "The search icon leads you to the page where you can search delicious recipes. \n " +
                            "The About option gives you information of the billionaires who made this app. \n " +
                            "The favourites button in the navigation drawer takes you to your favourite recipes again!")
                    .setNegativeButton("Okay, cool.", (v, args) -> {
                    })
                    .create().show();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}