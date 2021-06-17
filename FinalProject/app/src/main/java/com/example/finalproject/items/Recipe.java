package com.example.finalproject.items;

import android.util.Pair;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.items.Item;

import java.util.ArrayList;

public class Recipe {
    private Item product;
    private ArrayList<Pair<Item, Integer>> ingredients=new ArrayList<>();

    public Recipe(Item product, ArrayList<Pair<Item, Integer>> ingredients) {
        this.product = product;
        this.ingredients.addAll(ingredients);
    }

    public Item getProduct() {
        return product;
    }

    public void setProduct(Item product) {
        this.product = product;
    }

    public ArrayList<Pair<Item, Integer>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Pair<Item, Integer>> ingredients) {
        this.ingredients = ingredients;
    }
}
