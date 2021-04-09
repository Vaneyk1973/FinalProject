package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Map extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Pair<ImageView, Pair<Integer, Integer>>[][] map=new Pair[5][5];
        TableLayout table=getView().findViewById(R.id.tableLayout);
        TableRow[] rows=new TableRow[table.getChildCount()];
        Bitmap[] bm=new Bitmap[10];
        for (int i=0;i<10;i++)
            bm[i]=Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888);
        bm[0].eraseColor(Color.BLACK);
        bm[1].eraseColor(Color.BLUE);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<Integer, Integer> coord=find_image((ImageView)v, map);
                if (!MainActivity.player.getCoordinates().equals(coord))
                {
                    int a=new Random().nextInt(100);
                    int dx=coord.first-MainActivity.player.getCoordinates().first,
                            dy=coord.second-MainActivity.player.getCoordinates().second;
                    MainActivity.player.setCoordinates(new Pair<>(MainActivity.player.getCoordinates().first+dx, MainActivity.player.getCoordinates().second+dy));
                    for (int i=0;i<5;i++){
                        for (int j=0;j<5;j++){
                        }
                    }
                    if (a<=50){
                        Toast.makeText(getContext(), coord.first+" "+coord.second, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), coord.first+" "+coord.second, Toast.LENGTH_SHORT).show();
                        /*FragmentManager fm=getParentFragmentManager();
                        FragmentTransaction fragmentTransaction= fm.beginTransaction();
                        fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                        fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                        fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                        fragmentTransaction.add(R.id.fight, new Fight());
                        fragmentTransaction.commit();*/
                    }
                }
            }
        };
        for (int i=0;i<5;i++){
            rows[i]=(TableRow)table.getChildAt(i);
            for (int j=0;j<5;j++){
                map[i][j]=new Pair<>((ImageView)rows[i].getChildAt(j), new Pair<>(i, j));
                map[i][j].first.setImageBitmap(Bitmap.createBitmap(bm[0]));
                map[i][j].first.setOnClickListener(onClickListener);
                if (i==2&&j==2)
                {
                    map[i][j].first.setImageBitmap(bm[1]);
                }
            }
        }
    }

    private Pair<Integer, Integer> find_image(ImageView v, Pair[][] p){
        for (int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if (v==p[i][j].first)
                    return (Pair<Integer, Integer>)p[i][j].second;
            }
        }
        return null;
    }
}