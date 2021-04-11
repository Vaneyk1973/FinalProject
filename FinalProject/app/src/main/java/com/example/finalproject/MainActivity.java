package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Pair;
import android.view.Display;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static Player player;
    public static HashMap<Integer, Integer> chances_of_fight =new HashMap<>();
    public static MapTitle[][] map=new MapTitle[11][11];
    public static HashMap<Integer, HashMap<Enemy, Integer>> chances_of_enemy=new HashMap<>();
    public static ArrayList<Enemy> enemies=new ArrayList<>();
    public static HashMap<Integer, ArrayList<Pair<Item, Integer>>> drop=new HashMap<>();
    public static ArrayList<Pair<Element, Boolean>> elements=new ArrayList<>();
    public static ArrayList<Pair<ManaChannel, Boolean>> manaChannels =new ArrayList<>();
    public static ArrayList<Pair<Type, Boolean>> types =new ArrayList<>();
    public static ArrayList<Pair<Form, Boolean>> forms =new ArrayList<>();
    public static ArrayList<Pair<ManaReservoir, Boolean>> manaReservoirs =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, new Map());
        fragmentTransaction.add(R.id.status, new StatusBar());
        fragmentTransaction.add(R.id.menu, new Menu());
        fragmentTransaction.commit();
        setInitialData();
    }

    private void setInitialData(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        player=new Player(4, 4);
        for (int i=0;i<11;i++){
            for (int j=0;j<11;j++){
                map[i][j]=new MapTitle(new Pair<>(i, j), Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888), 0);
                if ((i<=1||j<=1)||(i>=map.length-2||j>=map.length-2)){
                    map[i][j].getTexture().eraseColor(Color.GREEN);
                    map[i][j].setType(-1);
                }
                else{
                    map[i][j].getTexture().eraseColor(Color.BLACK);
                }
            }
        }
        map[5][5].getTexture().eraseColor(Color.YELLOW);
        map[4][4].getTexture().eraseColor(Color.GRAY);
        drop.put(0, new ArrayList<>());
        drop.get(0).add(new Pair<>(new Item("yyy"), 70));
        drop.get(0).add(new Pair<>(new Item("rrr"), 30));
        player.setTitle_texture(Bitmap.createBitmap(map[player.getCoordinates().first][player.getCoordinates().second].texture));
        map[player.getCoordinates().first][player.getCoordinates().second].getTexture().eraseColor(Color.BLUE);
        enemies.add(new Enemy("Wolf", 30, 0, 5, 10, drop.get(0)));
        chances_of_fight.put(2, 50);
        chances_of_fight.put(0, 75);
        chances_of_fight.put(1, 30);
        chances_of_fight.put(3, 10);
        chances_of_fight.put(4, 30);
        chances_of_enemy.put(0, new HashMap<>());
        chances_of_enemy.put(1, new HashMap<>());
        chances_of_enemy.put(4, new HashMap<>());
        chances_of_enemy.put(3, new HashMap<>());
        chances_of_enemy.put(2, new HashMap<>());
        for (int i=0;i<7;i++)
            elements.add(new Pair<>(new Element("fire", i), true));
        types.add(new Pair<>(new Type("self", 12), true));
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
            this.texture = texture;
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