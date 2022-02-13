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
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.service.MapTile;
import com.example.finalproject.R;
import com.example.finalproject.service.Research;
import com.example.finalproject.entities.Enemy;
import com.example.finalproject.entities.Player;
import com.example.finalproject.items.Armor;
import com.example.finalproject.items.Item;
import com.example.finalproject.items.Recipe;
import com.example.finalproject.service.Music;
import com.example.finalproject.service.Task;
import com.example.finalproject.service.Triplex;
import com.example.finalproject.service.User;
import com.example.finalproject.service.spell.Element;
import com.example.finalproject.service.spell.Form;
import com.example.finalproject.service.spell.ManaChannel;
import com.example.finalproject.service.spell.ManaReservoir;
import com.example.finalproject.service.spell.Type;
import com.google.gson.Gson;

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
    public static ArrayList<Map> map = new ArrayList<>();
    public static Bitmap[] menu = new Bitmap[4];
    public static HashMap<Integer, HashMap<Integer, Enemy>> chancesOfEnemy = new HashMap<>();
    public static HashMap<Integer, Enemy> enemies = new HashMap<>();
    public static HashMap<Integer, ArrayList<Pair<Item, Integer>>> drop = new HashMap<>();
    public static ArrayList<Element> elements = new ArrayList<>();
    public static ArrayList<ManaChannel> manaChannels = new ArrayList<>();
    public static ArrayList<Type> types = new ArrayList<>();
    public static ArrayList<Form> forms = new ArrayList<>();
    public static ArrayList<ManaReservoir> manaReservoirs = new ArrayList<>();
    public static ArrayList<Research> researches=new ArrayList<>();
    public static ArrayList<Recipe> recipes = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>(),
            shopList = new ArrayList<>();
    public static ArrayList<Task> tasks = new ArrayList<>();
    public static ArrayList<String> researchesJson = new ArrayList<>(),
            elementsJson = new ArrayList<>(),
            manaChannelsJson = new ArrayList<>(),
            typesJson = new ArrayList<>(),
            formsJson = new ArrayList<>(),
            manaReservoirsJson = new ArrayList<>();
    public static ArrayList<Bitmap> mapTextures = new ArrayList<>();
    public static HashMap<Integer, String> categories = new HashMap<>();
    public static HashMap<Integer, Integer> ids =new HashMap<>();
    public static HashMap<Integer, String> names=new HashMap<>();
    private static Display display;
    private static Resources res;
    public static Bitmap[][] textures;
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
        XmlPullParser mapParser = getResources().getXml(R.xml.start_map),
                itemsParser=getResources().getXml(R.xml.items),
                namesParser=getResources().getXml(R.xml.names),
                recipesParser=getResources().getXml(R.xml.recipes),
                enemiesParser=getResources().getXml(R.xml.enemies),
                locationsParser=getResources().getXml(R.xml.locations);
        map.add(new Map(mapParser));
        mapParser = getResources().getXml(R.xml.first_village_map);
        map.add(new Map(mapParser));
        sh = getPreferences(MODE_PRIVATE);
        display = getWindowManager().getDefaultDisplay();
        res = getResources();
        m = new Music();
        m.start(this, R.raw.main);
        showTutorial = sh.getBoolean("Tutorial", true);
        player=new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2,  2))), Player.class);
        setInitialData(itemsParser, namesParser, recipesParser, enemiesParser, locationsParser);
        if (showTutorial) {
            fragmentTransaction.add(R.id.tutorial, new TutorialFragment());
        } else {
            fragmentTransaction.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
            fragmentTransaction.add(R.id.status, new StatusBarFragment());
            fragmentTransaction.add(R.id.menu, new MenuFragment());
        }
        fragmentTransaction.commit();
    }

    private static void setTasks() {
        tasks.add(new Task("Earn 100 gold to get 50 exp and 50 gold", "First money"));
        tasks.add(new Task("Achieve level 5 to get 500 gold and 100 exp", "Getting power"));
    }

    private static void setNames(XmlPullParser namesXml){
        try {
            while (namesXml.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (namesXml.getEventType() == XmlPullParser.START_TAG && namesXml.getName().equals("name")) {
                    names.put(Integer.parseInt(namesXml.getAttributeValue(0)), namesXml.getAttributeValue(1));
                }
                namesXml.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setTextures() {
        Point size = new Point();
        display.getSize(size);
        Bitmap a;
        int width = size.x, height = size.y;
        double xy = width * 1.0 / height;
        if (xy >= 0.4 && xy <= 0.6) {
            menuWidth = width / 4;
            avatarWidth = width / 3;
            mapTitleWidth = width * 1000 / 5000;
        } else {
            mapTitleWidth = height * 1000 / 9000;
            menuWidth = width / 4;
            avatarWidth = width / 4;
        }
        statusImagesWidth = width / 10;
        categoryImageWidth = width / 4;
        int n = 10, m = 20;
        a = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, R.drawable.textures),
                mapTitleWidth * m, mapTitleWidth * n, false);
        textures = new Bitmap[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                textures[i][j] = Bitmap.createBitmap(a,
                        j * mapTitleWidth,
                        i * mapTitleWidth,
                        mapTitleWidth,
                        mapTitleWidth);
            }
        }
        mapTextures.addAll(Arrays.asList(textures[1]));
        int id=512;
        for (int i = 0; i < menu.length; i++)
            menu[i] = Bitmap.createScaledBitmap(textures[0][i], menuWidth, menuWidth, false);
        for (int i = 0; i < map.size(); i++)
            for (int j = 0; j < map.get(i).length; j++)
                for (int k = 0; k < map.get(i).width; k++)
                    if (map.get(i).map[j][k].getId() != id+255)
                        map.get(i).map[j][k].setTexture(mapTextures.get(map.get(i).map[j][k].getId()-512));
                    else  {
                        map.get(i).map[j][k].setTexture(Bitmap.createBitmap(mapTitleWidth, mapTitleWidth, Bitmap.Config.ARGB_8888));
                        map.get(i).map[j][k].getTexture().eraseColor(Color.BLACK);
                    }
        player.setAvatar(Bitmap.createBitmap(textures[5][5]));
    }

    private static void setResearches() {
        researches = new ArrayList<>();
        if (researchesJson != null&&!researchesJson.isEmpty())
            for (int i = 0; i < researchesJson.size(); i++)
                researches.add(new Gson().fromJson(researchesJson.get(i), Research.class));
        else {
            researchesJson = new ArrayList<>();
            ArrayList<Research> rqr = new ArrayList<>();
            researches.add(new Research(new ArrayList<>(),
                    "Basic spell creation", 1, 0, 0, false, true));
            rqr.add(researches.get(0));
            researches.add(new Research(rqr,
                    "Fire mage", 6, 1, 1, false, false));
            researches.add(new Research(rqr,
                    "Water mage", 2, 1, 2, false, false));
            researches.add(new Research(rqr,
                    "Earth mage", 4, 1, 3, false, false));
            researches.add(new Research(rqr,
                    "Air mage", 3, 1, 4, false, false));
            for (int i = 0; i < researches.size(); i++)
                researchesJson.add(new Gson().toJson(researches.get(i)));
        }

    }

    private static void setItems(XmlPullParser itemsXml, XmlPullParser recipesXml) {
        try {
            while (itemsXml.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (itemsXml.getEventType() == XmlPullParser.START_TAG){
                    switch (itemsXml.getName()){
                        case "item":{
                            int id=Integer.parseInt(itemsXml.getAttributeValue(3));
                            String name=names.get(id);
                            int costSell= Integer.parseInt(itemsXml.getAttributeValue(2)),
                                    costBuy= Integer.parseInt(itemsXml.getAttributeValue(1)),
                                    rarity= Integer.parseInt(itemsXml.getAttributeValue(4)),
                                    category= Integer.parseInt(itemsXml.getAttributeValue(0));
                            ids.put(id, items.size());
                            items.add(new Item(name, costSell, costBuy, rarity, category));
                            break;
                        }
                        case "armor":{
                            int id=Integer.parseInt(itemsXml.getAttributeValue(4));
                            String name=names.get(id);
                            int costSell= Integer.parseInt(itemsXml.getAttributeValue(3)),
                                    costBuy= Integer.parseInt(itemsXml.getAttributeValue(2)),
                                    rarity= Integer.parseInt(itemsXml.getAttributeValue(5)),
                                    category= Integer.parseInt(itemsXml.getAttributeValue(1)),
                                    armor= Integer.parseInt(itemsXml.getAttributeValue(0)),
                                    weight= Integer.parseInt(itemsXml.getAttributeValue(7)),
                                    typeOfArmor= Integer.parseInt(itemsXml.getAttributeValue(6));
                            ids.put(id, items.size());
                            items.add(new Armor(name, costSell, costBuy, armor, weight, typeOfArmor, category, rarity));
                            Log.d("HHH", ids.get(id)+"");
                        }
                    }
                }
                itemsXml.next();
            }
            while (recipesXml.getEventType()!=XmlPullParser.END_DOCUMENT){
                if(recipesXml.getEventType()==XmlPullParser.START_TAG
                        &&recipesXml.getName().equals("recipe")){
                    int productId=Integer.parseInt(recipesXml.getAttributeValue(0)),
                            productsNumber=Integer.parseInt(recipesXml.getAttributeValue(1));
                    ArrayList<Pair<Item, Integer>> ingredients=new ArrayList<>();
                    recipesXml.next();
                    while (!recipesXml.getName().equals("recipe")) {
                        if (recipesXml.getEventType() == XmlPullParser.START_TAG
                                && recipesXml.getName().equals("ingredient")) {
                            int ingredientId = Integer.parseInt(recipesXml.getAttributeValue(0)),
                                    ingredientsNumber = Integer.parseInt(recipesXml.getAttributeValue(1));
                            ingredients.add(new Pair<>(items.get(ids.get(ingredientId)), ingredientsNumber));
                        }
                        recipesXml.next();
                    }
                    recipes.add(new Recipe(items.get(ids.get(productId)), ingredients));
                    continue;
                }
                recipesXml.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        shopList.addAll(items);
    }

    private static void setDrop() {
        /*drop.put(0, new ArrayList<>());
        drop.get(0).add(new Pair<>(items.get(0), 70));
        drop.get(0).add(new Pair<>(items.get(1), 30));
        drop.put(1, new ArrayList<>());
        drop.get(1).add(new Pair<>(items.get(2), 90));
        drop.get(1).add(new Pair<>(items.get(3), 10));
        drop.put(2, new ArrayList<>());
        drop.get(2).add(new Pair<>(items.get(4), 100));
        drop.put(3, new ArrayList<>());
        drop.get(3).add(new Pair<>(items.get(5), 100));
        drop.put(4, new ArrayList<>());
        drop.get(4).add(new Pair<>(items.get(6), 100));
        drop.put(5, new ArrayList<>());
        drop.get(5).add(new Pair<>(items.get(8), 50));*/
    }

    private static void setEnemies(XmlPullParser enemiesXml, XmlPullParser locationXml) {
        try{
            while (enemiesXml.getEventType()!=XmlPullParser.END_DOCUMENT){
                if (enemiesXml.getEventType()==XmlPullParser.START_TAG
                        && enemiesXml.getName().equals("enemy")){
                    int armor=Integer.parseInt(enemiesXml.getAttributeValue(0)),
                            health=Integer.parseInt(enemiesXml.getAttributeValue(4)),
                            damage=Integer.parseInt(enemiesXml.getAttributeValue(1)),
                            mana=Integer.parseInt(enemiesXml.getAttributeValue(6)),
                            givenGold=Integer.parseInt(enemiesXml.getAttributeValue(3)),
                            givenExp=Integer.parseInt(enemiesXml.getAttributeValue(2)),
                            texture=Integer.parseInt(enemiesXml.getAttributeValue(7)),
                            id=Integer.parseInt(enemiesXml.getAttributeValue(5));
                    String name=names.get(id);
                    ArrayList<Triplex<Item, Integer, Integer>> drop=new ArrayList<>();
                    enemies.put(id, new Enemy(name, health, mana, damage, armor, givenGold, givenExp, drop, textures[5][texture]));
                    enemiesXml.next();
                    while (!enemiesXml.getName().equals("enemy")){
                        if (enemiesXml.getEventType()==XmlPullParser.START_TAG
                                && enemiesXml.getName().equals("drop")){
                            enemiesXml.next();
                            while (!enemiesXml.getName().equals("drop")){
                                if (enemiesXml.getEventType()==XmlPullParser.START_TAG
                                        && enemiesXml.getName().equals("item")){
                                    int number=Integer.parseInt(enemiesXml.getAttributeValue(2)),
                                            chance=Integer.parseInt(enemiesXml.getAttributeValue(0)),
                                            itemId=Integer.parseInt(enemiesXml.getAttributeValue(1));
                                    drop.add(new Triplex<>(items.get(ids.get(itemId)), chance, number));
                                }
                                enemiesXml.next();
                            }
                        }
                        enemiesXml.next();
                    }
                }
                enemiesXml.next();
            }
            while(locationXml.getEventType()!=XmlPullParser.END_DOCUMENT){
                if (locationXml.getEventType()==XmlPullParser.START_TAG
                        && locationXml.getName().equals("location")){
                    int chanceOfFight=Integer.parseInt(locationXml.getAttributeValue(0)),
                            id=Integer.parseInt(locationXml.getAttributeValue(1));
                    chancesOfFight.put(id, chanceOfFight);
                    chancesOfEnemy.put(id, new HashMap<>());
                    locationXml.next();
                    while (!locationXml.getName().equals("location")){
                        if (locationXml.getEventType()==XmlPullParser.START_TAG
                                && locationXml.getName().equals("enemy")){
                            int chance=Integer.parseInt(locationXml.getAttributeValue(0)),
                                    enemyId=Integer.parseInt(locationXml.getAttributeValue(1));
                            chancesOfEnemy.get(id).put(chance, enemies.get(enemyId));
                            Log.d("EN", chancesOfEnemy.toString());
                        }
                        locationXml.next();
                    }
                }
                locationXml.next();
            }
        }catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setMagic() {
        elements.clear();
        types.clear();
        forms.clear();
        manaReservoirs.clear();
        manaChannels.clear();
        if (elementsJson != null&&!elementsJson.isEmpty())
            for (int i = 0; i < elementsJson.size(); i++)
                elements.add(new Gson().fromJson(elementsJson.get(i), Element.class));
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
            elementsJson = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++)
                elementsJson.add(new Gson().toJson(elements.get(i)));
        }

        if (typesJson != null&&!typesJson.isEmpty())
            for (int i = 0; i < typesJson.size(); i++)
                types.add(new Gson().fromJson(typesJson.get(i), Type.class));
        else {
            types.add(new Type("On enemy", 0, true));
            typesJson = new ArrayList<>();
            for (int i = 0; i < types.size(); i++)
                typesJson.add(new Gson().toJson(types.get(i)));
        }

        if (formsJson != null&&!formsJson.isEmpty())
            for (int i = 0; i < formsJson.size(); i++)
                forms.add(new Gson().fromJson(formsJson.get(i), Form.class));
        else {
            forms.add(new Form("Sphere", 0, true));
            formsJson = new ArrayList<>();
            for (int i = 0; i < forms.size(); i++)
                formsJson.add(new Gson().toJson(forms.get(i)));
        }

        if (manaReservoirsJson != null&&!manaReservoirsJson.isEmpty())
            for (int i = 0; i < manaReservoirsJson.size(); i++)
                manaReservoirs.add(new Gson().fromJson(manaReservoirsJson.get(i), ManaReservoir.class));
        else {
            manaReservoirs.add(new ManaReservoir("Basic", 1, true));
            manaReservoirs.add(new ManaReservoir("Big", 10, true));
            manaReservoirsJson = new ArrayList<>();
            for (int i = 0; i < manaReservoirs.size(); i++)
                manaReservoirsJson.add(new Gson().toJson(manaReservoirs.get(i)));
        }

        if (manaChannelsJson != null&&!manaChannelsJson.isEmpty())
            for (int i = 0; i < manaChannelsJson.size(); i++)
                manaChannels.add(new Gson().fromJson(manaChannelsJson.get(i), ManaChannel.class));
        else {
            manaChannels.add(new ManaChannel("Basic", 2, true));
            manaChannelsJson = new ArrayList<>();
            for (int i = 0; i < manaChannels.size(); i++)
                manaChannelsJson.add(new Gson().toJson(manaChannels.get(i)));
        }
    }

    protected static void setInitialData(XmlPullParser items, XmlPullParser names,
                                         XmlPullParser recipes, XmlPullParser enemies,
                                         XmlPullParser locations) {
        researchesJson = new Gson().fromJson(sh.getString("Researches", new Gson().toJson(new ArrayList<String>())), ArrayList.class);
        elementsJson = new Gson().fromJson(sh.getString("Elements", new Gson().toJson(new ArrayList<String>())), ArrayList.class);
        typesJson = new Gson().fromJson(sh.getString("Types", new Gson().toJson(new ArrayList<String>())), ArrayList.class);
        formsJson = new Gson().fromJson(sh.getString("Forms", new Gson().toJson(new ArrayList<String>())), ArrayList.class);
        manaChannelsJson = new Gson().fromJson(sh.getString("Mana channels", new Gson().toJson(new ArrayList<String>())), ArrayList.class);
        manaReservoirsJson = new Gson().fromJson(sh.getString("Mana reservoirs", new Gson().toJson(new ArrayList<String>())), ArrayList.class);
        categories.put(0, "Armor/weapon");
        categories.put(1, "Food/potions");
        categories.put(2, "Resources");
        categories.put(3, "Other");
        setNames(names);
        setItems(items, recipes);
        setTextures();
        setResearches();
        setDrop();
        setEnemies(enemies, locations);
        setMagic();
        setTasks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        m.start(this, R.raw.main);
        sh = getPreferences(MODE_PRIVATE);
        player = new Gson().fromJson(sh.getString("Player", new Gson().toJson(new Player(2, 2))), Player.class);
        player.setUser(new Gson().fromJson(sh.getString("User", new Gson().toJson(new User("", ""))), User.class));
        showTutorial = sh.getBoolean("Tutorial", true);
        researchesJson = new ArrayList<String>(new Gson().fromJson(sh.getString("Researches", new Gson().toJson(new ArrayList<String>())), ArrayList.class));
        elementsJson = new ArrayList<>(new Gson().fromJson(sh.getString("Elements", new Gson().toJson(new ArrayList<String>())), ArrayList.class));
        typesJson = new ArrayList<>(new Gson().fromJson(sh.getString("Types", new Gson().toJson(new ArrayList<String>())), ArrayList.class));
        formsJson = new ArrayList<>(new Gson().fromJson(sh.getString("Forms", new Gson().toJson(new ArrayList<String>())), ArrayList.class));
        manaChannelsJson = new ArrayList<>(new Gson().fromJson(sh.getString("Mana channels", new Gson().toJson(new ArrayList<String>())), ArrayList.class));
        manaReservoirsJson = new ArrayList<>(new Gson().fromJson(sh.getString("Mana reservoirs", new Gson().toJson(new ArrayList<String>())), ArrayList.class));
    }

    @Override
    protected void onStop() {
        m.stop();
        sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.putString("User", new Gson().toJson(player.getUser()));
        ed.putBoolean("Tutorial", showTutorial);
        ed.putString("Researches", new Gson().toJson(researchesJson));
        ed.putString("Elements", new Gson().toJson(elementsJson));
        ed.putString("Types", new Gson().toJson(typesJson));
        ed.putString("Forms", new Gson().toJson(formsJson));
        ed.putString("Mana channels", new Gson().toJson(manaChannelsJson));
        ed.putString("Mana reservoirs", new Gson().toJson(manaReservoirsJson));
        ed.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        m.stop();
        sh = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.clear();
        ed.putString("Player", new Gson().toJson(player));
        ed.putString("User", new Gson().toJson(player.getUser()));
        ed.putBoolean("Tutorial", showTutorial);
        ed.putString("Researches", new Gson().toJson(researchesJson));
        ed.putString("Elements", new Gson().toJson(elementsJson));
        ed.putString("Types", new Gson().toJson(typesJson));
        ed.putString("Forms", new Gson().toJson(formsJson));
        ed.putString("Mana channels", new Gson().toJson(manaChannelsJson));
        ed.putString("Mana reservoirs", new Gson().toJson(manaReservoirsJson));
        ed.apply();
        super.onDestroy();
    }

    public static class Map {
        private final int length;
        private final int width;
        private final MapTile[][] map;

        public Map(XmlPullParser map_xml) {
            ArrayList<ArrayList<MapTile>> map = new ArrayList();
            int type;
            try {
                while (map_xml.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (map_xml.getEventType() == XmlPullParser.START_TAG && map_xml.getName().equals("row"))
                        map.add(new ArrayList<>());
                    if (map_xml.getEventType() == XmlPullParser.START_TAG && map_xml.getName().equals("map_title")) {
                        type = Integer.parseInt(map_xml.getAttributeValue(0));
                        MapTile tile =new MapTile(null, type);
                        map.get(map.size() - 1).add(tile);
                    }
                    map_xml.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            width = map.get(0).size();
            length = map.size();
            this.map = new MapTile[length][width];
            for (int i = 0; i < length; i++)
                for (int j = 0; j < width; j++)
                    this.map[i][j] = map.get(i).get(j);
        }

        public MapTile[][] getMap() {
            return map;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}