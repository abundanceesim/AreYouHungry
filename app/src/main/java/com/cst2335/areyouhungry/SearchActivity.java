package com.cst2335.areyouhungry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
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

        searchButton.setOnClickListener(click -> {
            recipes.clear();
            searchedRecipe = findViewById(R.id.searchText);
            String search = searchedRecipe.getText().toString();
            String searchLowerCase = search.toLowerCase();

            //modification starts here:
            String searchQuery = "https://api.edamam.com/search?app_id=bd8ab790&app_key=466eca9597ecd8b4fbedecfaca1c6250&q=" + search; //modified
            System.err.println("Search query" + searchQuery);

            String searchURL = searchQuery.trim();
            System.err.println("Searched string(in on Create):" + search);
            new searchTask().execute(searchURL);
        });

        myList.setOnItemClickListener((list, view, position, id) -> {
            Recipe recipe = recipes.get(position);
            id = recipes.indexOf(recipe);
            String title = recipe.getTitle();
            String url = recipe.getUrl();
            Intent goToFragment = new Intent(this, RecipeDetailsActivity.class);
            goToFragment.putExtra("title", title);
            goToFragment.putExtra("url", url);
            startActivity(goToFragment);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        String recentRecipe = sharedPreferences.getString(TEXT, "");
        searchedRecipe.setText(recentRecipe);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, searchedRecipe.getText().toString());
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    class searchTask extends AsyncTask<String, Integer, String> {
        static private final String TAG = "MyHTTPRequest";

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
                String search = searchedRecipe.getText().toString();
                String searchLowerCase = search.toLowerCase();
                resultText = findViewById(R.id.queryResult);


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

                    String recipeTitleLowerCase = recipeTitle.toLowerCase();

                    String[] ingredientLines = new String[ingredientsArray.length()];

                    if(recipeTitleLowerCase.contains(searchLowerCase)){

                        Log.i(TAG, "What was searched: " + whatWasTyped) ;
                        Log.i(TAG, "Recipe found: " + recipeTitle) ;
                        for(int j = 0; j < ingredientsArray.length(); j++){
                            ingredientLines[j] = ingredientsArray.getString(j);
                            Log.i(TAG, "Recipe ingredients line " + (j+1) + ": "+ ingredientLines[j]);

                        }
                        recipes.add(new Recipe(recipeTitle, recipeURL));
                        myAdapter.notifyDataSetChanged();

                    } else {
                        resultText.setText("Not found");
                    }
                }
                publishProgress(25);
                Thread.sleep(1000);
                publishProgress(50);

            } catch (Exception e) {

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

    private class RecipeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return recipes.size();
        }

        @Override
        public Object getItem(int position) {
            Recipe thisRow = recipes.get(position);
            return thisRow.title;
            //return thisRow;
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View recipeView = inflater.inflate(R.layout.recipe_list_layout, parent, false);
            TextView recipeTitle = recipeView.findViewById(R.id.recipe_title);
            recipeTitle.setText(getItem(position).toString());

            return recipeView;
        }
    }

    public class Recipe {
        String title;
        String url;
        //long id;

        public Recipe(String title, String url/*, long id*/) {
            this.title = title;
            this.url = url;
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
        /*public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }*/
    }

}