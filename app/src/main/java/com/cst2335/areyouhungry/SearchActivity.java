package com.cst2335.areyouhungry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public static final String Shared_prefs = "sharedprefs";
    public static final String TEXT = "Search ";
    EditText searchedRecipe;
    Button searchButton;
    TextView resultText;
    RecipeAdapter myAdapter;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();  //array list that would contain messages.
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar tBar = findViewById(R.id.toolBar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("Recipe Search Page");

        searchedRecipe = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        resultText = findViewById(R.id.queryResult);

        myList = findViewById(R.id.listView);
        myList.setAdapter( myAdapter = new RecipeAdapter());

        /** When the search button is clicked, fetch results for searched recipe.*/
        searchButton.setOnClickListener(click -> {
            recipes.clear();
            searchedRecipe = findViewById(R.id.searchText);
            String search = searchedRecipe.getText().toString();
            String searchLowerCase = search.toLowerCase();
            Toast.makeText(SearchActivity.this, "Searching for " + searchLowerCase + " recipes...", Toast.LENGTH_SHORT).show();
            //modification starts here:
            String searchQuery = "https://api.edamam.com/search?app_id=bd8ab790&app_key=466eca9597ecd8b4fbedecfaca1c6250&q=" + searchLowerCase; //modified
            System.err.println("Search query:" + searchQuery);

            String searchURL = searchQuery.trim();
            System.err.println("Searched string(in on Create):" + search);
            new SearchTask().execute(searchURL);

            if(recipes.size() <= 0){
                resultText.setText("No results");
            }
        });

        /**When a list item is clicked on, start Recipe Details Activity*/
        myList.setOnItemClickListener((list, view, position, id) -> {
            Recipe recipe = recipes.get(position);
            id = recipes.indexOf(recipe);
            String title = recipe.getTitle();
            String url = recipe.getUrl();
            String ingredients = recipe.getIngredients();

            Intent goToFragment = new Intent(this, RecipeDetailsActivity.class);
            goToFragment.putExtra("title", title);
            goToFragment.putExtra("url", url);
            goToFragment.putExtra("ingredients", ingredients);
            startActivity(goToFragment);

        });

    }

    /** Retrieve searched recipe keyword from SharedPreferences file.*/
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        String recentRecipe = sharedPreferences.getString(TEXT, "");
        searchedRecipe.setText(recentRecipe);
    }

    /** Store searched recipe keyword in SharedPreferences file.*/
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, searchedRecipe.getText().toString());
        editor.apply();
    }

    /** Inflate Toolbar menu.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /** This method is executed each time a Toolbar menu item is selected*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.home_item:
                message = "Already in Home";
                Intent goToHome = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(goToHome);
                break;
            case R.id.search_item:
                message = "Already in Recipe Search Page";
                break;
            case R.id.recipe_item:
                message = "Redirecting to Recipe details page";
                Intent goToDetails = new Intent(SearchActivity.this, RecipeDetailsActivity.class);
                startActivity(goToDetails);
                break;
            case R.id.favourites_item:
                message = "Redirecting to Favourites";
                Intent goToFavourites = new Intent(SearchActivity.this, FavouritesActivity.class);
                startActivity(goToFavourites);
                break;

            case R.id.help_overflow:
                //remember to make the snackbar
                //Snackbar snack = Snackbar.make(R.id.help_overflow, "Help menu clicked", Snackbar.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Help")

                        .setMessage("Welcome to the Recipe Search Page! \nINSTRUCTIONS:\n• Here you would enter a keyword you would like your recipe title to contain into the text box below " +
                                "and click on the search button. \n• Similar recipes would be displayed in the list below. "+
                                "\n• Your last searched recipe would be saved for use the next time you open the app.")
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

    /**
     * SearchTask class that handles fetching and retrieval of recipes containing searched keyword.
     * @author Abundance Esim
     * @version 1.0.0
     */
    class SearchTask extends AsyncTask<String, Integer, String> {
        static private final String TAG = "SearchTask";

        //Type3                Type1
        public String doInBackground(String... args) {
            try {

                URL url = new URL(args[0]);//create a URL object of what server to contact:
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();//open the connection
                InputStream response = urlConnection.getInputStream();//wait for data:

                //JSON reading. Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                myList = findViewById(R.id.listView);
                myList.setAdapter( myAdapter = new RecipeAdapter());

                EditText searchedRecipe = (EditText)findViewById(R.id.searchText);

                JSONObject uvReport = new JSONObject(result); // convert string to JSON: Look at slide 27:
                System.out.println(uvReport.toString());

                String whatWasTyped = uvReport.getString("q");
                JSONArray resultsArray = uvReport.getJSONArray("hits");  //JSONArray resultsArray = new JSONArray(result);

                for(int i = 0; i < resultsArray.length(); i++){
                    JSONObject resultsObject = resultsArray.getJSONObject(i);

                    JSONObject recipeObject = resultsObject.getJSONObject("recipe");

                    String recipeTitle = recipeObject.getString("label");
                    String recipeURL = recipeObject.getString("url");
                    JSONArray ingredientsArray = recipeObject.getJSONArray("ingredientLines");

                    String[] ingredientLines = new String[ingredientsArray.length()];

                    resultText.setText("Recipe found");
                    Log.i(TAG, "What was searched: " + whatWasTyped) ;
                    Log.i(TAG, "Recipe found: " + recipeTitle) ;
                    for(int j = 0; j < ingredientsArray.length(); j++){
                        ingredientLines[j] = "○ " + ingredientsArray.getString(j).trim();
                        Log.i(TAG, "Recipe ingredients line " + (j+1) + ": "+ ingredientLines[j]);
                    }
                    String ingredientsConcat = String.join("\n", ingredientLines);
                    System.out.println("Concatenated ingredients: " + ingredientsConcat);

                    recipes.add(new Recipe(recipeTitle, recipeURL, ingredientsConcat));//add recipe title, url and concatenated ingredients to recipes list.
                    myAdapter.notifyDataSetChanged();

                }
                publishProgress(25);
                Thread.sleep(1000);
                publishProgress(50);

            } catch (Exception e) {
                //e.printStackTrace();
            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer... args) {
            Log.i(TAG, "onProgressUpdate");
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {

            Log.i(TAG, "String gotten is: " + fromDoInBackground);
        }

    }

    /**
     * Recipe Adapter class for the inflation of each list entry.
     * @author Abundance Esim
     * @version 1.0.0
     * */
    private class RecipeAdapter extends BaseAdapter {

        @Override
        public int getCount() { return recipes.size(); }

        @Override
        public Object getItem(int position) {
            Recipe thisRow = recipes.get(position);
            return thisRow.title;
            //return thisRow;
        }

        @Override
        public long getItemId(int position) { return (long) position; }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View recipeView = inflater.inflate(R.layout.recipe_list_layout, parent, false);
            TextView recipeTitle = recipeView.findViewById(R.id.recipe_title);
            recipeTitle.setText(getItem(position).toString());

            return recipeView;
        }
    }

    /**
     * Recipe class for creating recipe objects that would be added to the list.
     * @author Abundance Esim
     * @version 1.0.0
     */
    public class Recipe {
        String title;
        String url;
        String ingredients;
        //long id;

        public Recipe(String title, String url, String ingredients/*, long id*/) {
            this.title = title;
            this.url = url;
            this.ingredients = ingredients;
            //this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIngredients() {
            return ingredients;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }


        /*public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }*/
    }

}