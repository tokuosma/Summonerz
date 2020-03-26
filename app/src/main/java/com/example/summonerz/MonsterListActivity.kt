package com.example.summonerz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_monster_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MonsterListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList(){
        doAsync {
            val db = Room.databaseBuilder(applicationContext, MonsterDatabase::class.java, "monsters").build()
            val monsters = db.monsterDao().getAllMonsters()
            db.close()

            uiThread {
                if(monsters.isNotEmpty()){
                    val adapter = MonsterAdapter(applicationContext, monsters)
                    monster_list.adapter = adapter
                } else{
                    toast("No monsters yet. Go scan some!")
                }

            }
        }
    }
}
