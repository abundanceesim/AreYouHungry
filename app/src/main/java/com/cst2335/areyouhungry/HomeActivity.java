package com.cst2335.areyouhungry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    /**TO DO:
     * Make Snackbar work
     * Use Async Task to retrieve probably a title for the recipe app
     * Make frame layout and use it for the person's name or something. Create a fragment and pass the person's name into it
     * Add a login button that redirects the user to an activity(HomeFragment) which is an About page.
     * "Thank you so much for using our app, usersName! We really hope you enjoy the experience. Please do well to leave a rating for our app
     * on Play Store. Once again, thank you!
     * From the team at RecipeWorks.co"
     * */
    public static final String Shared_prefs = "SharedPreferencesHome";
    public static final String NAME = "username";
    Toolbar tBar;
    ProgressBar progressBar;
    Button goToSearchPage;
    int counter = 0;
    EditText name;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = findViewById(R.id.userName);

        tBar = findViewById(R.id.toolBar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("Home v1.0.0 by Abundance");

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(view ->{
            String message = null;
            switch (view.getItemId()){
                case R.id.home_item:
                   message = "Already in home";
                   break;
                case R.id.search_item:
                    message = "Redirecting to Search page";
                    Intent goToSearch = new Intent(HomeActivity.this, SearchActivity.class);
                    startActivity(goToSearch);
                    break;
                case R.id.recipe_item:
                    message = "Redirecting to Recipe details page";
                    Intent goToDetails = new Intent(HomeActivity.this, RecipeActivity.class);
                    startActivity(goToDetails);
                    break;
                case R.id.favourites_item:
                    message = "Redirecting to Favourites";
                    Intent goToFavourites = new Intent(HomeActivity.this, FavouritesActivity.class);
                    startActivity(goToFavourites);
                    break;
            }
            if ( message != null ) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
            return true;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goToSearchPage = findViewById(R.id.goToSearch);

        signUp = findViewById(R.id.signIn);
        signUp.setOnClickListener( click -> {
            name = findViewById(R.id.userName);
            String userName = name.getText().toString();
            Intent goToSignUpPage = new Intent(HomeActivity.this, SignUpActivity.class);
            goToSignUpPage.putExtra("username", userName);
            startActivity(goToSignUpPage);
        });

        goToSearchPage.setOnClickListener( click -> {
            updateProgress();
            Toast.makeText(this, "Loading complete", Toast.LENGTH_SHORT).show();
            Intent goToSearch = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(goToSearch);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        String recentName = sharedPreferences.getString(NAME, "");
        name.setText(recentName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, name.getText().toString());
        editor.apply();
    }

    public void updateProgress(){
        progressBar = findViewById(R.id.progressBar);
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter+=50;
                progressBar.setProgress(counter);
            }
        };
        t.schedule(tt, 0, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.home_item:
                message = "Already in Home";
                break;
            case R.id.search_item:
                message = "Redirecting to Search page";
                Intent goToSearch = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(goToSearch);
                break;
            case R.id.recipe_item:
                message = "Redirecting to Recipe details page";
                Intent goToDetails = new Intent(HomeActivity.this, RecipeActivity.class);
                startActivity(goToDetails);
                break;
            case R.id.favourites_item:
                message = "Redirecting to Favourites";
                Intent goToFavourites = new Intent(HomeActivity.this, FavouritesActivity.class);
                startActivity(goToFavourites);
                break;

            case R.id.help_overflow:
                //remember to make the snackbar
                //Snackbar snack = Snackbar.make(R.id.help_overflow, "Help menu clicked", Snackbar.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Help")

                        .setMessage("Welcome to the Home Page of our recipe app. \nThis page contains a toolbar for navigation across " +
                                "all pages. \nDo not forget to sign up using the text box and button below. ")
                        //Remove row from list and also delete it from the database.
                        .setPositiveButton("GOT IT", (click, arg) -> { })

                        //Display the Alert Dialog.
                        .create().show();

        }
        if ( message != null ) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        /*DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);*/
        return true;
    }

}