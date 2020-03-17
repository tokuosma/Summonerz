package com.example.summonerz

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Build.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.toast
//import org.jetbrains.anko.toast
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient

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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==123){
            if(grantResults.isNotEmpty()&& (grantResults[0] == PackageManager.PERMISSION_DENIED ||
                        grantResults[1] == PackageManager.PERMISSION_DENIED )){
                toast("The reminder app needs all the permissions to function")
            }
            if(VERSION.SDK_INT >= VERSION_CODES.Q){
                if(grantResults.isNotEmpty()&& (grantResults[2] == PackageManager.PERMISSION_DENIED)){
                    toast("The reminder app needs all the permissions to function")
                }
            }
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
    override fun onMapReady(map: GoogleMap?) {
        gMap=map?:return
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            gMap.isMyLocationEnabled=true
            fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener {location: Location?->
                if(location != null){
                    var latLong = LatLng(location.latitude, location.longitude)
                    with(gMap){
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 13f))
                    }
                }

            }
        } else {
            var permissions = mutableListOf<String>()
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            if(VERSION.SDK_INT >= VERSION_CODES.Q){
                permissions.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            ActivityCompat.requestPermissions(this,
                permissions.toTypedArray(),
                123)
        }
        gMap.setOnMapClickListener {location:LatLng ->
            with(gMap){
                clear()
                animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))

                val geocoder= Geocoder(applicationContext, Locale.getDefault())
                var title = ""
                var city = ""
                try{

                    val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    city = addressList.get(0).locality
                    title = addressList.get(0).getAddressLine(1)

                }catch (e:Exception){

                }
                val marker=addMarker(MarkerOptions().position(location).snippet(city).title(title))
                marker.showInfoWindow()
                addCircle(
                    CircleOptions().center(location)
                    .strokeColor(Color.argb(50,0,70,70))
                    .fillColor(Color.argb(100,150,150,150)))

            }
        }
    }
}
