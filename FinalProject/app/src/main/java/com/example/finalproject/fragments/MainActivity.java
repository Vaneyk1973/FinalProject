package com.example.finalproject.fragments;

import android.content.Context;
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
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.entities.Enemy;
import com.example.finalproject.items.Armor;
import com.example.finalproject.service.A;
import com.example.finalproject.service.Music;
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
    public static int menu_width, avatar_width, map_title_width, status_images_width, category_image_width;
    public static boolean show_tutorial = true;
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
    public static ArrayList<Research> researches = new ArrayList<>();
    public static ArrayList<String> researches1 = new ArrayList<>(),
            elements1 = new ArrayList<>(),
            mana_channels1 = new ArrayList<>(),
            types1 = new ArrayList<>(),
            forms1 = new ArrayList<>(),
            mana_reservoirs1 = new ArrayList<>();
    public static HashMap<Integer, String> categories = new HashMap<>();
    private static Display display;
    private static Resources res;
    public static Bitmap[][] b;
    public static Music m;
    private SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        sh = getPreferences(MODE_PRIVATE);
        researches1 = new Gson().fromJson(sh.getString("Researches", ""), ArrayList.class);
        elements1 = new Gson().fromJson(sh.getString("Elements", ""), ArrayList.class);
        types1 = new Gson().fromJson(sh.getString("Types", ""), ArrayList.class);
        forms1 = new Gson().fromJson(sh.getString("Forms", ""), ArrayList.class);
        mana_channels1 = new Gson().fromJson(sh.getString("Mana channels", ""), ArrayList.class);
        mana_reservoirs1 = new Gson().fromJson(sh.getString("Mana reservoirs", ""), ArrayList.class);
        m = new Music();
        m.start(this, R.raw.main);
        setContentView(R.layout.activity_main);
        display = getWindowManager().getDefaultDisplay();
        res = getResources();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        show_tutorial = sh.getBoolean("Tutorial", true);
        setInitialData();
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
                }
                Log.d("KKKKKKKK", MainActivity.player.getUser().isLogged_in() + " " + MainActivity.player.isRegistered());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("KKPSS", t.toString());
            }
        });
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
        player.setAvatar(Bitmap.createBitmap(b[5][5]));
        if (show_tutorial) {
            fragmentTransaction.add(R.id.tutorial, new TutorialFragment());
        } else {
            fragmentTransaction.add(R.id.map, new MapFragment());
            fragmentTransaction.add(R.id.status, new StatusBarFragment());
            fragmentTransaction.add(R.id.menu, new MenuFragment());
        }
        Log.d("KKC", player.toString());
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
            map_title_width = height * 100 / 900;
            menu_width = width / 4;
            avatar_width = width / 4;
        }
        status_images_width = width / 10;
        category_image_width = width / 4;
        a = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.textures), map_title_width * 10, map_title_width * 10, false);
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
        if (researches1 != null)
            for (int i = 0; i < researches1.size(); i++)
                researches.add(new Gson().fromJson(researches1.get(i), Research.class));
        else {
            researches1 = new ArrayList<>();
            ArrayList<Research> rqr = new ArrayList<>();
            researches.add(new Research(new ArrayList<Research>(), "Basic spell creation", 1, 0, 0, false, true));
            rqr.add(researches.get(0));
            researches.add(new Research((ArrayList<Research>) rqr.clone(), "Fire mage", 6, 1, 1, false, false));
            researches.add(new Research((ArrayList<Research>) rqr.clone(), "Water mage", 2, 1, 2, false, false));
            researches.add(new Research((ArrayList<Research>) rqr.clone(), "Earth mage", 4, 1, 3, false, false));
            researches.add(new Research((ArrayList<Research>) rqr.clone(), "Air mage", 3, 1, 4, false, false));
            for (int i = 0; i < researches.size(); i++)
                researches1.add(new Gson().toJson(researches.get(i)));
        }

    }

    private static void set_drop() {
        drop.put(0, new ArrayList<>());
        drop.get(0).add(new Pair<>(new Item(2, 0, 2, "Rabbit's fur"), 70));
        drop.get(0).add(new Pair<>(new Item(3, 0, 3, "Rabbit's leg"), 30));
        drop.put(1, new ArrayList<>());
        drop.get(1).add(new Pair<>(new Item(5, 0, 2, "Dog's fur"), 90));
        drop.get(1).add(new Pair<>(new Item(30, 0, 2, "Dog's tooth"), 10));
        drop.put(2, new ArrayList<>());
        drop.get(2).add(new Pair<>(new Item(15, 0, 2, "Wolf's fur"), 100));
        drop.put(3, new ArrayList<>());
        drop.get(3).add(new Pair<>(new Item(20, 0, 2, "Fox's fur"), 100));
        drop.put(4, new ArrayList<>());
        drop.get(4).add(new Pair<>(new Item(50, 0, 2, "Bear's fur"), 100));
        drop.put(5, new ArrayList<>());
        drop.get(5).add(new Pair<>(new Armor(75, 5, 10, 1, 1, "Iron chestplate"), 50));
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
        elements.clear();
        types.clear();
        forms.clear();
        mana_reservoirs.clear();
        mana_channels.clear();
        if (elements1 != null)
            for (int i = 0; i < elements1.size(); i++)
                elements.add(new Gson().fromJson(elements1.get(i), Element.class));
        else {
            elements.add(new Element("Pure mana", 6, 2, false));
            elements.add(new Element("Fire", 1, 10, false));
            elements.add(new Element("Water", 0, 5, false));
            elements.add(new Element("Air", 3, 4, false));
            elements.add(new Element("Earth", 2, 8, false));
            elements.add(new Element("Life", 5, -5, false));
            elements.add(new Element("Death", 4, 5, false));
/*        elements.add(new Element("Light", 6, 2, false));
        elements.add(new Element("Darkness", 7, 7, false));*/
            elements1 = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++)
                elements1.add(new Gson().toJson(elements.get(i)));
        }

        if (types1 != null)
            for (int i = 0; i < types1.size(); i++)
                types.add(new Gson().fromJson(types1.get(i), Type.class));
        else {
            types.add(new Type("On enemy", 0, true));
            types1 = new ArrayList<>();
            for (int i = 0; i < types.size(); i++)
                types1.add(new Gson().toJson(types.get(i)));
        }

        if (forms1 != null)
            for (int i = 0; i < forms1.size(); i++)
                forms.add(new Gson().fromJson(forms1.get(i), Form.class));
        else {
            forms.add(new Form("Sphere", 0, true));
            forms1 = new ArrayList<>();
            for (int i = 0; i < forms.size(); i++)
                forms1.add(new Gson().toJson(forms.get(i)));
        }

        if (mana_reservoirs1 != null)
            for (int i = 0; i < mana_reservoirs1.size(); i++)
                mana_reservoirs.add(new Gson().fromJson(mana_reservoirs1.get(i), ManaReservoir.class));
        else {
            mana_reservoirs.add(new ManaReservoir("Basic", 1, true));
            mana_reservoirs.add(new ManaReservoir("Big", 10, true));
            mana_reservoirs1 = new ArrayList<>();
            for (int i = 0; i < mana_reservoirs.size(); i++)
                mana_reservoirs1.add(new Gson().toJson(mana_reservoirs.get(i)));
        }

        if (mana_channels1 != null)
            for (int i = 0; i < mana_channels1.size(); i++)
                mana_channels.add(new Gson().fromJson(mana_channels1.get(i), ManaChannel.class));
        else {
            mana_channels.add(new ManaChannel("Basic", 2, true));
            mana_channels1 = new ArrayList<>();
            for (int i = 0; i < mana_channels.size(); i++)
                mana_channels1.add(new Gson().toJson(mana_channels.get(i)));
        }
    }

    protected static void setInitialData() {
        categories.put(0, "Armor/weapon");
        categories.put(1, "Food/potions");
        categories.put(2, "Resources");
        categories.put(3, "Other");
        set_textures();
        set_researches();
        set_drop();
        set_enemies();
        set_magic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m.start(this, R.raw.main);
    }

