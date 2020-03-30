package com.example.summonerz

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonsterListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val monsterViewModel: MonsterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)

        val adapter = MonsterListAdapter()
        recyclerView = findViewById<RecyclerView>(R.id.monster_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        monsterViewModel.allMonsters.observe(this, Observer { monsters: List<Monster> ->
            monsters.let {
                adapter.setMonsters(it)
            }
        })

    }

//    override fun onResume() {
//        super.onResume()
//        refreshList()
//    }
//
//    private fun refreshList(){
//        doAsync {
//            val db = Room.databaseBuilder(applicationContext, MonsterDatabase::class.java, "monsters").build()
//            val monsters = db.monsterDao().getAllMonsters()
//            db.close()
//
////            uiThread {
////                if(monsters.isNotEmpty()){
////                    val adapter = MonsterAdapter(applicationContext, monsters)
////                    monster_list.adapter = adapter
////                } else{
////                    toast("No monsters yet. Go scan some!")
////                }
////
////            }
//        }
//    }
}
