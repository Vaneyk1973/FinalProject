package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Menu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView[] inv=new ImageView[4];
        inv[0]=getView().findViewById(R.id.inventory_button);
        inv[1]=getView().findViewById(R.id.spellCreation_button);
        inv[2]=getView().findViewById(R.id.spells_button);
        inv[3]=getView().findViewById(R.id.researchTree_button);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Bitmap bm=Bitmap.createBitmap(width/4, width/4, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.RED);
        inv[0].setImageBitmap(bm);
        inv[1].setImageBitmap(bm);
        inv[2].setImageBitmap(bm);
        inv[3].setImageBitmap(bm);
        FragmentManager fm=getParentFragmentManager();
        FragmentTransaction fragmentTransaction= fm.beginTransaction();
        inv[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.add(R.id.inventory, new InventoryFragment());
                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}