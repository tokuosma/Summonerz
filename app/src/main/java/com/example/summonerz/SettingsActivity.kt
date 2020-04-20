package com.example.summonerz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    private lateinit var creditView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        creditView = findViewById(R.id.credits_view)
        creditView.movementMethod = LinkMovementMethod.getInstance()
    }


}
