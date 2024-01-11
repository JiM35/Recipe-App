package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
//    The first parameter will be RandomRecipeApiResponse
//    The second parameter is the message we got from the api
    void didFetch(RandomRecipeApiResponse response, String message);

//    Add another method for error handling - we will only pass the message we got from the api
    void didError(String message);
}
