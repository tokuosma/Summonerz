package com.example.summonerz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var fabOpened = false

        itemList.setOnClickListener{
            val intent = Intent(applicationContext, ItemListActivity::class.java)
            startActivity(intent)
        }
        monsterList.setOnClickListener{
            val intent = Intent(applicationContext, MonsterListActivity::class.java)
            startActivity(intent)
        }
        settings.setOnClickListener{
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
        }
        mapMenu.setOnClickListener{
            if(!fabOpened){
                fabOpened = true
                itemList.animate().translationY(resources.getDimension(R.dimen.standard_66)) //66dp
                monsterList.animate().translationY(resources.getDimension(R.dimen.standard_116)) //132dp
                settings.animate().translationY(resources.getDimension((R.dimen.standard_156))) //(132 + 66)dp
            } else{
                fabOpened = false
                itemList.animate().translationY(0f)
                monsterList.animate().translationY(0f)
                settings.animate().translationY(0f)
            }
        }
        scan_button.setOnClickListener {
            val intent = Intent(applicationContext, ScanActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val oulu = LatLng(65.0, 25.0)
        mMap.addMarker(MarkerOptions().position(oulu).title("Marker in Oulu"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oulu))
    }
}
