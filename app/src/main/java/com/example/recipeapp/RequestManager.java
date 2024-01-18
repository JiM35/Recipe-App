package com.example.recipeapp;

import android.content.Context;

import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Models.RandomRecipeApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class RequestManager {

//    Create Retrofit object and object for Context
//    Paste URL here
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com/").addConverterFactory(GsonConverterFactory.create()).build();
    Context context;


//    Create constructor for the RequestManager class
    public RequestManager(Context context) {
        this.context = context;
    }

//    For the parameters at first we will pass an object of the RandomRecipeResponseListener
//    We can call the getRandomRecipes method from MainActivity to get all the data
    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags) {
//        We will create an instance of the CallRandomRecipes and name the object as callRandomRecipes
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
//        Create a call object
//        For the parameters we need to pass two objects the api key and the number
//        We have stored api key in th string folder
//        The second parameter is the number - the number of total number of random recipes that we want to face from the api. You can pass anything between 1 and 100
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10", tags);
//        We need to enqueue our call and create a new Callback for RandomRecipeApiResponse. We get the onResponse and onFailure method
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
//                Check if response is successful or not
//                If the response is not successful, we will call our listener.didError. We will only pass the response.message()
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
//                If the response is successful, we will call listener.didFetch
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
//                If the response is failure, we call listener.didError. For the message we will pass the message from Throwable
                listener.didError(t.getMessage());
            }
        });
    }

    /* We have to create another class specifically for random recipe api response - RandomRecipeApiResponse
    * We have to create a model class for the api response
    * We have to create another class to access this api response from our MainActivity
    * We will create a listener to pass this to our MainActivity */

//    Create a separate interface for the random recipe api call
    private interface CallRandomRecipes {
    //        This call method is a get method - we have to pass an annotation called GET
//        At the parameter of GET we have to pass the end point of URL
//        At the base URL we had written till com/, here at the end point we have to add the rest of this
    @GET("recipes/random")
//        Create a new call method
    Call<RandomRecipeApiResponse> callRandomRecipe(
//                From the API we can pass tags, it is a String type. The tags can be diets, meal types, cuisines or intolerances
//                We can pass multiple items wih these tags. We'll mainly create this tag as a list but in this example we'll only pass one tag at a time but you can pass as many of them as you want
            @Query("apiKey") String apiKey,
            @Query("number") String number,
            @Query("tags") List<String> tag
    );


    }
}
