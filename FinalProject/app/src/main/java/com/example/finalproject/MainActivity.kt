package com.example.finalproject

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
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.xmlpull.v1.XmlPullParser

class MainActivity : AppCompatActivity() {

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
        setInitialData()
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
        val map = ArrayList<Map>()
        val menu = arrayOfNulls<Bitmap>(4)
        private val mapTextures = ArrayList<Bitmap>()
        private lateinit var avatar: Bitmap
        lateinit var textures: Array<Array<Bitmap>>
        lateinit var music: Music
        private lateinit var sh: SharedPreferences
        lateinit var assets: Assets

        @kotlinx.serialization.Serializable
        class Assets {
            val chancesOfFight =
                HashMap<Int, Int>() //<id of location (field, forest etc.), chance of getting into a fight>
            val chancesOfEnemy =
                HashMap<Int, ArrayList<Pair<Int, Int>>>() //<id of location, <chance of getting into a fight with that enemy, id of an enemy>
            val enemies = HashMap<Int, Enemy>() //<id of an enemy, object template>
            val elements = ArrayList<Element>()
            val manaChannels = ArrayList<ManaChannel>()
            val types = ArrayList<Type>()
            val forms = ArrayList<Form>()
            val manaReservoirs = ArrayList<ManaReservoir>()
            val researches = ArrayList<Research>()
            val recipes = ArrayList<Recipe>()
            val items = HashMap<Int, Item>() //<id of an item, object template>
            val shopList = ArrayList<Item>() //<items available in the shop>
            val tasks = ArrayList<Task>()
        }

        fun getAvatar(): Bitmap = Bitmap.createBitmap(avatar)

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

        fun setInitialData() {
            setTextures()

            var data = ""
            val parser = res.getXml(R.xml.initial_data)
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                if (parser.name == "data") {
                    data = parser.text
                }
                parser.next()
            }
            assets = Json.decodeFromString(data)
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