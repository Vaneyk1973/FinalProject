package com.example.finalproject.fragments

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
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
import kotlin.math.abs
import com.example.finalproject.fragments.MainActivity.Companion.player
import java.util.*
import kotlin.random.Random

class MapFragment(val mapNum: Int = 0) : Fragment(), View.OnClickListener {

    private val idLocation = 512
    private val visibleMap: Array<Array<ImageView?>> = Array(5) { arrayOfNulls(5) }
    private val map = MainActivity.map[mapNum].map

    constructor() : this(0) {
        player.mapNum = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val table: TableLayout = requireView().findViewById(R.id.tableLayout)
        val rows: Array<TableRow?> = arrayOfNulls(table.childCount)
        for (i in 0 until table.childCount) rows[i] = table.getChildAt(i) as TableRow
        for (i in 0..4) {
            for (j in 0..4) {
                visibleMap[i][j] = rows[i]!!.getChildAt(j) as ImageView
                visibleMap[i][j]!!.setOnClickListener(this)
                if (i == 2 && j == 2)
                    visibleMap[i][j]!!.setImageBitmap(MainActivity.getAvatar())
                else
                    visibleMap[i][j]!!.setImageBitmap(
                        Bitmap.createBitmap(
                            map[player.coordinates[mapNum].first - 2 + i][player.coordinates[mapNum].second - 2 + j].getTexture()
                        )
                    )

            }
        }
    }

    private fun findTitleCoordinates(v: ImageView, p: Array<Array<ImageView?>>): Pair<Int, Int> {
        for (i in 0..4) {
            for (j in 0..4) {
                if (v === p[i][j])
                    return Pair(
                        i + player.coordinates[mapNum].first - 2,
                        j + player.coordinates[mapNum].second - 2
                    )
            }
        }
        return Pair(-1, -1)
    }

