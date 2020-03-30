package com.example.summonerz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MonsterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MonsterRepository

    val allMonsters: LiveData<List<Monster>>

    init {
        val monsterDao = MonsterDatabase.getDatabase(application, viewModelScope).monsterDao()
        repository = MonsterRepository(monsterDao)
        allMonsters = repository.allMonsters
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(monster: Monster) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(monster)
    }

    fun getMonster(scanValue: String): Monster? {
        return repository.getMonster(scanValue)
    }

    fun getMonster(id: Int): Monster? {
        return repository.getMonster(id)
    }


}