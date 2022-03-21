package com.example.finalproject.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.entities.Enemy
import java.util.*

class MapFragment : Fragment {
    private val mapNum: Int

    constructor(mapNum: Int) {
        this.mapNum = mapNum
    }

    internal constructor() {
        mapNum = 0
        MainActivity.player.mapNum = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val player_coords: Pair<Int, Int> = MainActivity.player.coordinates
        val table = getView()!!.findViewById<TableLayout>(R.id.tableLayout)
        val rows = arrayOfNulls<TableRow>(table.childCount)
        for (i in 0 until table.childCount) rows[i] = table.getChildAt(i) as TableRow
        val visible_map = Array(5) {
            arrayOfNulls<ImageView>(
                5
            )
        }
        val idLocation = 512
        val onClickListener =
            View.OnClickListener { v ->
                val coords = find_title_coordinates(v as ImageView, visible_map)
                var player_coords: Pair<Int, Int> = MainActivity.player.coordinates
                if (MainActivity.player.coordinates != coords
                    && MainActivity.map[mapNum].map[coords!!.first][coords.second].id != idLocation && MainActivity.map[mapNum].map[coords.first][coords.second].id != 255 + idLocation
                ) {
                    val dx = coords.first - player_coords.first
                    val dy = coords.second - player_coords.second
                    if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
                        MainActivity.player.regenerate()
                        var a = Random().nextInt(100)
                        val coordsId = MainActivity.map[mapNum].map[coords.first][coords.second].id
                        if (coordsId != 512 && mapNum != 1 && a < MainActivity.chancesOfFight[coordsId]!!) {
                            val fm = parentFragmentManager
                            val fragmentTransaction = fm.beginTransaction()
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                            fragmentTransaction.add(R.id.fight, FightFragment())
                            fragmentTransaction.commit()
                            a = Random().nextInt(101)
                            when (coordsId) {
                                513 -> {
                                    if (a < 30) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[1 + idLocation]!![30]!!
                                    ) else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[1 + idLocation]!![70]!!
                                    )
                                }
                                514 -> {
                                    if (a < 60) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[2 + idLocation]!![60]!!
                                    ) else if (a < 95) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[2 + idLocation]!![35]!!
                                    ) else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[2 + idLocation]!![5]!!
                                    )
                                }
                                516 -> {
                                    if (a < 75) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[4 + idLocation]!![75]!!
                                    ) else if (a < 95) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[4 + idLocation]!![20]!!
                                    ) else if (a < 99) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[4 + idLocation]!![4]!!
                                    ) else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[4 + idLocation]!![1]!!
                                    )
                                }
                                517 -> {
                                    if (a < 75) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[5 + idLocation]!![75]!!
                                    ) else if (a < 95) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[5 + idLocation]!![20]!!
                                    ) else if (a < 99) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[5 + idLocation]!![4]!!
                                    ) else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[5 + idLocation]!![1]!!
                                    )
                                }
                                518 -> {
                                    if (a < 60) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[6 + idLocation]!![60]!!
                                    ) else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[6 + idLocation]!![40]!!
                                    )
                                }
                                519 -> {
                                    if (a < 60) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[7 + idLocation]!![60]!!
                                    ) else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[7 + idLocation]!![40]!!
                                    )
                                }
                            }
                        } else {
                            when (coordsId) {
                                3 + 512 -> {
                                    MainActivity.player.mapNum = 1
                                    MainActivity.player.setCoordinates(1, Pair(6, 3))
                                    val fm =
                                        parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.add(R.id.map, MapFragment(1))
                                    fragmentTransaction.commit()
                                }
                                9 + 512 -> {
                                    val fm =
                                        parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                    fragmentTransaction.add(R.id.tasks, TaskManagerFragment(true))
                                    fragmentTransaction.commit()
                                }
                                10 + 512 -> {
                                    if (isInternetAvailable) {
                                        if (MainActivity.player.user.login.isEmpty()) Toast.makeText(
                                            context, "Sign in first", Toast.LENGTH_SHORT
                                        ).show() else {
                                            val fm =
                                                parentFragmentManager
                                            val fragmentTransaction = fm.beginTransaction()
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                            fragmentTransaction.add(R.id.fight, FightFragment(true))
                                            fragmentTransaction.commit()
                                        }
                                    } else Toast.makeText(
                                        context,
                                        "Check your Internet connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                11 + 512 -> {
                                    val fm =
                                        parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                    fragmentTransaction.add(
                                        R.id.crafting_station,
                                        CraftingStationFragment()
                                    )
                                    fragmentTransaction.commit()
                                }
                                12 + 512 -> {
                                    val fm =
                                        parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                    fragmentTransaction.add(R.id.shop, ShopFragment())
                                    fragmentTransaction.commit()
                                }
                                13 + 512 -> {
                                    val fm =
                                        parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.add(R.id.map, MapFragment())
                                    fragmentTransaction.commit()
                                }
                                14 + 512 -> {
                                    if (isInternetAvailable) {
                                        if (MainActivity.player.user.login.isEmpty()) {
                                            Toast.makeText(
                                                context,
                                                "Sign in first",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            val fm =
                                                parentFragmentManager
                                            val fragmentTransaction = fm.beginTransaction()
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                            fragmentTransaction.add(R.id.shop, ShopFragment(true))
                                            fragmentTransaction.commit()
                                        }
                                    } else Toast.makeText(
                                        context,
                                        "Check your Internet connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        if (coordsId > 526 || coordsId < 520) {
                            MainActivity.player.setTileTexture(
                                MainActivity.mapTextures[MainActivity.map[mapNum]
                                    .map[player_coords.first][player_coords.second].id - idLocation]
                            )
                            MainActivity.map[mapNum].map[player_coords.first][player_coords.second].texture =
                                Bitmap.createBitmap(MainActivity.player.getTileTexture())
                            MainActivity.player.setCoordinates(
                                mapNum,
                                Pair(player_coords.first + dx, player_coords.second + dy)
                            )
                            player_coords = MainActivity.player.coordinates
                            MainActivity.player.setTileTexture(
                                Bitmap.createBitmap(
                                    MainActivity.map[mapNum]
                                        .map[player_coords.first][player_coords.second].texture
                                )
                            )
                            MainActivity.map[mapNum].map[player_coords.first][player_coords.second]
                                .texture.eraseColor(Color.BLUE)
                            if (player_coords.first + dx >= 2 && player_coords.second + dy >= 2) {
                                for (i in 0..4) {
                                    for (j in 0..4) {
                                        visible_map[i][j]!!.setImageBitmap(
                                            MainActivity.map[mapNum].map[player_coords.first - 2 + i][player_coords.second - 2 + j].texture
                                        )
                                    }
                                }
                            } else if (player_coords.first + dx >= 2) {
                                for (i in 0..4) {
                                    for (j in 0..4) {
                                        visible_map[i][j]!!.setImageBitmap(
                                            MainActivity.map[mapNum].map[player_coords.first - 2 + i][j].texture
                                        )
                                    }
                                }
                            } else if (player_coords.second + dy >= 2) {
                                for (i in 0..4) {
                                    for (j in 0..4) {
                                        visible_map[i][j]!!.setImageBitmap(
                                            MainActivity.map[mapNum].map[i][player_coords.second - 2 + j].texture
                                        )
                                    }
                                }
                            } else {
                                for (i in 0..4) {
                                    for (j in 0..4) {
                                        visible_map[i][j]!!
                                            .setImageBitmap(MainActivity.map[mapNum].map[i][j].texture)
                                    }
                                }
                            }
                        }
                    }
                }
                StatusBarFragment.update()
            }
        for (i in 0..4) {
            for (j in 0..4) {
                visible_map[i][j] = rows[i]!!.getChildAt(j) as ImageView
                visible_map[i][j]!!.setImageBitmap(
                    MainActivity.map[mapNum]
                        .map[player_coords.first - 2 + i][player_coords.second - 2 + j].texture
                )
                visible_map[i][j]!!.setOnClickListener(onClickListener)
            }
        }
        Log.d("MAP1", MainActivity.map[mapNum].map[3][3].texture.toString() + "")
        MainActivity.player.setTileTexture(
            Bitmap.createBitmap(MainActivity.map[mapNum].map.get(MainActivity.player.coordinates.first)[MainActivity.player.coordinates.second].texture)
        )
        Log.d("MAP2", MainActivity.map[mapNum].map[3][3].texture.toString() + "")
        MainActivity.map[mapNum].map.get(MainActivity.player.coordinates.first)[MainActivity.player.coordinates.second]
            .texture.eraseColor(Color.BLUE)
        Log.d("MAP3", MainActivity.map[mapNum].map[3][3].texture.toString() + "")
    }

    private fun find_title_coordinates(v: ImageView, p: Array<Array<ImageView?>>): Pair<Int, Int>? {
        for (i in 0..4) {
            for (j in 0..4) {
                if (v === p[i][j]) return Pair(
                    i + MainActivity.player.coordinates.first - 2,
                    j + MainActivity.player.coordinates.second - 2
                )
            }
        }
        return null
    }

    private val isInternetAvailable: Boolean
        private get() {
            val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting
        }
}