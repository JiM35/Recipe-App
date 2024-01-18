package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeapp.Adapters.RandomRecipeAdapter;
import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Models.RandomRecipeApiResponse;

import java.util.ArrayList;
import java.util.List;


// Add internet permission to AndroidManifest.xml - <uses-permission android:name="android.permission.INTERNET"/>
public class MainActivity extends AppCompatActivity {
    //    Objects
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
//    Create a new Object of Spinner
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    //    Initialize SearchView - create a new SearchView object. Use androidx.appcompat.widget
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Initialize dialog, manager - pass context this
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

//        Initialize searchView
        searchView = findViewById(R.id.searchView_home);
//        Create setOnQueryTextListener for searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

//        Initialize spinner inside onCreate method
        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.tags, R.layout.spinner_text);

        manager = new RequestManager(this);

//        This will show an error because we are calling the API once the user selects any item for the spinner so we don't need to call these at the onCreate method
//        manager.getRandomRecipes(randomRecipeResponseListener); and dialog.show();
//        manager.getRandomRecipes(randomRecipeResponseListener);  // Pass the listener to getRandomRecipes parameter

//        Set the drop down resources layout for our spinner
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
//        Attach the arrayAdapter to the spinner
        spinner.setAdapter(arrayAdapter);

//        Attach this spinnerSelectedListeners to our spinner item
        spinner.setOnItemSelectedListener(spinnerSelectedListeners);

//        Show dialog
//        dialog.show();
    }

    //    Create listener
    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
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

//        Create a separate listener for the spinners OnItemSelectedListener
    private final AdapterView.OnItemSelectedListener spinnerSelectedListeners = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            We'll add items to these tags whenever user selects any items from the spinner
//            Clear the list
            tags.clear();
//            We'll add the item that user selected
            tags.add(adapterView.getSelectedItem().toString());
//            Call getRandomRecipes. Pass listener
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
//            Enable our progress dialog
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}