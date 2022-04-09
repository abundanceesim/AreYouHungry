package com.cst2335.areyouhungry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {
    Toolbar tBar;
    TextView textView;
    Button backButton;
    String userName;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tBar = (Toolbar)view.findViewById(R.id.toolBar);
        textView = (TextView)view.findViewById(R.id.fragmentText);
        backButton = (Button) view.findViewById(R.id.backButton);

        tBar.setTitle("About v1.0.0 by Abundance");

        if(savedInstanceState == null) {
            // Get back arguments that were passed into the Bundle
            if (getArguments() != null) {
                userName = getArguments().getString("username", "");
                textView.setText("Welcome " + userName.toString() + "!" + "\nThank you so much for using our app! We really hope you enjoy the experience. Please do well to leave a rating for our app" +
                        " on Play Store. Once again, thank you!\n" +
                        "\n \n From the team at RecipeWorks.co");
            }
        }
        // Inflate the layout for this fragment
        return view;
    }

    /**Add a login button that redirects the user to an activity(HomeFragment) which is an About page.
     * "Thank you so much for using our app, usersName! We really hope you enjoy the experience. Please do well to leave a rating for our app
     * on Play Store. Once again, thank you!
     * From the team at RecipeWorks.co" */
}