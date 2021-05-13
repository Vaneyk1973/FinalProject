package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static Player player;
    public static HashMap<Integer, Integer> chances_of_fight =new HashMap<>();
    public static MapTitle[][] map=new MapTitle[32][32];
    public static Bitmap[] menu=new Bitmap[4];
    public static HashMap<Integer, HashMap<Enemy, Integer>> chances_of_enemy=new HashMap<>();
    public static ArrayList<Enemy> enemies=new ArrayList<>();
    public static HashMap<Integer, ArrayList<Pair<Item, Integer>>> drop=new HashMap<>();
    public static ArrayList<Element> elements=new ArrayList<>();
    public static ArrayList<ManaChannel> mana_channels =new ArrayList<>();
    public static ArrayList<Type> types =new ArrayList<>();
    public static ArrayList<Form> forms =new ArrayList<>();
    public static ArrayList<ManaReservoir> mana_reservoirs =new ArrayList<>();
    public static ArrayList<Research> researches=new ArrayList();
    private static boolean created=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, new Map());
        fragmentTransaction.add(R.id.status, new StatusBar());
        fragmentTransaction.add(R.id.menu, new Menu());
        fragmentTransaction.commit();
        if (savedInstanceState!=null)
        {
            player=new Player(savedInstanceState.getParcelable("player"));
            Log.d("HHH", player.toString());
        }
        else player=new Player(2, 4);
        setInitialData();
    }



    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("player", player);
        Log.d("LLL", player.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player=new Player(savedInstanceState.getParcelable("player"));
        Log.d("LLLL", player.toString());
    }

    private void setInitialData(){
        if (!created){

        }
        created=true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Bitmap a=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.xxx), width/5*9, width/5, false);
        Bitmap[][] b=new Bitmap[9][1];
        for (int i=0;i<9;i++){
            b[i][0]=Bitmap.createBitmap(a, i*width/5, 0, width/5, width/5);
        }
        for (int i=0;i<map.length;i++){
            for (int j=0;j<map[0].length;j++){
                map[i][j]=new MapTitle(new Pair<>(i, j), Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888), 0);
                if ((i<=1||j<=1)||(i>=map.length-2||j>=map.length-2)){
                    map[i][j].setType(1);
                }
                else{
                    map[i][j].getTexture().eraseColor(Color.BLACK);
                }
            }
        }
        for (int i=4;i<7;i++){
            for (int j=4;j<7;j++){
                map[i][j].setType(5);
            }
        }
        map[4][4].setType(3);
        for (int i=0;i<map.length;i++){
            for (int j=0;j<map[0].length;j++){
                switch (map[i][j].getType()){
                    case 0: map[i][j].setTexture(b[4][0]); break;
                    case 1: map[i][j].setTexture(b[1][0]); break;
                    case 3: map[i][j].setTexture(b[3][0]); break;
                    case 5: map[i][j].setTexture(b[5][0]); break;
                }
            }
        }
        researches.add(new Research(null, "Basic spell creation", 1, 0, 0, false, true));
        ArrayList<Research> rqr=new ArrayList<>();
        rqr.add(researches.get(0));
        researches.add(new Research((ArrayList<Research>)rqr.clone(), "Fire mage", 3, 1, 1, false, false));
        researches.add(new Research((ArrayList<Research>)rqr.clone(), "Water mage", 3, 1, 2, false, false));
        researches.add(new Research((ArrayList<Research>)rqr.clone(), "Earth mage", 3, 1, 3, false, false));
        researches.add(new Research((ArrayList<Research>)rqr.clone(), "Air mage", 3, 1, 4, false, false));
        drop.put(0, new ArrayList<>());
        drop.get(0).add(new Pair<>(new Item("yyy"), 70));
        drop.get(0).add(new Pair<>(new Item("rrr"), 30));
        player.setTitle_texture(Bitmap.createBitmap(map[player.getCoordinates().first][player.getCoordinates().second].texture));
        map[player.getCoordinates().first][player.getCoordinates().second].getTexture().eraseColor(Color.BLUE);
        enemies.add(new Enemy("Wolf", 30, 0, 5, 0, 10, 5, drop.get(0)));
        chances_of_fight.put(2, 50);
        chances_of_fight.put(0, 75);
        chances_of_fight.put(1, 30);
        chances_of_fight.put(3, 10);
        chances_of_fight.put(4, 30);
        chances_of_fight.put(5, 30);
        chances_of_enemy.put(0, new HashMap<>());
        chances_of_enemy.put(1, new HashMap<>());
        chances_of_enemy.put(4, new HashMap<>());
        chances_of_enemy.put(3, new HashMap<>());
        chances_of_enemy.put(2, new HashMap<>());
        elements.add(new Element("Pure mana", -1, 2, false));
        elements.add(new Element("Fire", 0, 10, false));
        elements.add(new Element("Water", 1, 5, false));
        elements.add(new Element("Air", 2, 5, false));
        elements.add(new Element("Earth", 3, 8, false));
        elements.add(new Element("Life", 4, -5, false));
        elements.add(new Element("Death", 5, 5,false));
        elements.add(new Element("Light", 6, 2,false));
        elements.add(new Element("Darkness", 7, 7,false));
        types.add(new Type("Self", 0, true));
        forms.add(new Form("Sphere", 0, true));
        mana_reservoirs.add(new ManaReservoir("Basic", 1, true));
        mana_channels.add(new ManaChannel("Basic", 2, true));
        menu[0]=Bitmap.createScaledBitmap(b[2][0], width/4, width/4, false);
        menu[2]=Bitmap.createScaledBitmap(b[0][0], width/4, width/4, false);
    }


    class MapTitle{
        private final Pair<Integer, Integer> coords;
        private Bitmap texture;
        private int type;

        public MapTitle(Pair<Integer, Integer> coords, Bitmap texture, int type) {
            this.coords = coords;
            this.texture = texture;
            this.type = type;
        }

        public Pair<Integer, Integer> getCoords() {
            return coords;
        }

        public void setTexture(Bitmap texture) {
            this.texture = Bitmap.createBitmap(texture);
        }

        public Bitmap getTexture() {
            return texture;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}