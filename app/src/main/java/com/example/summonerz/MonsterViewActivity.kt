package com.example.summonerz

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.summonerz.MonsterListActivity.Companion.EXTRA_MONSTER

class MonsterViewActivity : AppCompatActivity() {

    private lateinit var monster: Monster

    private lateinit var iconView: ImageView
    private lateinit var nameView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_view)

        iconView = findViewById<ImageView>(R.id.monster_view_icon)
        nameView = findViewById(R.id.monster_view_name)

        monster = intent.getSerializableExtra(EXTRA_MONSTER) as Monster

        iconView.setImageResource(
            applicationContext.resources.getIdentifier(
                monster.icon, "drawable",
                applicationContext.packageName
            )
        )
        nameView.text = monster.name

    }
}
