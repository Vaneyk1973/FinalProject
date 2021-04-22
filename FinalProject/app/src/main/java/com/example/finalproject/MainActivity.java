package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Pair;
import android.view.Display;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static Player player;
    public static HashMap<Integer, Integer> chances_of_fight =new HashMap<>();
    public static MapTitle[][] map=new MapTitle[11][11];
    public static Bitmap[] menu=new Bitmap[4];
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
        Bitmap a=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.xxx), width/5*9, width/5, false);
        Bitmap[][] b=new Bitmap[9][1];
        for (int i=0;i<9;i++){
            b[i][0]=Bitmap.createBitmap(a, i*width/5, 0, width/5, width/5);
        }
        player=new Player(2, 4);
        for (int i=0;i<11;i++){
            for (int j=0;j<11;j++){
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
        for (int i=0;i<11;i++){
            for (int j=0;j<11;j++){
                switch (map[i][j].getType()){
                    case 0: map[i][j].setTexture(b[4][0]); break;
                    case 1: map[i][j].setTexture(b[1][0]); break;
                    case 3: map[i][j].setTexture(b[3][0]); break;
                    case 5: map[i][j].setTexture(b[5][0]); break;
                }
            }
        }
        drop.put(0, new ArrayList<>());
        drop.get(0).add(new Pair<>(new Item("yyy"), 70));
        drop.get(0).add(new Pair<>(new Item("rrr"), 30));
        player.setTitle_texture(Bitmap.createBitmap(map[player.getCoordinates().first][player.getCoordinates().second].texture));
        map[player.getCoordinates().first][player.getCoordinates().second].getTexture().eraseColor(Color.BLUE);
        enemies.add(new Enemy("Wolf", 30, 0, 5, 0, drop.get(0)));
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
        for (int i=0;i<7;i++)
            elements.add(new Pair<>(new Element("fire", i), true));
        types.add(new Pair<>(new Type("Self", 0), true));
        forms.add(new Pair<>(new Form("Sphere", 0), true));
        manaReservoirs.add(new Pair<>(new ManaReservoir("Basic", 10), true));
        manaChannels.add(new Pair<>(new ManaChannel("Basic", 2), true));
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