package com.example.finalproject.fragments

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R
import java.util.Date
import kotlin.math.abs
import kotlin.random.Random

private const val MIN_LOCATION_ID = 512
private const val MAX_LOCATION_ID = MIN_LOCATION_ID + 255

class MapFragment(private val mapNum: Int = 0) : Fragment(), View.OnClickListener {

    private val visibleMap: Array<Array<ImageView?>> = Array(5) { arrayOfNulls(5) }
    private val map = MainActivity.map[mapNum].map
    private var enemyId: Int = -1

    constructor() : this(0) {
        player.mapNumber = 0
    }

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    /**
     * initializes graphic components
     */
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
                            map[player.coordinates[mapNum].first - 2 + i]
                                    [player.coordinates[mapNum].second - 2 + j].getTexture()
                        )
                    )

            }
        }
    }

    /**
     * @param v the view of a title
     * @param p the array of views 'map'
     * @return coordinates of the title
     * finds the coordinates of a title
     */
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

    /**
     * @return true if the network is available, false otherwise
     * checks if the network is available
     */
    private fun isInternetAvailable(): Boolean {
        val activeNetwork =
            (requireContext().getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager).activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(v: View?) {
        if (v is ImageView) {
            val clickCoordinates = findTitleCoordinates(v, visibleMap)
            var playerCoordinates: Pair<Int, Int> = player.coordinates[mapNum]
            if (player.coordinates[mapNum] != clickCoordinates
                && map[clickCoordinates.first][clickCoordinates.second].id != MIN_LOCATION_ID
                && map[clickCoordinates.first][clickCoordinates.second].id != MAX_LOCATION_ID
            ) {
                val dx = clickCoordinates.first - playerCoordinates.first
                val dy = clickCoordinates.second - playerCoordinates.second
                if (abs(dx) <= 1 && abs(dy) <= 1) {
                    player.regenerate()
                    var chance = Random(Date().time).nextInt(101)
                    val tileId = map[clickCoordinates.first][clickCoordinates.second].id
                    if (mapNum != 1 && chance < assets.chancesOfFight[tileId]!!) {
                        chance = Random(Date().time).nextInt(101)
                        var prevChance = 0
                        for (enemyChance in assets.chancesOfEnemy[tileId]!!) {
                            if (prevChance <= chance && chance < prevChance + enemyChance.first) {
                                enemyId = enemyChance.second
                                break
                            }
                            prevChance += enemyChance.first
                        }
                        val fragmentManager = parentFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.map)!!)
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.menu)!!)
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.status)!!)
                        fragmentTransaction.add(R.id.fight, FightFragment(enemyId))
                        fragmentTransaction.commit()
                    } else {
                        when (tileId) {
                            3 + MIN_LOCATION_ID -> {
                                player.mapNumber = 1
                                player.coordinates[1] = Pair(6, 3)
                                val fm = parentFragmentManager
                                val fragmentTransaction = fm.beginTransaction()
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                fragmentTransaction.add(R.id.map, MapFragment(1))
                                fragmentTransaction.commit()
                            }

                            9 + MIN_LOCATION_ID -> {
                                val fm = parentFragmentManager
                                val fragmentTransaction = fm.beginTransaction()
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                fragmentTransaction.add(R.id.tasks, TaskManagerFragment(true))
                                fragmentTransaction.commit()
                            }

                            10 + MIN_LOCATION_ID -> {
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
                                        fragmentTransaction.add(R.id.duel, DuelFragment())
                                        fragmentTransaction.commit()
                                    }
                                } else
                                    Toast.makeText(
                                        context, "Check your Internet connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }

                            11 + MIN_LOCATION_ID -> {
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

                            12 + MIN_LOCATION_ID -> {
                                val fm = parentFragmentManager
                                val fragmentTransaction = fm.beginTransaction()
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                                fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                                fragmentTransaction.add(R.id.shop, ShopFragment())
                                fragmentTransaction.commit()
                            }

                            13 + MIN_LOCATION_ID -> {
                                val fm = parentFragmentManager
                                val fragmentTransaction = fm.beginTransaction()
                                fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                                fragmentTransaction.add(R.id.map, MapFragment())
                                fragmentTransaction.commit()
                            }

                            14 + MIN_LOCATION_ID -> {
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
                    if (tileId !in MIN_LOCATION_ID + 8..MIN_LOCATION_ID + 14) {
                        player.coordinates[mapNum] =
                            Pair(playerCoordinates.first + dx, playerCoordinates.second + dy)
                        playerCoordinates = player.coordinates[mapNum]
                        for (i in 0..4)
                            for (j in 0..4) {
                                visibleMap[i][j]!!.setOnClickListener(this)
                                if (i == 2 && j == 2)
                                    visibleMap[i][j]!!.setImageBitmap(MainActivity.getAvatar())
                                else
                                    visibleMap[i][j]!!.setImageBitmap(
                                        Bitmap.createBitmap(
                                            map[playerCoordinates.first - 2 + i][playerCoordinates.second - 2 + j].getTexture()
                                        )
                                    )
                            }
                    }
                }
            }
            (parentFragmentManager.findFragmentById(R.id.status) as StatusBarFragment).update()
        }
    }
}