/*    @Override
    protected void onDestroy() {
        m.stop();
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Elements", new Gson().toJson(elements1));
        ed.putString("Types", new Gson().toJson(types1));
        ed.putString("Forms", new Gson().toJson(forms1));
        ed.putString("Mana channels", new Gson().toJson(mana_channels1));
        ed.putString("Mana reservoirs", new Gson().toJson(mana_reservoirs1));
        ed.apply();
        Log.d("KKD", player.toString());
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        m.stop();
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Elements", new Gson().toJson(elements1));
        ed.putString("Types", new Gson().toJson(types1));
        ed.putString("Forms", new Gson().toJson(forms1));
        ed.putString("Mana channels", new Gson().toJson(mana_channels1));
        ed.putString("Mana reservoirs", new Gson().toJson(mana_reservoirs1));
        ed.apply();
        Log.d("KKS", player.toString());
        super.onStop();
    }*/

    @Override
    protected void onPause() {
        m.stop();
        sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.putBoolean("Tutorial", show_tutorial);
        ed.putString("Researches", new Gson().toJson(researches1));
        ed.putString("Elements", new Gson().toJson(elements1));
        ed.putString("Types", new Gson().toJson(types1));
        ed.putString("Forms", new Gson().toJson(forms1));
        ed.putString("Mana channels", new Gson().toJson(mana_channels1));
        ed.putString("Mana reservoirs", new Gson().toJson(mana_reservoirs1));
        ed.apply();
        Log.d("KKP", player.toString());
        Log.d("KKKLLL", new Gson().toJson(MainActivity.researches));
        Log.d("KKKLL!", new Gson().toJson(MainActivity.researches1));
        Log.d("KKKLLLLL", sh.getString("Researches", ""));
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