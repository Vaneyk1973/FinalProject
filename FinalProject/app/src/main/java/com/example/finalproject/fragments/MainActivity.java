package com.example.finalproject.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.entities.Enemy;
import com.example.finalproject.service.A;
import com.example.finalproject.service.Research;
import com.example.finalproject.service.spell.Element;
import com.example.finalproject.service.spell.Form;
import com.example.finalproject.items.Item;
import com.example.finalproject.service.spell.ManaChannel;
import com.example.finalproject.service.spell.ManaReservoir;
import com.example.finalproject.entities.Player;
import com.example.finalproject.R;
import com.example.finalproject.service.spell.Type;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static Player player;
    public static int menu_width, avatar_width, map_title_width;
    public static HashMap<Integer, Integer> chances_of_fight = new HashMap<>();
    public static MapTitle[][] map = new MapTitle[32][32];
    public static Bitmap[] menu = new Bitmap[4];
    public static HashMap<Integer, HashMap<Integer, Enemy>> chances_of_enemy = new HashMap<>();
    public static HashMap<Integer, Bitmap> title_textures = new HashMap<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static HashMap<Integer, ArrayList<Pair<Item, Integer>>> drop = new HashMap<>();
    public static ArrayList<Element> elements = new ArrayList<>();
    public static ArrayList<ManaChannel> mana_channels = new ArrayList<>();
    public static ArrayList<Type> types = new ArrayList<>();
    public static ArrayList<Form> forms = new ArrayList<>();
    public static ArrayList<ManaReservoir> mana_reservoirs = new ArrayList<>();
    public static ArrayList<Research> researches;
    private static Display display;
    private static Resources res;
    private static Bitmap[][] b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = getWindowManager().getDefaultDisplay();
        res = getResources();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, new MapFragment());
        fragmentTransaction.add(R.id.status, new StatusBarFragment());
        fragmentTransaction.add(R.id.menu, new MenuFragment());
        fragmentTransaction.commit();
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        setInitialData();
        Log.d("KKC", player.toString());
        player.setTitle_texture(Bitmap.createBitmap(map[player.getCoordinates().first][player.getCoordinates().second].texture));
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://m5hw.herokuapp.com/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        A a = retrofit.create(A.class);
        a.is_registered(MainActivity.player.getUser().getLogin()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null) {
                    MainActivity.player.setRegistered(response.body());
                    if (response.body()) {
                        a.is_logged_in(MainActivity.player.getUser().getLogin()).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.body())
                                    MainActivity.player.getUser().log_in();
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.d("KKFF", t.toString());
                            }
                        });
                    }
                }
                Log.d("KKKKKKKK", MainActivity.player.getUser().isLogged_in() + " " + MainActivity.player.isRegistered());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("KKPSS", t.toString());
            }
        });
    }

    private static void set_textures() {
        Point size = new Point();
        display.getSize(size);
        Bitmap a;
        int width = size.x, height = size.y;
        double xy = width * 1.0 / height;
        Log.d("KKQQ", xy + "");
        if (xy >= 0.4 && xy <= 0.6) {
            menu_width = width / 4;
            avatar_width = width / 3;
            map_title_width = width * 100 / 500;
        } else {
            map_title_width = height * 100 / 1000;
            menu_width = width / 4;
            avatar_width = width / 5;
        }
        a = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.xxx), map_title_width * 10, map_title_width * 10, false);
        int n = 10, m = 10;
        b = new Bitmap[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                b[i][j] = Bitmap.createBitmap(a, i * map_title_width, j * map_title_width, map_title_width, map_title_width);
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new MapTitle(new Pair<>(i, j), Bitmap.createBitmap(map_title_width, map_title_width, Bitmap.Config.ARGB_8888), 0);
                if ((i <= 1 || j <= 1) || (i >= map.length - 2 || j >= map.length - 2)) {
                    map[i][j].setType(0);
                } else {
                    map[i][j].setType(2);
                }
            }
        }
        for (int i = 2; i < 11; i++) {
            for (int j = 2; j < 11; j++) {
                map[j][i].setType(1);
            }
        }
        for (int i = 15; i < 20; i++) {
            for (int j = 17; j < 22; j++) {
                map[i][j].setType(1);
            }
        }
        for (int i = 20; i < 23; i++) {
            for (int j = 6; j < 9; j++) {
                map[i][j].setType(1);
            }
        }
        for (int i = 7; i < 20; i++) {
            map[6][i].setType(5);
        }
        for (int i = 20; i < 30; i++) {
            map[6][i].setType(7);
        }
        for (int i = 7; i < 21; i++) {
            map[i][19].setType(6);
        }
        for (int i = 19; i > 6; i -= 2) {
            map[21][i].setType(5);
        }
        map[6][6].setType(3);
        map[17][19].setType(3);
        map[21][7].setType(3);
        for (int i = 0; i <= 7; i++) {
            title_textures.put(i, Bitmap.createBitmap(b[i][1]));
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j].setTexture(title_textures.get(map[i][j].getType()));
            }
        }
        menu[0] = Bitmap.createScaledBitmap(b[0][0], menu_width, menu_width, false);
        menu[1] = Bitmap.createScaledBitmap(b[1][0], menu_width, menu_width, false);
        menu[2] = Bitmap.createScaledBitmap(b[2][0], menu_width, menu_width, false);
        menu[3] = Bitmap.createScaledBitmap(b[3][0], menu_width, menu_width, false);
        map[player.getCoordinates().first][player.getCoordinates().second].getTexture().eraseColor(Color.BLUE);
    }

    private static void set_researches() {
        researches = new ArrayList<>();
        ArrayList<Research> rqr = new ArrayList<>();
        researches.add(new Research(null, "Basic spell creation", 1, 0, 0, false, true));
        rqr.add(researches.get(0));
        researches.add(new Research((ArrayList<Research>) rqr.clone(), "Fire mage", 3, 1, 1, false, false));
        researches.add(new Research((ArrayList<Research>) rqr.clone(), "Water mage", 3, 1, 2, false, false));
        researches.add(new Research((ArrayList<Research>) rqr.clone(), "Earth mage", 3, 1, 3, false, false));
        researches.add(new Research((ArrayList<Research>) rqr.clone(), "Air mage", 3, 1, 4, false, false));
    }

    private static void set_drop() {
        drop.put(0, new ArrayList<>());
        drop.get(0).add(new Pair<>(new Item(2, "Rabbit's fur"), 70));
        drop.get(0).add(new Pair<>(new Item(3, "Rabbit's leg"), 30));
        drop.put(1, new ArrayList<>());
        drop.get(1).add(new Pair<>(new Item(5, "Dog's fur"), 100));
        drop.put(2, new ArrayList<>());
        drop.put(3, new ArrayList<>());
        drop.put(4, new ArrayList<>());
        drop.put(5, new ArrayList<>());
    }

    private static void set_enemies() {
        enemies.add(new Enemy("Rabbit", 5, 0, 1, 0, 1, 1, drop.get(0), b[2][5]));
        enemies.add(new Enemy("Dog", 15, 0, 2, 0, 5, 5, drop.get(1), b[4][5]));
        enemies.add(new Enemy("Wolf", 35, 0, 5, 0, 10, 10, drop.get(2), b[0][5]));
        enemies.add(new Enemy("Fox", 20, 0, 3, 0, 7, 6, drop.get(3), b[1][5]));
        enemies.add(new Enemy("Bear", 100, 10, 15, 0, 50, 200, drop.get(4), b[3][5]));
        enemies.add(new Enemy("Highwayman", 50, 10, 20, 0, 100, 50, drop.get(5), b[5][5]));
        chances_of_fight.put(0, 0);
        chances_of_fight.put(1, 20);
        chances_of_fight.put(2, 60);
        chances_of_fight.put(3, 0);
        chances_of_fight.put(4, 10);
        chances_of_fight.put(5, 10);
        chances_of_fight.put(6, 5);
        chances_of_fight.put(7, 5);
        chances_of_enemy.put(1, new HashMap<>());
        chances_of_enemy.get(1).put(30, enemies.get(0));
        chances_of_enemy.get(1).put(70, enemies.get(1));
        chances_of_enemy.put(2, new HashMap<>());
        chances_of_enemy.get(2).put(60, enemies.get(2));
        chances_of_enemy.get(2).put(35, enemies.get(3));
        chances_of_enemy.get(2).put(5, enemies.get(4));
        chances_of_enemy.put(4, new HashMap<>());
        chances_of_enemy.get(4).put(100, enemies.get(5));
        chances_of_enemy.put(5, new HashMap<>());
        chances_of_enemy.get(5).put(100, enemies.get(5));
        chances_of_enemy.put(6, new HashMap<>());
        chances_of_enemy.get(6).put(100, enemies.get(5));
        chances_of_enemy.put(7, new HashMap<>());
        chances_of_enemy.get(7).put(100, enemies.get(5));
        chances_of_enemy.put(8, new HashMap<>());
        chances_of_enemy.get(8).put(100, enemies.get(5));
    }

    private static void set_magic() {
        elements.add(new Element("Pure mana", -1, 2, false));
        elements.add(new Element("Fire", 0, 10, false));
        elements.add(new Element("Water", 1, 5, false));
        elements.add(new Element("Air", 2, 5, false));
        elements.add(new Element("Earth", 3, 8, false));
        elements.add(new Element("Life", 4, -5, false));
        elements.add(new Element("Death", 5, 5, false));
        elements.add(new Element("Light", 6, 2, false));
        elements.add(new Element("Darkness", 7, 7, false));
        types.add(new Type("Self", 0, true));
        forms.add(new Form("Sphere", 0, true));
        mana_reservoirs.add(new ManaReservoir("Basic", 1, true));
        mana_channels.add(new ManaChannel("Basic", 2, true));
    }

    protected static void setInitialData() {
        set_textures();
        set_researches();
        set_drop();
        set_enemies();
        set_magic();
    }

    @Override
    protected void onStart() {
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        set_researches();
        Log.d("KKSt", player.toString());
        super.onStart();
    }

    @Override
    protected void onResume() {
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        Log.d("KKR", player.toString());
        super.onResume();
    }

    @Override
    protected void onRestart() {
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        Log.d("KKRe", player.toString());
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.apply();
        Log.d("KKD", player.toString());
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.apply();
        Log.d("KKS", player.toString());
        super.onStop();
    }

    @Override
    protected void onPause() {
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.apply();
        Log.d("KKP", player.toString());
        super.onPause();
    }


    static class MapTitle {
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