package com.example.summonerz

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class MonsterRepository(private val monsterDao: MonsterDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allMonsters: LiveData<List<Monster>> = monsterDao.getAllMonsters()

    fun getMonster(scanValue: String): Monster {
        return monsterDao.getMonsterByScanValue(scanValue)
    }

    fun getMonster(id: Int): Monster {
        return monsterDao.getMonsterById(id)
    }

    suspend fun insert(monster: Monster) {
        monsterDao.insert(monster)
    }
}