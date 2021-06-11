package com.example.finalproject.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.finalproject.R;
import com.example.finalproject.entities.Enemy;

import java.util.Random;

public class MapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Pair<Integer, Integer> player_coords=MainActivity.player.getCoordinates();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        TableLayout table=getView().findViewById(R.id.tableLayout);
        TableRow[] rows=new TableRow[table.getChildCount()];
        for (int i=0;i<table.getChildCount();i++)
            rows[i]=(TableRow)table.getChildAt(i);
        ImageView[][] visible_map=new ImageView[5][5];
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<Integer, Integer> coords= find_title_coords((ImageView)v, visible_map),
                        player_coords=MainActivity.player.getCoordinates();
                if (!MainActivity.player.getCoordinates().equals(coords)
                        &&MainActivity.map.get(0).getMap()[coords.first][coords.second].getType()!=0)
                {
                    int dx=coords.first-player_coords.first,
                            dy=coords.second-player_coords.second;
                    if (Math.abs(dx)<=1&&Math.abs(dy)<=1){
                        MainActivity.player.regenerate();
                        int a=new Random().nextInt(100);
                        if (a<MainActivity.chances_of_fight.get(
                                MainActivity.map.get(0).getMap()[coords.first][coords.second].getType())) {
                            FragmentManager fm=getParentFragmentManager();
                            FragmentTransaction fragmentTransaction= fm.beginTransaction();
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                            fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                            fragmentTransaction.add(R.id.fight, new FightFragment());
                            fragmentTransaction.commit();
                        }
                        MainActivity.player.setTitle_texture(
                                MainActivity.map_textures.get(
                                        MainActivity.map.get(0).getMap()[player_coords.first][player_coords.second].getType()));
                        MainActivity.map.get(0).getMap()[player_coords.first][player_coords.second].setTexture(
                                Bitmap.createBitmap(MainActivity.player.getTitle_texture()));
                        MainActivity.player.setCoordinates(
                                new Pair<>(player_coords.first+dx, player_coords.second+dy));
                        player_coords=MainActivity.player.getCoordinates();
                        MainActivity.player.setTitle_texture(
                                Bitmap.createBitmap(
                                        MainActivity.map.get(0).getMap()[player_coords.first][player_coords.second].getTexture()));
                        MainActivity.map.get(0).getMap()[player_coords.first][player_coords.second].getTexture().eraseColor(Color.BLUE);
                        if (player_coords.first+dx>=2&&player_coords.second+dy>=2){
                            for (int i=0;i<5;i++) {
                                for (int j = 0; j < 5; j++) {
                                    visible_map[i][j].setImageBitmap(MainActivity.map.get(0).getMap()[player_coords.first-2+i][player_coords.second-2+j].getTexture());
                                }
                            }
                        }
                        else if (player_coords.first+dx>=2) {
                            for (int i=0;i<5;i++) {
                                for (int j = 0; j < 5; j++) {
                                    visible_map[i][j].setImageBitmap(MainActivity.map.get(0).getMap()[player_coords.first-2+i][j].getTexture());
                                }
                            }
                        }
                        else if (player_coords.second+dy>=2){
                            for (int i=0;i<5;i++) {
                                for (int j = 0; j < 5; j++) {
                                    visible_map[i][j].setImageBitmap(MainActivity.map.get(0).getMap()[i][player_coords.second-2+j].getTexture());
                                }
                            }
                        }
                        else {
                            for (int i=0;i<5;i++) {
                                for (int j = 0; j < 5; j++) {
                                    visible_map[i][j].setImageBitmap(MainActivity.map.get(0).getMap()[i][j].getTexture());
                                }
                            }
                        }
                        a=new Random().nextInt(100);
                        switch (MainActivity.map.get(0).getMap()[coords.first][coords.second].getType()){
                            case 1:{
                                if (a<30)
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(1).get(30)));
                                else MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(1).get(70)));
                                break;
                            }
                            case 2:{
                                if (a<60)
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(2).get(60)));
                                else if (a<=95&&a>=60)
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(2).get(35)));
                                else MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(2).get(5)));
                                break;
                            }
                            case 4:{
                                MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(4).get(100)));
                                break;
                            }
                            case 5:{
                                MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(5).get(100)));
                                break;
                            }
                            case 6:{
                                MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(6).get(100)));
                                break;
                            }
                            case 7:{
                                MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(7).get(100)));
                                break;
                            }
                            case 8:{
                                MainActivity.player.setEnemy(new Enemy(MainActivity.chances_of_enemy.get(8).get(100)));
                                break;
                            }
                        }
                    }
                }
                StatusBarFragment.update();
            }
        };
        for (int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                visible_map[i][j] = (ImageView) rows[i].getChildAt(j);
                visible_map[i][j].setImageBitmap(MainActivity.map.get(0).getMap()[player_coords.first-2+i][player_coords.second-2+j].getTexture());
                visible_map[i][j].setOnClickListener(onClickListener);
            }
        }
    }

    private Pair<Integer, Integer> find_title_coords(ImageView v, ImageView[][] p){
        for (int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if (v==p[i][j])
                    return new Pair<>(i+MainActivity.player.getCoordinates().first-2, j+MainActivity.player.getCoordinates().second-2);
            }
        }
        return null;
    }

}