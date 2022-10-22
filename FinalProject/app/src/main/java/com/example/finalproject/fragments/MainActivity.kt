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
import com.example.finalproject.service.classes.entities.Enemy
import com.example.finalproject.service.classes.entities.Player
import com.example.finalproject.service.*
import com.example.finalproject.service.classes.*
import com.example.finalproject.service.classes.Map
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.spell.*
import com.google.gson.Gson
import org.xmlpull.v1.XmlPullParser


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
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
        }
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        music.start(this, R.raw.main)
    }

    override fun onStop() {
        super.onStop()
        music.stop()
        sh = getPreferences(MODE_PRIVATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        music.stop()
        sh = getPreferences(MODE_PRIVATE)
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
        val drop = HashMap<Int, ArrayList<Loot>>()
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

        fun getAvatar(): Bitmap =Bitmap.createBitmap(avatar)

        private fun setTasks() {
            tasks.add(Task("Earn 100 gold to get 50 exp and 50 gold", "First money"))
            tasks.add(Task("Achieve level 5 to get 500 gold and 100 exp", "Getting power"))
        }

        private fun setNames(namesXml: XmlPullParser) {
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
        }

        private fun setItems(itemsXml: XmlPullParser, recipesXml: XmlPullParser) {
        }

        private fun setDrop() {
        }

        private fun setEnemies(enemiesXml: XmlPullParser, locationXml: XmlPullParser) {
        }

        private fun setMagic() {

        }

        fun setInitialData(
            items: XmlPullParser,
            names: XmlPullParser,
            recipes: XmlPullParser,
            enemies: XmlPullParser,
            locations: XmlPullParser
        ) {
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