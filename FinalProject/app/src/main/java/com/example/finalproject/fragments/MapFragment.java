package com.example.finalproject.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.entities.Enemy;
import com.google.firebase.auth.FirebaseAuth;

import java.net.InetAddress;
import java.util.Random;

public class MapFragment extends Fragment {
    private final int mapNum;

    public MapFragment(int mapNum){
        this.mapNum = mapNum;
    }

    MapFragment(){
        mapNum =0;
        MainActivity.player.setMapNum(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Pair<Integer, Integer> player_coords=MainActivity.player.getCoordinates(mapNum);
        TableLayout table=getView().findViewById(R.id.tableLayout);
        TableRow[] rows=new TableRow[table.getChildCount()];
        for (int i=0;i<table.getChildCount();i++)
            rows[i]=(TableRow)table.getChildAt(i);
        ImageView[][] visible_map=new ImageView[5][5];
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {Pair<Integer, Integer> coords= find_title_coordinates((ImageView)v, visible_map),
                    player_coords=MainActivity.player.getCoordinates(mapNum);
                if (!MainActivity.player.getCoordinates(mapNum).equals(coords)
                        &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=0
                        &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=100)
                {
                    int dx=coords.first-player_coords.first,
                            dy=coords.second-player_coords.second;
                    if (Math.abs(dx)<=1&&Math.abs(dy)<=1){
                        MainActivity.player.regenerate();
                        int a=new Random().nextInt(100);
                        if (MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=3&&
                                mapNum!=1
                                &&a<MainActivity.chancesOfFight.get(
                                MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType())) {
                            FragmentManager fm=getParentFragmentManager();
                            FragmentTransaction fragmentTransaction= fm.beginTransaction();
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                            fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                            fragmentTransaction.add(R.id.fight, new FightFragment());
                            fragmentTransaction.commit();
                            a=new Random().nextInt(100);
                            switch (MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()){
                                case 1:{
                                    if (a<30)
                                        MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(1).get(30)));
                                    else MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(1).get(70)));
                                    break;
                                }
                                case 2:{
                                    if (a<60)
                                        MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(2).get(60)));
                                    else if (a<=95&&a>=60)
                                        MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(2).get(35)));
                                    else MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(2).get(5)));
                                    break;
                                }
                                case 4:{
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(4).get(100)));
                                    break;
                                }
                                case 5:{
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(5).get(100)));
                                    break;
                                }
                                case 6:{
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(6).get(100)));
                                    break;
                                }
                                case 7:{
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(7).get(100)));
                                    break;
                                }
                                case 8:{
                                    MainActivity.player.setEnemy(new Enemy(MainActivity.chancesOfEnemy.get(8).get(100)));
                                    break;
                                }
                            }
                        }
                        switch (MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()){
                            case 3:{
                                MainActivity.player.setMapNum(1);
                                MainActivity.player.setCoordinates(1, new Pair<>(6, 3));
                                FragmentManager fm = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                fragmentTransaction.add(R.id.map, new MapFragment(1));
                                fragmentTransaction.commit();
                                break;
                            }
                            case 9:{
                                FragmentManager fm = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                                fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                                fragmentTransaction.add(R.id.tasks, new TaskManagerFragment(true));
                                fragmentTransaction.commit();
                                break;
                            }
                            case 10:{
                                if (isInternetAvailable()){
                                    if (MainActivity.player.getUser().getLogin().isEmpty())
                                        Toast.makeText(getContext(), "Sign in first", Toast.LENGTH_SHORT).show();
                                    else {
                                        FragmentManager fm = getParentFragmentManager();
                                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                                        fragmentTransaction.add(R.id.fight, new FightFragment(true));
                                        fragmentTransaction.commit();
                                    }
                                }
                                else Toast.makeText(getContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 11:{
                                FragmentManager fm = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                                fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                                fragmentTransaction.add(R.id.crafting_station, new CraftingStationFragment());
                                fragmentTransaction.commit();
                                break;
                            }
                            case 12:{
                                FragmentManager fm = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                                fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                                fragmentTransaction.add(R.id.shop, new ShopFragment());
                                fragmentTransaction.commit();
                                break;
                            }
                            case 13:{
                                FragmentManager fm = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                fragmentTransaction.add(R.id.map, new MapFragment());
                                fragmentTransaction.commit();
                                break;
                            }
                            case 14:{
                                if (isInternetAvailable()){
                                    if (MainActivity.player.getUser().getLogin().isEmpty()){
                                        Toast.makeText(getContext(), "Sign in first", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        FragmentManager fm = getParentFragmentManager();
                                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                                        fragmentTransaction.add(R.id.shop, new ShopFragment(true));
                                        fragmentTransaction.commit();
                                    }
                                }
                                else Toast.makeText(getContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if (MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=8
                                &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=9
                                &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=10
                                &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=11
                                &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=12
                                &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=13
                                &&MainActivity.map.get(mapNum).getMap()[coords.first][coords.second].getType()!=14)
                        {
                            MainActivity.player.setTitleTexture(
                                    MainActivity.mapTextures.get(
                                            MainActivity.map.get(mapNum)
                                                    .getMap()[player_coords.first][player_coords.second].getType()));
                            MainActivity.map.get(mapNum).getMap()[player_coords.first][player_coords.second].setTexture(
                                    Bitmap.createBitmap(MainActivity.player.getTitleTexture()));
                            MainActivity.player.setCoordinates(mapNum,
                                    new Pair<>(player_coords.first+dx, player_coords.second+dy));
                            player_coords=MainActivity.player.getCoordinates(mapNum);
                            MainActivity.player.setTitleTexture(
                                    Bitmap.createBitmap(
                                            MainActivity.map.get(mapNum)
                                                    .getMap()[player_coords.first][player_coords.second].getTexture()));
                            MainActivity.map.get(mapNum).getMap()[player_coords.first][player_coords.second]
                                    .getTexture().eraseColor(Color.BLUE);
                            if (player_coords.first+dx>=2&&player_coords.second+dy>=2){
                                for (int i=0;i<5;i++) {
                                    for (int j = 0; j < 5; j++) {
                                        visible_map[i][j].setImageBitmap(
                                                MainActivity.map.get(mapNum).getMap()[player_coords.first-2+i]
                                                        [player_coords.second-2+j].getTexture());
                                    }
                                }
                            }
                            else if (player_coords.first+dx>=2) {
                                for (int i=0;i<5;i++) {
                                    for (int j = 0; j < 5; j++) {
                                        visible_map[i][j].setImageBitmap(
                                                MainActivity.map.get(mapNum).getMap()[player_coords.first-2+i][j].getTexture());
                                    }
                                }
                            }
                            else if (player_coords.second+dy>=2){
                                for (int i=0;i<5;i++) {
                                    for (int j = 0; j < 5; j++) {
                                        visible_map[i][j].setImageBitmap(
                                                MainActivity.map.get(mapNum).getMap()[i][player_coords.second-2+j].getTexture());
                                    }
                                }
                            }
                            else {
                                for (int i=0;i<5;i++) {
                                    for (int j = 0; j < 5; j++) {
                                        visible_map[i][j].setImageBitmap(MainActivity.map.get(mapNum).getMap()[i][j].getTexture());
                                    }
                                }
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
                visible_map[i][j].setImageBitmap(MainActivity.map.get(mapNum)
                        .getMap()[player_coords.first-2+i]
                        [player_coords.second-2+j].getTexture());
                visible_map[i][j].setOnClickListener(onClickListener);
            }
        }
        MainActivity.player.setTitleTexture(
                Bitmap.createBitmap(MainActivity.map.get(mapNum).
                        getMap()[MainActivity.player.getCoordinates(mapNum).first]
                        [MainActivity.player.getCoordinates(mapNum).second].getTexture()));
        MainActivity.map.get(mapNum).getMap()[MainActivity.player.getCoordinates(mapNum).first]
                [MainActivity.player.getCoordinates(mapNum).second]
                .getTexture().eraseColor(Color.BLUE);
    }

    private Pair<Integer, Integer> find_title_coordinates(ImageView v, ImageView[][] p){
        for (int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if (v==p[i][j])
                    return new Pair<>(i+MainActivity.player.getCoordinates(mapNum).first-2,
                            j+MainActivity.player.getCoordinates(mapNum).second-2);
            }
        }
        return null;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}