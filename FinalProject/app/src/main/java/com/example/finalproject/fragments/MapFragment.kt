package com.example.finalproject.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
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
import java.util.*
import kotlin.math.abs

class MapFragment(val mapNum: Int=0): Fragment() {
    constructor() : this(0) {
        MainActivity.player.mapNum = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playerCoords: Pair<Int, Int> = MainActivity.player.coordinates[mapNum]
        val table:TableLayout = requireView().findViewById(R.id.tableLayout)
        val rows:Array<TableRow?> = arrayOfNulls(table.childCount)
        for (i in 0 until table.childCount) rows[i] = table.getChildAt(i) as TableRow
        val visibleMap:Array<Array<ImageView?>> = Array(5) {arrayOfNulls(5)}
        val idLocation = 512
        val onClickListener =
            View.OnClickListener { v ->
                val coords = findTitleCoordinates(v as ImageView, visibleMap)
                var playerCoords1: Pair<Int, Int> = MainActivity.player.coordinates[mapNum]
                if (MainActivity.player.coordinates[mapNum] != coords
                    && MainActivity.map[mapNum].map[coords.first][coords.second].id != idLocation
                    && MainActivity.map[mapNum].map[coords.first][coords.second].id != 255 + idLocation) {
                    val dx = coords.first - playerCoords1.first
                    val dy = coords.second - playerCoords1.second
                    if (abs(dx) <= 1 && abs(dy) <= 1) {
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
                                1+idLocation -> {
                                    if (a < 30) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[1 + idLocation]!![30]!!)
                                    else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[1 + idLocation]!![70]!!)
                                }
                                2 + idLocation -> {
                                    when {
                                        a < 60 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[2 + idLocation]!![60]!!)
                                        a < 95 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[2 + idLocation]!![35]!!)
                                        else -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[2 + idLocation]!![5]!!)
                                    }
                                }
                                4 + idLocation -> {
                                    when {
                                        a < 75 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[4 + idLocation]!![75]!!)
                                        a < 95 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[4 + idLocation]!![20]!!)
                                        a < 99 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[4 + idLocation]!![4]!!)
                                        else -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[4 + idLocation]!![1]!!)
                                    }
                                }
                                5 + idLocation -> {
                                    when {
                                        a < 75 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[5 + idLocation]!![75]!!)
                                        a < 95 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[5 + idLocation]!![20]!!)
                                        a < 99 -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[5 + idLocation]!![4]!!)
                                        else -> MainActivity.player.enemy = Enemy(
                                            MainActivity.chancesOfEnemy[5 + idLocation]!![1]!!)
                                    }
                                }
                                6 + idLocation -> {
                                    if (a < 60) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[6 + idLocation]!![60]!!)
                                    else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[6 + idLocation]!![40]!!
                                    )
                                }
                                7 + idLocation -> {
                                    if (a < 60) MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[7 + idLocation]!![60]!!)
                                    else MainActivity.player.enemy = Enemy(
                                        MainActivity.chancesOfEnemy[7 + idLocation]!![40]!!
                                    )
                                }
                            }
                        } else {
                            when (coordsId) {
                                3 + idLocation -> {
                                    MainActivity.player.mapNum = 1
                                    MainActivity.player.coordinates[1]=Pair(6, 3)
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
                                        if (MainActivity.player.user.login.isEmpty())
                                            Toast.makeText(context, "Sign in first", Toast.LENGTH_SHORT).show()
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
                                            Toast.LENGTH_SHORT).show()
                                }
                                11 + idLocation -> {
                                    val fm = parentFragmentManager
                                    val fragmentTransaction = fm.beginTransaction()
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                    fragmentTransaction.add(R.id.crafting_station, CraftingStationFragment())
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
                                        if (MainActivity.player.user.login.isEmpty()) {
                                            Toast.makeText(
                                                context, "Sign in first",
                                                Toast.LENGTH_SHORT).show()
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
                                        Toast.makeText(context, "Check your Internet connection",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        if (coordsId > idLocation+14 || coordsId < idLocation+8) {

                            MainActivity.map[mapNum].map[playerCoords1.first][playerCoords1.second].setTexture(
                                MainActivity.player.getTileTexture())

                            MainActivity.player.coordinates[mapNum]=Pair(playerCoords1.first + dx, playerCoords1.second + dy)

                            playerCoords1 = MainActivity.player.coordinates[mapNum]

                            MainActivity.player.setTileTexture(
                                MainActivity.map[mapNum].map[playerCoords1.first][playerCoords1.second].getTexture())

                            MainActivity.map[mapNum].map[playerCoords1.first][playerCoords1.second].setTexture(
                                MainActivity.player.getAvatar())

                            if (playerCoords1.first + dx >= 2 && playerCoords1.second + dy >= 2) {
                                for (i in 0..4)
                                    for (j in 0..4)
                                        visibleMap[i][j]!!.setImageBitmap(
                                            MainActivity.map[mapNum].map[playerCoords1.first - 2 + i][playerCoords1.second - 2 + j].getTexture())
                            } else if (playerCoords1.first + dx >= 2) {
                                for (i in 0..4)
                                    for (j in 0..4)
                                        visibleMap[i][j]!!.setImageBitmap(
                                            MainActivity.map[mapNum].map[playerCoords1.first - 2 + i][j].getTexture())
                            } else if (playerCoords1.second + dy >= 2) {
                                for (i in 0..4)
                                    for (j in 0..4)
                                        visibleMap[i][j]!!.setImageBitmap(
                                            MainActivity.map[mapNum].map[i][playerCoords1.second - 2 + j].getTexture())
                            } else {
                                for (i in 0..4)
                                    for (j in 0..4)
                                        visibleMap[i][j]!!
                                            .setImageBitmap(MainActivity.map[mapNum].map[i][j].getTexture())
                            }
                        }
                    }
                }
                (parentFragmentManager.findFragmentById(R.id.status) as StatusBarFragment).update()
            }
        for (i in 0..4) {
            for (j in 0..4) {
                visibleMap[i][j] = rows[i]!!.getChildAt(j) as ImageView
                visibleMap[i][j]!!.setImageBitmap(
                    MainActivity.map[mapNum].map[playerCoords.first - 2 + i][playerCoords.second - 2 + j].getTexture())
                visibleMap[i][j]!!.setOnClickListener(onClickListener)
            }
        }
        MainActivity.player.setTileTexture(
            MainActivity.map[mapNum].map[MainActivity.player.coordinates[mapNum].first][MainActivity.player.coordinates[mapNum].second].getTexture())
        MainActivity.map[mapNum].map[MainActivity.player.coordinates[mapNum].first][MainActivity.player.coordinates[mapNum].second]
            .setTexture(MainActivity.player.getAvatar())
    }

    private fun findTitleCoordinates(v: ImageView, p: Array<Array<ImageView?>>): Pair<Int, Int> {
        for (i in 0..4) {
            for (j in 0..4) {
                if (v === p[i][j])
                    return Pair(
                        i + MainActivity.player.coordinates[mapNum].first - 2,
                        j + MainActivity.player.coordinates[mapNum].second - 2)
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
}