package com.example.finalproject

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finalproject.fragments.MapFragment
import com.example.finalproject.fragments.MenuFragment
import com.example.finalproject.fragments.StatusBarFragment
import com.example.finalproject.fragments.TutorialFragment
import com.example.finalproject.service.*
import com.example.finalproject.service.classes.*
import com.example.finalproject.service.classes.Map
import com.example.finalproject.service.classes.entities.Enemy
import com.example.finalproject.service.classes.entities.Player
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.spell.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.xmlpull.v1.XmlPullParser
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    /**
     * initializes base data required for app to function, calls graphic components
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goFullscreen()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var mapParser: XmlPullParser = resources.getXml(R.xml.start_map)
        map.add(Map(mapParser))
        mapParser = resources.getXml(R.xml.first_village_map)
        map.add(Map(mapParser))
        val bounds = getBounds()
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        saved = sharedPreference.getBoolean("saved", false)
        width = bounds.first
        height = bounds.second
        res = resources
        music = Music()
        music.start(this, R.raw.main)
        player = Player(2, 2)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "saves"
        ).build()
        setTextures()
        setInitialData(saved)
        while (!inited)
            continue
        if (!saved) {
            fragmentTransaction.add(R.id.tutorial, TutorialFragment())
            showTutorial = false
        } else {
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
        }
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        music.start(this, R.raw.main)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        saved = sharedPreference.getBoolean("saved", false)
    }

    override fun onStop() {
        super.onStop()
        music.stop()
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean("saved", saved)
        editor.apply()
    }

    @Entity
    class Saves(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "assets") val assetsSave: String,
        @ColumnInfo(name = "player") val playerSave: String,
        @ColumnInfo(name = "tutorial") val tutorialSave: Boolean
    )

    @Dao
    interface SavesDao {

        /**
         * @return saves from database
         */
        @Query("SELECT * FROM saves")
        fun getJsons(): List<Saves>

        /**
         * clears the save
         */
        @Query("DELETE FROM saves WHERE id=0")
        fun deleteSaves()

        /**
         * @param save inserts the save into the database
         */
        @Insert
        fun insertSave(save: Saves)
    }

    @Database(entities = [Saves::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): SavesDao
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
        val map = ArrayList<Map>()
        val menu = arrayOfNulls<Bitmap>(4)
        private val mapTextures = ArrayList<Bitmap>()
        private lateinit var avatar: Bitmap
        lateinit var textures: Array<Array<Bitmap>>
        lateinit var music: Music
        lateinit var assets: Assets
        var showTutorial = true
        var saved = false
        lateinit var db: AppDatabase
        private var inited = false

        @Serializable
        class Assets {
            val chancesOfFight =
                HashMap<Int, Int>() //<id of a location, chance of getting into a fight>
            val chancesOfEnemy =
                HashMap<Int, ArrayList<Pair<Int, Int>>>() //<id of a location, <chance of getting into a fight with that enemy, id of an enemy>
            val enemies = HashMap<Int, Enemy>() //<id of an enemy, object template>
            val elements = HashMap<Int, Element>()
            val manaChannels = HashMap<Int, ManaChannel>()
            val types = HashMap<Int, Type>()
            val forms = HashMap<Int, Form>()
            val manaReservoirs = HashMap<Int, ManaReservoir>()
            val researches = HashMap<Int, Research>() //<id of a research, object template>
            val researchEffects =
                HashMap<Int, ResearchEffect>() //<id of an effect, object template>
            val availableResearches = ArrayList<Int>() //<id of a research>
            val recipes = ArrayList<Recipe>()
            val items = HashMap<Int, Item>() //<id of an item, object template>
            val shopList = ArrayList<Item>()
            val tasks = HashMap<Int, Task>() //<id of a task, object template>
            val activeTasks = ArrayList<Int>()
            val itemsObtained: HashMap<Int, Int> = HashMap() //<id of an item, quantity>
            val enemiesKilled: HashMap<Int, Int> = HashMap() //<id of an enemy, quantity>
        }

        /**
         * @return player's avatar
         */
        fun getAvatar(): Bitmap = Bitmap.createBitmap(avatar)

        /**
         * parses textures and initializes map
         */
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

        /**
         * @param saved true if the player has already made saves, false otherwise
         * initializes assets either from Room or from xml file
         */
        fun setInitialData(saved: Boolean = false) {
            if (saved) thread {
                val dao = db.userDao()
                val saves = dao.getJsons()
                assets =
                    Json.decodeFromString(Assets.serializer(), saves[saves.size - 1].assetsSave)
                player =
                    Json.decodeFromString(Player.serializer(), saves[saves.size - 1].playerSave)
                showTutorial = saves[saves.size - 1].tutorialSave
                inited = true
            } else {
                var data = ""
                val parser = res.getXml(R.xml.initial_data)
                while (parser.eventType != XmlPullParser.END_TAG) {
                    if (parser.eventType == XmlPullParser.START_TAG && parser.name == "data") {
                        parser.next()
                        data = parser.text
                    }
                    parser.next()
                }
                assets = Json.decodeFromString(Assets.serializer(), data)
                assets.items[0] = Item("Wolf pelt", 0, 10, 15, 0, 2)
                assets.items[1] = Item("Wolf tooth", 1, 10, 12, 0, 3)
                assets.items[12] = Item("Leather", 12, 17, 20, 0, 2)
                assets.elements[1024] = Element(
                    "Pure mana",
                    1024,
                    false,
                    1,
                    5.0
                )
                assets.elements[1026] = Element(
                    "Fire",
                    1026,
                    false,
                    3,
                    10.0
                )
                assets.forms[1031] = Form(
                    "Sphere",
                    1031,
                    false,
                    0
                )
                assets.manaChannels[1032] = ManaChannel(
                    "Basic channel",
                    1032,
                    false,
                    1.0
                )
                assets.manaReservoirs[1033] = ManaReservoir(
                    "Basic reservoir",
                    1034,
                    false,
                    4.0
                )
                assets.types[1034] = Type(
                    "On enemy",
                    1034,
                    false,
                    0
                )
                assets.researchEffects[1536] = ResearchEffect(
                    "Unlock spell creation",
                    1536,
                    affectedResearches = arrayListOf(1281),
                    unlockedComponents = arrayListOf(1024, 1031, 1032, 1033, 1034)
                )
                assets.researchEffects[1537] = ResearchEffect(
                    "Unlock fire element",
                    1537,
                    unlockedComponents = arrayListOf(1026)
                )
                assets.researchEffects[1538] = ResearchEffect(
                    "Spell usage",
                    1538,
                    affectedResearches = arrayListOf(1280)
                )
                assets.researchEffects[1540] = ResearchEffect(
                    "5% physical resistance upgrade",
                    1540,
                    affectedResearches = arrayListOf(1285, 1286),
                    upgradedResistances = arrayListOf(Pair(0, 0.05))
                )
                assets.recipes.add(
                    Recipe(
                        Pair(5, assets.items[12]!!),
                        arrayListOf(Pair(3, assets.items[0]!!))
                    )
                )
                assets.researches[1280] = Research(
                    "Spell creation",
                    1280,
                    15,
                    0,
                    1536,
                    description = "Allows you to create custom spells"
                )
                assets.researches[1281] = Research(
                    "Fire element",
                    1281,
                    2,
                    2,
                    1537,
                    requiredResearches = arrayListOf(1280),
                    description = "Allows you to use a new element in your spells"
                )
                assets.researches[1282] = Research(
                    "Spell usage",
                    1282,
                    2,
                    0,
                    available = true,
                    effect = 1538,
                    description = "Allows you to use magic"
                )
                assets.researches[1284] = Research(
                    "Hard endurance training 1",
                    1284,
                    2,
                    0,
                    available = true,
                    effect = 1540,
                    description = "You train a lot to become more endurant"
                )
                assets.researches[1285] = Research(
                    "Hard endurance training 2",
                    1285,
                    4,
                    1,
                    requiredResearches = arrayListOf(1284),
                    effect = 1540,
                    description = "You train a lot to become more even endurant"
                )
                assets.researches[1286] = Research(
                    "Hard endurance training 3",
                    1286,
                    8,
                    2,
                    requiredResearches = arrayListOf(1285),
                    effect = 1540,
                    description = "You train a lot to reach the peak of a human body"
                )
                assets.availableResearches.add(1282)
                assets.availableResearches.add(1284)
                assets.shopList.addAll(assets.items.values)
                assets.tasks[1] = Task(
                    "Wolf killer",
                    1,
                    "Kill five wolfs",
                    enemiesToKill = arrayListOf(Pair(256, 5)),
                    goldGiven = 70,
                    experienceGiven = 20
                )
                assets.tasks[0] = Task(
                    "Your first levels",
                    0,
                    "Reach level 3",
                    levelToReach = 3,
                    goldGiven = 15,
                    experienceGiven = 14
                )
                inited = true
                Log.d("Assets", Json.encodeToString(Assets.serializer(), assets))
            }
        }
    }

    /**
     * regulates app's window size
     */
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

    /**
     * @return width and height of a device's screen
     * calculates returned value to scale textures
     */
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