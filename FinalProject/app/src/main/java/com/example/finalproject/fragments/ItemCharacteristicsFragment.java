package com.example.finalproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.items.Item;
import com.example.finalproject.R;

import org.jetbrains.annotations.NotNull;

public class ItemCharacteristicsFragment extends Fragment {
    private Item item;
    private boolean mode;

    public ItemCharacteristicsFragment(Item item){
        this.item=item;
        mode=true;
    }

    public ItemCharacteristicsFragment(Item item, boolean mode){
        this.item=item;
        this.mode=mode;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_characteristics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView img=getView().findViewById(R.id.category_image);
        img.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.textures[6][item.getCategory()],
                MainActivity.categoryImageWidth, MainActivity.categoryImageWidth, false));
        TextView name = (TextView) getView().findViewById(R.id.name_field);
        TextView category = (TextView) getView().findViewById(R.id.category_field);
        TextView cost = (TextView) getView().findViewById(R.id.cost_field);
        name.setText(item.getName()+"");
        category.setText(MainActivity.categories.get(item.getCategory())+"");
        if (mode)
            cost.setText(item.getCostSell()+"");
        else cost.setText(item.getCostBuy()+"");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}