package com.example.finalproject.fragments

import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.example.finalproject.entities.Enemy
import com.example.finalproject.entities.Player
import com.example.finalproject.service.classes.items.Armor
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.*
import com.example.finalproject.service.classes.*
import com.example.finalproject.service.classes.Map
import com.example.finalproject.service.classes.spell.*
import com.google.gson.Gson
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.indices
import kotlin.collections.set
import kotlin.math.cos


//TODO
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goFullscreen()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var mapParser: XmlPullParser = resources.getXml(R.xml.start_map)
        val itemsParser: XmlPullParser = resources.getXml(R.xml.items)
        val namesParser: XmlPullParser = resources.getXml(R.xml.names)
        val recipesParser: XmlPullParser = resources.getXml(R.xml.recipes)
        val enemiesParser: XmlPullParser = resources.getXml(R.xml.enemies)
        val locationsParser: XmlPullParser = resources.getXml(R.xml.locations)
        map.add(Map(mapParser))
        mapParser = resources.getXml(R.xml.first_village_map)
        map.add(Map(mapParser))
        val bounds = getBounds()
        width = bounds.first
        height = bounds.second
        sh = getPreferences(MODE_PRIVATE)
        res = resources
        music = Music()
        music.start(this, R.raw.main)
        showTutorial = sh.getBoolean("Tutorial", true)
        player = Gson().fromJson(
            sh.getString("Player", Gson().toJson(Player(2, 2))),
            Player::class.java
        )
        setInitialData(itemsParser, namesParser, recipesParser, enemiesParser, locationsParser)
        if (showTutorial) {
            fragmentTransaction.add(R.id.tutorial, TutorialFragment())
        } else {
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNum))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
        }
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        music.start(this, R.raw.main)
        sh = getPreferences(MODE_PRIVATE)
        player = Gson().fromJson(
            sh.getString("Player", Gson().toJson(Player(2, 2))),
            Player::class.java
        )
        player.user = Gson().fromJson(
            sh.getString("User", Gson().toJson(User())),
            User::class.java
        )
        showTutorial = sh.getBoolean("Tutorial", true)
        researchesJson = ArrayList(
            Gson().fromJson(
                sh.getString("Researches", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            )
        ) as ArrayList<String>
        elementsJson = ArrayList(
            Gson().fromJson(
                sh.getString("Elements", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            )
        ) as ArrayList<String>
        typesJson = ArrayList(
            Gson().fromJson(
                sh.getString("Types", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            )
        ) as ArrayList<String>
        formsJson = ArrayList(
            Gson().fromJson(
                sh.getString("Forms", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            )
        ) as ArrayList<String>
        manaChannelsJson = ArrayList(
            Gson().fromJson(
                sh.getString("Mana channels", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            )
        ) as ArrayList<String>
        manaReservoirsJson = ArrayList(
            Gson().fromJson(
                sh.getString("Mana reservoirs", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            )
        ) as ArrayList<String>
    }

    override fun onStop() {
        music.stop()
        sh = getPreferences(MODE_PRIVATE)
        val ed = sh.edit()
        ed.clear()
        ed.putString("Player", Gson().toJson(player))
        ed.putString("User", Gson().toJson(player.user))
        ed.putBoolean("Tutorial", showTutorial)
        ed.putString("Researches", Gson().toJson(researchesJson))
        ed.putString("Elements", Gson().toJson(elementsJson))
        ed.putString("Types", Gson().toJson(typesJson))
        ed.putString("Forms", Gson().toJson(formsJson))
        ed.putString("Mana channels", Gson().toJson(manaChannelsJson))
        ed.putString("Mana reservoirs", Gson().toJson(manaReservoirsJson))
        ed.apply()
        super.onStop()
    }

    override fun onDestroy() {
        music.stop()
        sh = getPreferences(MODE_PRIVATE)
        val ed = sh.edit()
        ed.clear()
        ed.putString("Player", Gson().toJson(player))
        ed.putString("User", Gson().toJson(player.user))
        ed.putBoolean("Tutorial", showTutorial)
        ed.putString("Researches", Gson().toJson(researchesJson))
        ed.putString("Elements", Gson().toJson(elementsJson))
        ed.putString("Types", Gson().toJson(typesJson))
        ed.putString("Forms", Gson().toJson(formsJson))
        ed.putString("Mana channels", Gson().toJson(manaChannelsJson))
        ed.putString("Mana reservoirs", Gson().toJson(manaReservoirsJson))
        ed.apply()
        super.onDestroy()
    }

    companion object {
        lateinit var player: Player
        private lateinit var res: Resources
        private var width: Int = 0
        private var height: Int = 0
        private var menuWidth = 0
        private var mapTitleWidth = 0
        var avatarWidth = 0
        var statusImagesWidth = 0
        var categoryImageWidth = 0
        var showTutorial = true
        val chancesOfFight = HashMap<Int, Int>()
        val map = ArrayList<Map>()
        val menu = arrayOfNulls<Bitmap>(4)
        val chancesOfEnemy = HashMap<Int, HashMap<Int, Enemy>>()
        private val enemies = HashMap<Int, Enemy>()
        val drop = HashMap<Int, ArrayList<Pair<Item, Int>>>()
        val elements = ArrayList<Element>()
        val manaChannels = ArrayList<ManaChannel>()
        val types = ArrayList<Type>()
        val forms = ArrayList<Form>()
        val manaReservoirs = ArrayList<ManaReservoir>()
        val researches = ArrayList<Research>()
        val recipes = ArrayList<Recipe>()
        private val items = ArrayList<Item>()
        val shopList = ArrayList<Item>()
        val tasks = ArrayList<Task>()
        var researchesJson: ArrayList<String> = ArrayList()
        var elementsJson: ArrayList<String> = ArrayList()
        var manaChannelsJson: ArrayList<String> = ArrayList()
        var typesJson: ArrayList<String> = ArrayList()
        var formsJson: ArrayList<String> = ArrayList()
        var manaReservoirsJson: ArrayList<String> = ArrayList()
        private val mapTextures = ArrayList<Bitmap>()
        private val categories = HashMap<Int, String>()
        private val ids = HashMap<Int, Int>()
        private val names = HashMap<Int, String>()
        private lateinit var avatar: Bitmap
        lateinit var textures: Array<Array<Bitmap>>
        lateinit var music: Music
        private lateinit var sh: SharedPreferences

        fun getAvatar(): Bitmap = Bitmap.createBitmap(avatar)

        private fun setTasks() {
            tasks.add(Task("Earn 100 gold to get 50 exp and 50 gold", "First money"))
            tasks.add(Task("Achieve level 5 to get 500 gold and 100 exp", "Getting power"))
        }

        private fun setNames(namesXml: XmlPullParser) {
            try {
                while (namesXml.eventType != XmlPullParser.END_DOCUMENT) {
                    if (namesXml.eventType == XmlPullParser.START_TAG && namesXml.name == "name") {
                        names[namesXml.getAttributeValue(0).toInt()] =
                            namesXml.getAttributeValue(1)
                    }
                    namesXml.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun setTextures() {
            val texturesSource: Bitmap
            val xy = width * 1.0 / height
            if (xy in 0.4..0.6) {
                menuWidth = width / 4
                avatarWidth = width / 3
                mapTitleWidth = width * 1000 / 5000
            } else {
                mapTitleWidth = height * 1000 / 9000
                menuWidth = width / 4
                avatarWidth = width / 4
            }
            statusImagesWidth = width / 10
            categoryImageWidth = width / 4
            val n = 10
            val m = 20
            texturesSource = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, R.drawable.textures),
                mapTitleWidth * m, mapTitleWidth * n, false
            )
            textures = Array(n) {
                Array(m) {
                    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                }
            }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    textures[i][j] = Bitmap.createBitmap(
                        texturesSource,
                        j * mapTitleWidth,
                        i * mapTitleWidth,
                        mapTitleWidth,
                        mapTitleWidth
                    )
                }
            }
            mapTextures.addAll(textures[1].asList())
            val id = 512
            for (i in menu.indices)
                menu[i] = Bitmap.createScaledBitmap(
                    textures[0][i], menuWidth, menuWidth, false
                )
            for (i in map.indices)
                for (j in 0 until map[i].length)
                    for (k in 0 until map[i].width)
                        if (map[i].map[j][k].id != id + 255)
                            map[i].map[j][k].setTexture(
                                mapTextures[map[i].map[j][k].id - 512]
                            )
                        else {
                            map[i].map[j][k].setTexture(
                                Bitmap.createBitmap(
                                    mapTitleWidth,
                                    mapTitleWidth,
                                    Bitmap.Config.ARGB_8888
                                )
                            )
                            map[i].map[j][k].getTexture().eraseColor(Color.BLACK)
                        }
            avatar = Bitmap.createScaledBitmap(textures[5][5], mapTitleWidth, mapTitleWidth, false)
        }

        private fun setResearches() {
            if (researchesJson.isNotEmpty())
                for (i in researchesJson.indices)
                    researches.add(
                        Gson().fromJson(
                            researchesJson[i],
                            Research::class.java
                        )
                    )
            else {
                val requiredResearches = ArrayList<Research>()
                researches.add(
                    Research(
                        ArrayList(),
                        "Basic spell creation", 1, 0, 0, false, true
                    )
                )
                requiredResearches.add(researches[0])
                researches.add(
                    Research(
                        requiredResearches,
                        "Fire mage", 6, 1, 1, false, false
                    )
                )
                researches.add(
                    Research(
                        requiredResearches,
                        "Water mage", 2, 1, 2, false, false
                    )
                )
                researches.add(
                    Research(
                        requiredResearches,
                        "Earth mage", 4, 1, 3, false, false
                    )
                )
                researches.add(
                    Research(
                        requiredResearches,
                        "Air mage", 3, 1, 4, false, false
                    )
                )
                for (i in researches.indices)
                    researchesJson.add(
                        Gson().toJson(
                            researches[i]
                        )
                    )
            }
        }

        private fun setItems(itemsXml: XmlPullParser, recipesXml: XmlPullParser) {
            try {
                while (itemsXml.eventType != XmlPullParser.END_DOCUMENT) {
                    if (itemsXml.eventType == XmlPullParser.START_TAG) {
                        when (itemsXml.name) {
                            "item" -> {
                                val id = itemsXml.getAttributeValue(3).toInt()
                                val name = names[id]!!
                                val costSell = itemsXml.getAttributeValue(2).toInt()
                                val costBuy = itemsXml.getAttributeValue(1).toInt()
                                val rarity = itemsXml.getAttributeValue(4).toInt()
                                val category = itemsXml.getAttributeValue(0).toInt()
                                ids[id] = items.size
                                items.add(
                                    Item(
                                        name, id, costSell, costBuy, rarity, category
                                    )
                                )
                            }
                            "armor" -> {
                                val id = itemsXml.getAttributeValue(4).toInt()
                                val name = names[id]!!
                                val costSell = itemsXml.getAttributeValue(3).toInt()
                                val costBuy = itemsXml.getAttributeValue(2).toInt()
                                val rarity = itemsXml.getAttributeValue(5).toInt()
                                val category = itemsXml.getAttributeValue(1).toInt()
                                val armor = itemsXml.getAttributeValue(0).toDouble()
                                val weight = itemsXml.getAttributeValue(7).toDouble()
                                val typeOfArmor = itemsXml.getAttributeValue(6).toInt()
                                ids[id] = items.size
                                items.add(
                                    Armor(
                                        name,
                                        id,
                                        costSell,
                                        costBuy,
                                        rarity,
                                        category,
                                        armor,
                                        weight,
                                        typeOfArmor
                                    )
                                )
                            }
                        }
                    }
                    itemsXml.next()
                }
                while (recipesXml.eventType != XmlPullParser.END_DOCUMENT) {
                    if (recipesXml.eventType == XmlPullParser.START_TAG
                        && recipesXml.name == "recipe"
                    ) {
                        val productId = recipesXml.getAttributeValue(0).toInt()
                        val productsNumber = recipesXml.getAttributeValue(1).toInt()
                        val ingredients = ArrayList<Pair<Int, Item>>()
                        recipesXml.next()
                        while (recipesXml.name != "recipe") {
                            if (recipesXml.eventType == XmlPullParser.START_TAG
                                && recipesXml.name == "ingredient"
                            ) {
                                val ingredientId = recipesXml.getAttributeValue(0).toInt()
                                val ingredientsNumber = recipesXml.getAttributeValue(1).toInt()
                                ingredients.add(
                                    Pair(
                                        ingredientsNumber,
                                        items[ids[ingredientId]!!]
                                    )
                                )
                            }
                            recipesXml.next()
                        }
                        recipes.add(
                            Recipe(
                                Pair(productsNumber, items[ids[productId]!!]), ingredients
                            )
                        )
                        continue
                    }
                    recipesXml.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            shopList.addAll(items)
        }

        private fun setDrop() {
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

        private fun setEnemies(enemiesXml: XmlPullParser, locationXml: XmlPullParser) {
            try {
                while (enemiesXml.eventType != XmlPullParser.END_DOCUMENT) {
                    if (enemiesXml.eventType == XmlPullParser.START_TAG
                        && enemiesXml.name == "enemy"
                    ) {
                        val armor = enemiesXml.getAttributeValue(0).toDouble()
                        val health = enemiesXml.getAttributeValue(4).toDouble()
                        val healthRegen = 0.0
                        val damage = enemiesXml.getAttributeValue(1).toDouble()
                        val mana = enemiesXml.getAttributeValue(6).toDouble()
                        val givenGold = enemiesXml.getAttributeValue(3).toInt()
                        val givenExp = enemiesXml.getAttributeValue(2).toInt()
                        val texture = enemiesXml.getAttributeValue(7).toInt()
                        val id = enemiesXml.getAttributeValue(5).toInt()
                        val name = names[id]!!
                        val drop = ArrayList<Triple<Item, Int, Int>>()
                        enemies[id] =
                            Enemy(
                                name,
                                id,
                                health,
                                health,
                                1.0,
                                mana,
                                mana,
                                1.0,
                                ArrayList(),
                                Loot(),
                                Damage()
                            )
                        enemiesXml.next()
                        while (enemiesXml.name != "enemy") {
                            if (enemiesXml.eventType == XmlPullParser.START_TAG
                                && enemiesXml.name == "drop"
                            ) {
                                enemiesXml.next()
                                while (enemiesXml.name != "drop") {
                                    if (enemiesXml.eventType == XmlPullParser.START_TAG
                                        && enemiesXml.name == "item"
                                    ) {
                                        val number = enemiesXml.getAttributeValue(2).toInt()
                                        val chance = enemiesXml.getAttributeValue(0).toInt()
                                        val itemId = enemiesXml.getAttributeValue(1).toInt()
                                        drop.add(
                                            Triple(
                                                items[ids[itemId]!!], chance, number
                                            )
                                        )
                                    }
                                    enemiesXml.next()
                                }
                            }
                            enemiesXml.next()
                        }
                    }
                    enemiesXml.next()
                }
                while (locationXml.eventType != XmlPullParser.END_DOCUMENT) {
                    if (locationXml.eventType == XmlPullParser.START_TAG
                        && locationXml.name == "location"
                    ) {
                        val chanceOfFight = locationXml.getAttributeValue(0).toInt()
                        val id = locationXml.getAttributeValue(1).toInt()
                        chancesOfFight[id] =
                            chanceOfFight
                        chancesOfEnemy[id] =
                            HashMap()
                        locationXml.next()
                        while (locationXml.name != "location") {
                            if (locationXml.eventType == XmlPullParser.START_TAG
                                && locationXml.name == "enemy"
                            ) {
                                val chance = locationXml.getAttributeValue(0).toInt()
                                val enemyId = locationXml.getAttributeValue(1).toInt()
                                chancesOfEnemy[id]!![chance] = enemies[enemyId]!!
                            }
                            locationXml.next()
                        }
                    }
                    locationXml.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun setMagic() {
            elements.clear()
            types.clear()
            forms.clear()
            manaReservoirs.clear()
            manaChannels.clear()
            if (elementsJson.isNotEmpty())
                for (i in elementsJson.indices)
                    elements.add(
                        Gson().fromJson(
                            elementsJson[i],
                            Element::class.java
                        )
                    )
            else {
                elements.add(Element("Pure mana", 6, 2.0, false))
                elements.add(Element("Fire", 1, 10.0, false))
                elements.add(Element("Water", 0, 5.0, false))
                elements.add(Element("Air", 3, 4.0, false))
                elements.add(Element("Earth", 2, 8.0, false))
                elements.add(Element("Life", 5, -5.0, false))
                elements.add(Element("Death", 4, 5.0, false))
                elements.add(Element("Light", 6, 2.0, false))
                elements.add(Element("Darkness", 7, 7.0, false))
                elementsJson = ArrayList()
                for (i in elements.indices)
                    elementsJson.add(
                        Gson().toJson(
                            elements[i]
                        )
                    )
            }
            if (typesJson.isNotEmpty())
                for (i in typesJson.indices)
                    types.add(
                        Gson().fromJson(
                            typesJson[i],
                            Type::class.java
                        )
                    )
            else {
                types.add(Type(0, "On enemy", true))
                typesJson = ArrayList()
                for (i in types.indices)
                    typesJson.add(
                        Gson().toJson(
                            types[i]
                        )
                    )
            }
            if (formsJson.isNotEmpty())
                for (i in formsJson.indices)
                    forms.add(
                        Gson().fromJson(
                            formsJson[i],
                            Form::class.java
                        )
                    ) else {
                forms.add(Form(0, "Sphere", true))
                formsJson = ArrayList()
                for (i in forms.indices)
                    formsJson.add(
                        Gson().toJson(
                            forms[i]
                        )
                    )
            }
            if (manaReservoirsJson.isNotEmpty())
                for (i in manaReservoirsJson.indices)
                    manaReservoirs.add(
                        Gson().fromJson(
                            manaReservoirsJson[i],
                            ManaReservoir::class.java
                        )
                    )
            else {
                manaReservoirs.add(ManaReservoir(1.0, "Basic", true))
                manaReservoirs.add(ManaReservoir(2.0, "Big", true))
                manaReservoirsJson = ArrayList()
                for (i in manaReservoirs.indices)
                    manaReservoirsJson.add(
                        Gson().toJson(
                            manaReservoirs[i]
                        )
                    )
            }
            if (manaChannelsJson.isNotEmpty())
                for (i in manaChannelsJson.indices)
                    manaChannels.add(
                        Gson().fromJson(
                            manaChannelsJson[i],
                            ManaChannel::class.java
                        )
                    )
            else {
                manaChannels.add(ManaChannel(2.0, "Basic", true))
                manaChannelsJson = ArrayList()
                for (i in manaChannels.indices)
                    manaChannelsJson.add(
                        Gson().toJson(
                            manaChannels[i]
                        )
                    )
            }
        }

        fun setInitialData(
            items: XmlPullParser, names: XmlPullParser,
            recipes: XmlPullParser, enemies: XmlPullParser,
            locations: XmlPullParser
        ) {
            researchesJson = Gson().fromJson(
                sh.getString("Researches", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            ) as ArrayList<String>
            elementsJson = Gson().fromJson(
                sh.getString("Elements", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            ) as ArrayList<String>
            typesJson = Gson().fromJson(
                sh.getString("Types", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            ) as ArrayList<String>
            formsJson = Gson().fromJson(
                sh.getString("Forms", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            ) as ArrayList<String>
            manaChannelsJson = Gson().fromJson(
                sh.getString("Mana channels", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            ) as ArrayList<String>
            manaReservoirsJson = Gson().fromJson(
                sh.getString("Mana reservoirs", Gson().toJson(ArrayList<String>())),
                ArrayList::class.java
            ) as ArrayList<String>
            categories[0] = "Armor/weapon"
            categories[1] = "Food/potions"
            categories[2] = "Resources"
            categories[3] = "Other"
            setNames(names)
            setItems(items, recipes)
            setTextures()
            setResearches()
            setDrop()
            setEnemies(enemies, locations)
            setMagic()
            setTasks()
        }
    }

    @Suppress("DEPRECATION")
    private fun goFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun getBounds(): Pair<Int, Int> {
        val wm = windowManager
        val width: Int
        val height: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val windowInsets: WindowInsets = windowMetrics.windowInsets

            val insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
            )
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom

            val bounds = windowMetrics.bounds
            width = bounds.width() - insetsWidth
            height = bounds.height() - insetsHeight
        } else {
            val size = Point()
            val display = wm.defaultDisplay
            display?.getSize(size)
            width = size.x
            height = size.y
        }
        return Pair(width, height);
    }
}