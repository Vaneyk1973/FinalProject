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

import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Fight extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fight, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Button run=(Button)getView().findViewById(R.id.run);
        ImageView player=(ImageView)getView().findViewById(R.id.player);
        ImageView enemy=(ImageView)getView().findViewById(R.id.enemy);
        Bitmap bm=Bitmap.createBitmap(width/4, width/4, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.YELLOW);
        player.setImageBitmap(bm);
        enemy.setImageBitmap(bm);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=new Random().nextInt(100);
                if (a>=50){
                    FragmentManager fm=getParentFragmentManager();
                    FragmentTransaction fragmentTransaction= fm.beginTransaction();
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                    fragmentTransaction.add(R.id.map, new Map());
                    fragmentTransaction.add(R.id.status, new StatusBar());
                    fragmentTransaction.add(R.id.menu, new Menu());
                    fragmentTransaction.commit();
                }
            }
        });
    }
}

