package com.example.finalproject.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.R;
import com.example.finalproject.entities.Enemy;
import com.example.finalproject.entities.Player;
import com.example.finalproject.items.Armor;
import com.example.finalproject.items.Item;
import com.example.finalproject.service.Music;
import com.example.finalproject.service.Research;
import com.example.finalproject.service.User;
import com.example.finalproject.service.spell.Element;
import com.example.finalproject.service.spell.Form;
import com.example.finalproject.service.spell.ManaChannel;
import com.example.finalproject.service.spell.ManaReservoir;
import com.example.finalproject.service.spell.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static Player player;
    public static int menuWidth, avatarWidth, mapTitleWidth, statusImagesWidth, categoryImageWidth;
    public static boolean showTutorial = true;
    public static HashMap<Integer, Integer> chancesOfFight = new HashMap<>();
    public static ArrayList<Map> map=new ArrayList<>();
    public static Bitmap[] menu = new Bitmap[4];
    public static HashMap<Integer, HashMap<Integer, Enemy>> chancesOfEnemy = new HashMap<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static HashMap<Integer, ArrayList<Pair<Item, Integer>>> drop = new HashMap<>();
    public static ArrayList<Element> elements = new ArrayList<>();
    public static ArrayList<ManaChannel> manaChannels = new ArrayList<>();
    public static ArrayList<Type> types = new ArrayList<>();
    public static ArrayList<Form> forms = new ArrayList<>();
    public static ArrayList<ManaReservoir> manaReservoirs = new ArrayList<>();
    public static ArrayList<Research> researches = new ArrayList<>();
    public static ArrayList<String> researches1 = new ArrayList<>(),
            elements1 = new ArrayList<>(),
            manaChannels1 = new ArrayList<>(),
            types1 = new ArrayList<>(),
            forms1 = new ArrayList<>(),
            manaReservoirs1 = new ArrayList<>();
    public static ArrayList<Bitmap> mapTextures =new ArrayList<>();
    public static HashMap<Integer, String> categories = new HashMap<>();
    private static Display display;
    private static Resources res;
    public static Bitmap[][] b;
    public static Music m;
    private static SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        XmlPullParser parser=getResources().getXml(R.xml.start_map);
        map.add(new Map(parser));
        parser=getResources().getXml(R.xml.first_village_map);
        map.add(new Map(parser));
        sh = getPreferences(MODE_PRIVATE);
        display = getWindowManager().getDefaultDisplay();
        res = getResources();
        m = new Music();
        m.start(this, R.raw.main);
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        showTutorial = sh.getBoolean("Tutorial", true);
        setInitialData();
        if (showTutorial) {
            fragmentTransaction.add(R.id.tutorial, new TutorialFragment());
        } else {
            fragmentTransaction.add(R.id.map, new MapFragment());
            fragmentTransaction.add(R.id.status, new StatusBarFragment());
            fragmentTransaction.add(R.id.menu, new MenuFragment());
        }
        fragmentTransaction.commit();
    }

    private static void set_textures() {
        Point size = new Point();
        display.getSize(size);
        Bitmap a;
        int width = size.x, height = size.y;
        double xy = width * 1.0 / height;
        if (xy >= 0.4 && xy <= 0.6) {
            menuWidth = width / 4;
            avatarWidth = width / 3;
            mapTitleWidth = width * 100 / 500;
        } else {
            mapTitleWidth = height * 100 / 900;
            menuWidth = width / 4;
            avatarWidth = width / 4;
        }
        statusImagesWidth = width / 10;
        categoryImageWidth = width / 4;
        int n =10, m = 20;
        a = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, R.drawable.textures),
                mapTitleWidth * m, mapTitleWidth * n, false);
        b = new Bitmap[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                b[i][j] = Bitmap.createBitmap(a,
                        j * mapTitleWidth,
                        i * mapTitleWidth,
                        mapTitleWidth,
                        mapTitleWidth);
            }
        }
        mapTextures.addAll(Arrays.asList(b[1]));
        for (int i=0;i<menu.length;i++)
            menu[i] = Bitmap.createScaledBitmap(b[0][i], menuWidth, menuWidth, false);
        for (int i=0;i<map.size();i++)
            for (int j=0;j<map.get(i).length;j++)
                for (int k=0;k<map.get(i).width;k++)
                    map.get(i).map[j][k].setTexture(mapTextures.get(map.get(i).map[j][k].type));
        player.setAvatar(Bitmap.createBitmap(b[5][5]));
    }

    private static void set_researches() {
        researches = new ArrayList<>();
        if (researches1 != null)
            for (int i = 0; i < researches1.size(); i++)
                researches.add(new Gson().fromJson(researches1.get(i), Research.class));
        else {
            researches1 = new ArrayList<>();
            ArrayList<Research> rqr = new ArrayList<>();
            researches.add(new Research(new ArrayList<>(),
                    "Basic spell creation", 1, 0, 0, false, true));
            rqr.add(researches.get(0));
            researches.add(new Research((ArrayList<Research>) rqr.clone(),
                    "Fire mage", 6, 1, 1, false, false));
            researches.add(new Research((ArrayList<Research>) rqr.clone(),
                    "Water mage", 2, 1, 2, false, false));
            researches.add(new Research((ArrayList<Research>) rqr.clone(),
                    "Earth mage", 4, 1, 3, false, false));
            researches.add(new Research((ArrayList<Research>) rqr.clone(),
                    "Air mage", 3, 1, 4, false, false));
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
        FirebaseDatabase.getInstance().getReference("Auction").child("Iron chestplate")
                .setValue(new Armor(75, 5, 10, 1, 1, "Iron chestplate"))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful())
                    FirebaseDatabase.getInstance().getReference("Auction").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                            Log.d("KKK", task.getResult().getValue()+"");
                        }
                    });
            }
        });
    }

    private static void set_enemies() {
        enemies.add(new Enemy("Rabbit", 5, 0, 1, 0, 1, 1, drop.get(0), b[2][5]));
        enemies.add(new Enemy("Dog", 15, 0, 2, 0, 5, 5, drop.get(1), b[4][5]));
        enemies.add(new Enemy("Wolf", 35, 0, 5, 0, 10, 10, drop.get(2), b[0][5]));
        enemies.add(new Enemy("Fox", 20, 0, 3, 0, 7, 6, drop.get(3), b[1][5]));
        enemies.add(new Enemy("Bear", 100, 10, 15, 0, 50, 200, drop.get(4), b[3][5]));
        enemies.add(new Enemy("Highwayman", 50, 10, 20, 0, 100, 50, drop.get(5), b[5][5]));
        chancesOfFight.put(0, 0);
        chancesOfFight.put(1, 20);
        chancesOfFight.put(2, 60);
        chancesOfFight.put(3, 0);
        chancesOfFight.put(4, 10);
        chancesOfFight.put(5, 10);
        chancesOfFight.put(6, 5);
        chancesOfFight.put(7, 5);
        chancesOfFight.put(8, 0);
        chancesOfFight.put(9, 0);
        chancesOfFight.put(10, 0);
        chancesOfEnemy.put(1, new HashMap<>());
        chancesOfEnemy.get(1).put(30, enemies.get(0));
        chancesOfEnemy.get(1).put(70, enemies.get(1));
        chancesOfEnemy.put(2, new HashMap<>());
        chancesOfEnemy.get(2).put(60, enemies.get(2));
        chancesOfEnemy.get(2).put(35, enemies.get(3));
        chancesOfEnemy.get(2).put(5, enemies.get(4));
        chancesOfEnemy.put(4, new HashMap<>());
        chancesOfEnemy.get(4).put(100, enemies.get(5));
        chancesOfEnemy.put(5, new HashMap<>());
        chancesOfEnemy.get(5).put(100, enemies.get(5));
        chancesOfEnemy.put(6, new HashMap<>());
        chancesOfEnemy.get(6).put(100, enemies.get(5));
        chancesOfEnemy.put(7, new HashMap<>());
        chancesOfEnemy.get(7).put(100, enemies.get(5));
        chancesOfEnemy.put(8, new HashMap<>());
        chancesOfEnemy.get(8).put(100, enemies.get(5));
    }

    private static void set_magic() {
        elements.clear();
        types.clear();
        forms.clear();
        manaReservoirs.clear();
        manaChannels.clear();
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

        if (manaReservoirs1 != null)
            for (int i = 0; i < manaReservoirs1.size(); i++)
                manaReservoirs.add(new Gson().fromJson(manaReservoirs1.get(i), ManaReservoir.class));
        else {
            manaReservoirs.add(new ManaReservoir("Basic", 1, true));
            manaReservoirs.add(new ManaReservoir("Big", 10, true));
            manaReservoirs1 = new ArrayList<>();
            for (int i = 0; i < manaReservoirs.size(); i++)
                manaReservoirs1.add(new Gson().toJson(manaReservoirs.get(i)));
        }

        if (manaChannels1 != null)
            for (int i = 0; i < manaChannels1.size(); i++)
                manaChannels.add(new Gson().fromJson(manaChannels1.get(i), ManaChannel.class));
        else {
            manaChannels.add(new ManaChannel("Basic", 2, true));
            manaChannels1 = new ArrayList<>();
            for (int i = 0; i < manaChannels.size(); i++)
                manaChannels1.add(new Gson().toJson(manaChannels.get(i)));
        }
    }

    protected static void setInitialData() {
        researches1 = new Gson().fromJson(sh.getString("Researches", ""), ArrayList.class);
        elements1 = new Gson().fromJson(sh.getString("Elements", ""), ArrayList.class);
        types1 = new Gson().fromJson(sh.getString("Types", ""), ArrayList.class);
        forms1 = new Gson().fromJson(sh.getString("Forms", ""), ArrayList.class);
        manaChannels1 = new Gson().fromJson(sh.getString("Mana channels", ""), ArrayList.class);
        manaReservoirs1 = new Gson().fromJson(sh.getString("Mana reservoirs", ""), ArrayList.class);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        m.start(this, R.raw.main);
        sh=getPreferences(MODE_PRIVATE);
        player=new Gson().fromJson(sh.getString("Payer", ""), Player.class);
        player.setUser(new Gson().fromJson(sh.getString("User", ""), User.class));
        showTutorial=sh.getBoolean("Tutorial", true);
        researches1=new ArrayList<String>(new Gson().fromJson(sh.getString("Researches", ""), ArrayList.class));
        elements1=new ArrayList<>(new Gson().fromJson(sh.getString("Elements", ""), ArrayList.class));
        types1=new ArrayList<>(new Gson().fromJson(sh.getString("Types", ""), ArrayList.class));
        forms1=new ArrayList<>(new Gson().fromJson(sh.getString("Forms", ""), ArrayList.class));
        manaChannels1=new ArrayList<>(new Gson().fromJson(sh.getString("Mana channels", ""), ArrayList.class));
        manaReservoirs1=new ArrayList<>(new Gson().fromJson(sh.getString("Mana reservoirs", ""), ArrayList.class));
    }

    @Override
    protected void onPause() {
        m.stop();
        sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.putString("User", new Gson().toJson(player.getUser()));
        ed.putBoolean("Tutorial", showTutorial);
        ed.putString("Researches", new Gson().toJson(researches1));
        ed.putString("Elements", new Gson().toJson(elements1));
        ed.putString("Types", new Gson().toJson(types1));
        ed.putString("Forms", new Gson().toJson(forms1));
        ed.putString("Mana channels", new Gson().toJson(manaChannels1));
        ed.putString("Mana reservoirs", new Gson().toJson(manaReservoirs1));
        ed.apply();
        super.onPause();
    }

    public static class MapTitle {
        private final Pair<Integer, Integer> coordinates;
        private Bitmap texture;
        private int type;

        public MapTitle(Pair<Integer, Integer> coordinates, Bitmap texture, int type) {
            this.coordinates = coordinates;
            this.texture = texture;
            this.type = type;
        }

        public Pair<Integer, Integer> getCoordinates() {
            return coordinates;
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

    public class Map{
        private int length, width;
        private MapTitle[][] map;

        public Map(XmlPullParser map_xml){
            ArrayList<ArrayList<MapTitle>> map=new ArrayList();
            Pair<Integer, Integer> coordinates;
            Bitmap texture;
            int type;
            try {
                while (map_xml.getEventType()!= XmlPullParser.END_DOCUMENT) {
                    if (map_xml.getEventType() == XmlPullParser.START_TAG && map_xml.getName().equals("row"))
                        map.add(new ArrayList<>());
                    if (map_xml.getEventType()==XmlPullParser.START_TAG&& map_xml.getName().equals("map_title")){
                        coordinates=new Pair<>(map.size()-1, map.get(0).size());
                        type= Integer.parseInt(map_xml.getAttributeValue(0));
                        map.get(map.size()-1).add(new MapTitle(coordinates, null, type));
                    }
                    map_xml.next();
                }
            }
            catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            width=map.get(0).size();
            length=map.size();
            this.map=new MapTitle[length][width];
            for (int i=0;i<length;i++)
                for (int j=0;j<width;j++)
                    this.map[i][j]=map.get(i).get(j);
        }

        public MapTitle[][] getMap() {
            return map;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}