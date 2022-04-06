package com.cst2335.areyouhungry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {
    Toolbar tBar;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Home fragment creation
        /*tBar = findViewById(R.id.toolBar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("About"); */

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame1, homeFragment);    //replace the frame with the fragment
        ft.commit();

        Intent fromHomeActivity = getIntent();
        userName = fromHomeActivity.getStringExtra("username");

        //Send data to the Fragment using a Bundle object.
        Bundle args = new Bundle();
        args.putString("username", userName);
        homeFragment.setArguments(args);
    }

}