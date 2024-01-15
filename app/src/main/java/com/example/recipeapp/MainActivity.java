package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeapp.Adapters.RandomRecipeAdapter;
import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Models.RandomRecipeApiResponse;


// Add internet permission to AndroidManifest.xml - <uses-permission android:name="android.permission.INTERNET"/>
public class MainActivity extends AppCompatActivity {
    //    Objects
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
//    Create a new Object of Spinner
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Initialize dialog, manager - pass context this
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

//        Initialize spinner inside onCreate method
        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.tags, R.layout.spinner_text);

        manager = new RequestManager(this);
        manager.getRandomRecipes(randomRecipeResponseListener);  // Pass the listener to getRandomRecipes parameter
//        Set the drop down resources layout for our spinner
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
//        Attach the arrayAdapter to the spinner
        spinner.setAdapter(arrayAdapter);

//        Show dialog
        dialog.show();
    }

//    Create listener
    private final RandomRecipeResponseListener randomRecipeResponseListener=new RandomRecipeResponseListener() {
//        When we get our response on didFetch we’ll show that on the recyclerview and if we get an error we’ll show a toast message with the error message
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
//            Initialize recyclerview
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));  // span count is 1
//            Create adapter - pass the context and list of recipes
            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes);
//            Add these adapter to recyclerview
            recyclerView.setAdapter(randomRecipeAdapter);

            dialog.dismiss();  // The progress dialog was showing still - to disable the dialog once we get the response on the didFetch we can dismiss the dialog
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}