package com.example.finalproject.service.classes

import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.MainActivity.Companion.player
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val name: String,
    val id: Int,
    val description: String,
    val enemiesToKill: ArrayList<Pair<Int, Int>> = ArrayList(),
    val itemsToObtain: ArrayList<Pair<Int, Int>> = ArrayList(),
    val goldToGain: Int = -1,
    val levelToReach: Int = -1,
    val itemsGiven: ArrayList<Pair<Int, Int>> = ArrayList(),
    val goldGiven: Int = 0,
    val experienceGiven: Int = 0
) {
    var completed: Boolean = false
    var taken: Boolean = false

    /**
     * @return true if task was completed, false otherwise
     * checks whether the task was completed
     */
    fun checkTask(): Boolean {
        completed = taken && player.gold >= goldToGain && player.level >= levelToReach
        if (enemiesToKill.isNotEmpty()) {
            for (enemy in enemiesToKill) {
                completed =
                    completed && assets.enemiesKilled[enemy.first]?.let { it >= enemy.second } ?: false
                if (!completed)
                    break
            }
        }
        if (itemsToObtain.isNotEmpty()) {
            for (item in itemsToObtain) {
                completed =
                    completed && assets.itemsObtained[item.first]?.let { it >= item.second } ?: false
                if (!completed)
                    break
            }
        }
        if (completed) {
            player.gold += goldGiven
            player.experience += experienceGiven
            player.levelUp()
            for (item in itemsGiven)
                player.addItemsToInventory(Pair(item.first, assets.items[item.second]!!))
        }
        return completed
    }
}