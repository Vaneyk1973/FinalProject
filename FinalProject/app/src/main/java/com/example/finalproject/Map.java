package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

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
        TableLayout table=getView().findViewById(R.id.tableLayout);
        TableRow[] rows=new TableRow[table.getChildCount()];
        Bitmap bm=Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.BLUE);
        ((ImageView)getView().findViewById(MainActivity.player.getCoordinates())).setImageBitmap(bm);
        bm.eraseColor(Color.BLACK);
        ImageView[][] im=new ImageView[5][5];
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()!=MainActivity.player.getCoordinates())
                {
                    int a=new Random().nextInt(100);
                    if (a<=50){
                        Toast.makeText(getContext(), Integer.toString(a), Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getContext(), Integer.toString(a), Toast.LENGTH_SHORT).show();
                }
                ((ImageView)getView().findViewById(MainActivity.player.getCoordinates())).setImageBitmap(bm);
                MainActivity.player.setCoordinates(v.getId());
                Bitmap b=Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888);
                b.eraseColor(Color.BLUE);
                ((ImageView)v).setImageBitmap(b);
            }
        };
        for (int i=0;i<table.getChildCount();i++){
            rows[i]=(TableRow)table.getChildAt(i);
            for (int j=0;j<rows[i].getChildCount();j++){
                im[i][j]=(ImageView)rows[i].getChildAt(j);
                im[i][j].setImageBitmap(bm);
                im[i][j].setOnClickListener(onClickListener);
                if (im[i][j].getId()==MainActivity.player.getCoordinates())
                    im[i][j].callOnClick();
            }
        }
    }
}