    private fun isInternetAvailable(): Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }

    override fun onClick(v: View?) {
        val coords = findTitleCoordinates(v as ImageView, visibleMap)
        var playerCoords1: Pair<Int, Int> = player.coordinates[mapNum]
        if (player.coordinates[mapNum] != coords
            && map[coords.first][coords.second].id != idLocation
            && map[coords.first][coords.second].id != 255 + idLocation
        ) {
            val dx = coords.first - playerCoords1.first
            val dy = coords.second - playerCoords1.second
            if (abs(dx) <= 1 && abs(dy) <= 1) {
                player.regenerate()
                var chance = Random(Date().time).nextInt(101)
                val coordsId = map[coords.first][coords.second].id
                if (coordsId != 512 && mapNum != 1 && chance < MainActivity.chancesOfFight[coordsId]!!) {
                    val fm = parentFragmentManager
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                    fragmentTransaction.add(R.id.fight, FightFragment())
                    fragmentTransaction.commit()
                    chance = Random(Date().time).nextInt(101)
                    when (coordsId) {
                        1 + idLocation -> {
                            if (chance < 30) player.enemy = Enemy(
                                MainActivity.chancesOfEnemy[1 + idLocation]!![30]!!
                            )
                            else player.enemy = Enemy(
                                MainActivity.chancesOfEnemy[1 + idLocation]!![70]!!
                            )
                        }
                        2 + idLocation -> {
                            when {
                                chance < 60 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[2 + idLocation]!![60]!!
                                )
                                chance < 95 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[2 + idLocation]!![35]!!
                                )
                                else -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[2 + idLocation]!![5]!!
                                )
                            }
                        }
                        4 + idLocation -> {
                            when {
                                chance < 75 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[4 + idLocation]!![75]!!
                                )
                                chance < 95 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[4 + idLocation]!![20]!!
                                )
                                chance < 99 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[4 + idLocation]!![4]!!
                                )
                                else -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[4 + idLocation]!![1]!!
                                )
                            }
                        }
                        5 + idLocation -> {
                            when {
                                chance < 75 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[5 + idLocation]!![75]!!
                                )
                                chance < 95 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[5 + idLocation]!![20]!!
                                )
                                chance < 99 -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[5 + idLocation]!![4]!!
                                )
                                else -> player.enemy = Enemy(
                                    MainActivity.chancesOfEnemy[5 + idLocation]!![1]!!
                                )
                            }
                        }
                        6 + idLocation -> {
                            if (chance < 60) player.enemy = Enemy(
                                MainActivity.chancesOfEnemy[6 + idLocation]!![60]!!
                            )
                            else player.enemy = Enemy(
                                MainActivity.chancesOfEnemy[6 + idLocation]!![40]!!
                            )
                        }
                        7 + idLocation -> {
                            if (chance < 60) player.enemy = Enemy(
                                MainActivity.chancesOfEnemy[7 + idLocation]!![60]!!
                            )
                            else player.enemy = Enemy(
                                MainActivity.chancesOfEnemy[7 + idLocation]!![40]!!
                            )
                        }
                    }
                } else {
                    when (coordsId) {
                        3 + idLocation -> {
                            player.mapNum = 1
                            player.coordinates[1] = Pair(6, 3)
                            val fm = parentFragmentManager
                            val fragmentTransaction = fm.beginTransaction()
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                            fragmentTransaction.add(R.id.map, MapFragment(1))
                            fragmentTransaction.commit()
                        }
                        9 + idLocation -> {
                            val fm = parentFragmentManager
                            val fragmentTransaction = fm.beginTransaction()
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                            fragmentTransaction.add(R.id.tasks, TaskManagerFragment(true))
                            fragmentTransaction.commit()
                        }
                        10 + idLocation -> {
                            if (isInternetAvailable()) {
                                if (player.user.login.isEmpty())
                                    Toast.makeText(context, "Sign in first", Toast.LENGTH_SHORT)
                                        .show()
                                else {
                                    val fm = parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                    fragmentTransaction.add(R.id.fight, FightFragment(true))
                                    fragmentTransaction.commit()
                                }
                            } else
                                Toast.makeText(
                                    context, "Check your Internet connection",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                        11 + idLocation -> {
                            val fm = parentFragmentManager
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
                        12 + idLocation -> {
                            val fm = parentFragmentManager
                            val fragmentTransaction = fm.beginTransaction()
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                            fragmentTransaction.add(R.id.shop, ShopFragment())
                            fragmentTransaction.commit()
                        }
                        13 + idLocation -> {
                            val fm = parentFragmentManager
                            val fragmentTransaction = fm.beginTransaction()
                            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                            fragmentTransaction.add(R.id.map, MapFragment())
                            fragmentTransaction.commit()
                        }
                        14 + idLocation -> {
                            if (isInternetAvailable()) {
                                if (player.user.login.isEmpty()) {
                                    Toast.makeText(
                                        context, "Sign in first",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val fm = parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                    fragmentTransaction.add(R.id.shop, ShopFragment(true))
                                    fragmentTransaction.commit()
                                }
                            } else
                                Toast.makeText(
                                    context, "Check your Internet connection",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                }
                if (coordsId > idLocation + 14 || coordsId < idLocation + 8) {
                    player.coordinates[mapNum] =
                        Pair(playerCoords1.first + dx, playerCoords1.second + dy)
                    playerCoords1 = player.coordinates[mapNum]
                    for (i in 0..4)
                        for (j in 0..4) {
                            visibleMap[i][j]!!.setOnClickListener(this)
                            Log.d("Map", playerCoords1.toString())
                            if (i == 2 && j == 2)
                                visibleMap[i][j]!!.setImageBitmap(MainActivity.getAvatar())
                            else
                                visibleMap[i][j]!!.setImageBitmap(
                                    Bitmap.createBitmap(
                                        map[playerCoords1.first-2+i][playerCoords1.second-2+j].getTexture()
                                    )
                                )
                        }
                }
            }
        }
        (parentFragmentManager.findFragmentById(R.id.status) as StatusBarFragment).update()
    }
}