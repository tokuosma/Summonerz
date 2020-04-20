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
    private lateinit var hpView: TextView
    private lateinit var mpView: TextView
    private lateinit var strengthView: TextView
    private lateinit var speedView: TextView
    private lateinit var intelligenceView: TextView
    private lateinit var hpMutationView: TextView
    private lateinit var mpMutationView: TextView
    private lateinit var strengthMutationView: TextView
    private lateinit var speedMutationView: TextView
    private lateinit var intelligenceMutationView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_view)

        iconView = findViewById<ImageView>(R.id.monster_view_icon)
        nameView = findViewById(R.id.monster_view_name)
        hpView = findViewById(R.id.monster_view_hp)
        mpView = findViewById(R.id.monster_view_mp)
        strengthView = findViewById(R.id.monster_view_strength)
        speedView = findViewById(R.id.monster_view_speed)
        intelligenceView = findViewById(R.id.monster_view_intelligence)
        hpMutationView = findViewById(R.id.hp_mutations_view)
        mpMutationView = findViewById(R.id.mp_mutations_view)
        strengthMutationView = findViewById(R.id.strength_mutations_view)
        speedMutationView = findViewById(R.id.speed_mutations_view)
        intelligenceMutationView = findViewById(R.id.intelligence_mutations_view)


        monster = intent.getSerializableExtra(EXTRA_MONSTER) as Monster

        iconView.setImageResource(
            applicationContext.resources.getIdentifier(
                monster.icon, "drawable",
                applicationContext.packageName
            )
        )
        nameView.text = monster.name
        hpView.text = "HP: " + monster.hp.toString()
        mpView.text = "MP: " +monster.mp.toString()
        strengthView.text = "STRENGTH: " +monster.strength.toString()
        speedView.text = "SPEED: " +monster.speed.toString()
        intelligenceView.text = "INTELLIGENCE: " +monster.intelligence.toString()
        hpMutationView.text = monster.hp_mutation
        mpMutationView.text = monster.mp_mutation
        strengthMutationView.text = monster.strength_mutation
        speedMutationView.text = monster.speed_mutation
        intelligenceMutationView.text = monster.intelligence_mutation


    }
}
