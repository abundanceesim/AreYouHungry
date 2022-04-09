package com.cst2335.areyouhungry;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FavouritesActivity extends AppCompatActivity {
    /*FavouritesAdapter myAdapter;
    ArrayList<Favourites> recipes = new ArrayList<>();  //array list that would contain messages.*/
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Toolbar tBar = findViewById(R.id.toolBar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("Favourites");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}