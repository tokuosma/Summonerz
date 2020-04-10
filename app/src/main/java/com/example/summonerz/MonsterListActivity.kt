package com.example.summonerz

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonsterListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private val monsterViewModel: MonsterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)

        val adapter = MonsterListAdapter(this)
        recyclerView = findViewById<RecyclerView>(R.id.monster_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        monsterViewModel.allMonsters.observe(this, Observer { monsters: List<Monster> ->
            monsters.let {
                adapter.setMonsters(it)
            }
        })
    }

    override fun onItemClicked(monster: Monster) {
        val intent = Intent(applicationContext, MonsterViewActivity::class.java)
        intent.putExtra(EXTRA_MONSTER, monster)
        startActivity(intent)
    }

    companion object {
        val EXTRA_MONSTER_ID = "monster_uid"
        val EXTRA_MONSTER = "monster_serializible"
    }
}

interface OnItemClickListener {
    fun onItemClicked(monster: Monster)
}