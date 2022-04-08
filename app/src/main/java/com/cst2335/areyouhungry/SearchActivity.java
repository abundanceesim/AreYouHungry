package com.cst2335.areyouhungry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        myList = findViewById(R.id.listView);
        myList.setAdapter( myAdapter = new RecipeAdapter());

        searchedRecipe = findViewById(R.id.searchText);
        String search = searchedRecipe.getText().toString();

        searchButton = findViewById(R.id.searchButton);
        resultText = findViewById(R.id.queryResult);

        String searchQuery = "https://api.spoonacular.com/recipes/complexSearch?apiKey=17696a86da5e4c2a822ba6d7f503cf31&number=10";
        //String searchQuery = "https://api.spoonacular.com/recipes/complexSearch?apiKey=9b125163604948b2a0a0878254ab65ff&number=6";
        searchButton.setOnClickListener(click -> {
            new searchTask().execute(searchQuery);
        });

        myList.setOnItemClickListener(((adapterView, view, i, l) -> {

        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Shared_prefs, MODE_PRIVATE);
        String recentRecepie = sharedPreferences.getString(TEXT, "");
        searchedRecipe.setText(recentRecepie);
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

    class searchTask extends AsyncTask<String, Integer, String>{
        //String URL = "https://api.spoonacular.com/recipes/complexSearch";
        static private final String TAG = "MyHTTPRequest";

        //Type3                Type1
        public String doInBackground(String ... args)
        {
            try {

                URL url = new URL(args[0]);//create a URL object of what server to contact:
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();//open the connection
                InputStream response = urlConnection.getInputStream();//wait for data:

                //JSON reading. Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                myList = findViewById(R.id.listView);
                myList.setAdapter( myAdapter = new RecipeAdapter());


                JSONObject uvReport = new JSONObject(result); // convert string to JSON: Look at slide 27:

                JSONArray resultsArray = uvReport.getJSONArray("results");  //JSONArray resultsArray = new JSONArray(result);

                searchedRecipe = findViewById(R.id.searchText);
                String search = searchedRecipe.getText().toString();
                String searchLowerCase = search.toLowerCase();
                resultText = findViewById(R.id.queryResult);

                for(int i = 0; i < resultsArray.length(); i++){
                    JSONObject resultsObject = resultsArray.getJSONObject(i);
                    int id = resultsObject.getInt("id");
                    String recipeTitle = resultsObject.getString("title");
                    String recipeTitleLowerCase = recipeTitle.toLowerCase();

                    //Recipe recipe = new Recipe(recipeTitle);
                    if(recipeTitleLowerCase.contains(searchLowerCase)){
                        //resultText.setText(/*"Recipe " + */recipeTitle /*+ " with ID: " + id + " found"*/);
                        //Recipe recipe = new Recipe(recipeTitle);
                        Log.i(TAG, "Recipe found: " + recipeTitle) ;
                        recipes.add(new Recipe(recipeTitle, (long)id));
                        myAdapter.notifyDataSetChanged();

                    }
                    else {
                        resultText.setText("Not found");
                    }
                }
                publishProgress(25);
                Thread.sleep(1000);
                publishProgress(50);
                //Log.i(TAG, "Recipe found: " + recipeTitle) ;

            }
            catch (Exception e)
            {

            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            Log.i(TAG, "onProgressUpdate");
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {

            Log.i(TAG, "String gotten is: " + fromDoInBackground);
        }

    }

    private class RecipeAdapter extends BaseAdapter{

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
            return (long)position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View recipeView = inflater.inflate(R.layout.recipe_list_layout, parent, false);
            TextView recipeTitle = recipeView.findViewById(R.id.recipe_title);
            recipeTitle.setText( getItem(position).toString() );

            return recipeView;
        }
    }

    public class Recipe{
        String title;
        long id;

        public Recipe(String title, long id) {
            this.title = title;
            this.id  = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}