package com.example.recipeapp.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

// As we are using a recycler view we need to create adapter class for recycler view - RandomRecipeAdapter
// Make RandomRecipeAdapter class extends the default Adapter class of RecyclerView. Pass the ViewHolder class - RandomRecipeViewHolder
// Implement methods - select all and click OK. We get three methods - the onCreateViewHolder, onBindViewHolder and getItemCount
public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {
//    Create some objects inside RandomRecipeAdapter
    Context context;
    List<Recipe> list;

//    Create constructor
    public RandomRecipeAdapter(Context context, List<Recipe> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override

    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textview_title.setText(list.get(position).title);
//        Give the textview_title a horizontal scrolling effect
        holder.textview_title.setSelected(true);
        holder.textView_likes.setText(list.get(position).aggregateLikes + " likes");
        holder.textView_servings.setText(list.get(position).servings + " servings");
        holder.textView_time.setText(list.get(position).readyInMinutes + " minutes");
//        Populate the imageview with image of the particular meal
        Picasso.get().load(list.get(position).image).into(holder.imageView_food);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

// Create a ViewHolder class and make it extends RecyclerView.ViewHolder
// Create constructor matching super
class RandomRecipeViewHolder extends RecyclerView.ViewHolder {
//    Create a new CardView object
    CardView random_list_container;
    TextView textview_title, textView_servings, textView_likes, textView_time;
    ImageView imageView_food;

    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
//        Initialize our views
        random_list_container = itemView.findViewById(R.id.random_list_container);
        textview_title = itemView.findViewById(R.id.textview_title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_likes = itemView.findViewById(R.id.textView_likes);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_food = itemView.findViewById(R.id.imageView_food);

    }
}
