package com.example.summonerz

//import org.jetbrains.anko.toast

//import org.jetbrains.anko.toast
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.summonerz.Utils.getDataFromAsset
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.toast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var client: GoogleApiClient
    private lateinit var request: LocationRequest

    private lateinit var geofencingClient: GeofencingClient
    private var PLACE_GEOFENCE_ID = "GEOFENCE_ID"
    private val PLACE_GEOFENCE_RADIUS = 100.0
    private val GEOFENCE_EXPIRATION = 1000*60*60
    private val GEOFENCE_DWELL_DELAY = 1000*60


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var fabOpened = false

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
                //itemList.animate().translationY(resources.getDimension(R.dimen.standard_66)) //66dp
                monsterList.animate()
                    .translationY(resources.getDimension(R.dimen.standard_66)) //132dp
                settings.animate()
                    .translationY(resources.getDimension((R.dimen.standard_116))) //(132 + 66)dp
            } else{
                fabOpened = false
                //itemList.animate().translationY(0f)
                monsterList.animate().translationY(0f)
                settings.animate().translationY(0f)
            }
        }
        scan_button.setOnClickListener {
            val intent = Intent(applicationContext, ScanMonsterActivity::class.java)
            startActivity(intent)
        }
        disablescan()
        if (BuildConfig.DEBUG) {
            enablescan()
        }
        geofencingClient = LocationServices.getGeofencingClient(this)


    }

    fun createPlaceGeoFence(
        location:LatLng,
        geofencingClient: GeofencingClient,
        id:Int
    ) {
        val geofence = Geofence.Builder().setRequestId(PLACE_GEOFENCE_ID)
            .setCircularRegion(
                location.latitude,
                location.longitude,
                PLACE_GEOFENCE_RADIUS.toFloat())
            .setExpirationDuration(Geofence.NEVER_EXPIRE) //For now
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofenceRequest =
            GeofencingRequest.Builder().setInitialTrigger(Geofence.GEOFENCE_TRANSITION_DWELL)
                .addGeofence(geofence).build()

        val intent = Intent(this, GeofenceReceiver::class.java).putExtra("id", id)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        geofencingClient.addGeofences(geofenceRequest,pendingIntent)
    }

    fun disablescan() {
        scan_button.isEnabled = false
    }

    fun enablescan() {
        scan_button.isEnabled = true
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

    companion object places {

        fun getplaces(context: Context): List<PlaceOfPower> {
            val locationString = getDataFromAsset(context, "locations.json")
            val listLocations =
                object : TypeToken<List<com.example.summonerz.PlaceOfPower>>() {}.type
            val gson = Gson()
            return gson.fromJson(locationString, listLocations)
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap?) {
        gMap=map?:return
        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
//        gMap.
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            gMap.isMyLocationEnabled=true
            fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener {location: Location?->
                if(location != null){
                    var latLong = LatLng(location.latitude, location.longitude)
                    with(gMap){
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 13f))
                    }
                    var locs: List<PlaceOfPower> = getplaces(applicationContext)
                    gMap.clear()
                    for ((id, loc) in locs.withIndex()) {
                        createPlaceGeoFence(LatLng(loc.lat, loc.long), geofencingClient, id)
                        gMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(loc.lat, loc.long))
                                .title(loc.name)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_red_tower))
                        )
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
        gMap.setMinZoomPreference(14.0f)
        gMap.setMaxZoomPreference(17.0f)
//        gMap.setOnMapClickListener {location:LatLng ->
//            with(gMap){
//                clear()
//                animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
//
//                val geocoder= Geocoder(applicationContext)
//                var title = ""
//                var city = ""
//                try{
//
//                    val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                    city = addressList[0].locality
//                    title = addressList[0].getAddressLine(1)
//
//                }catch (e:Exception){
//
//                }
//                val marker=addMarker(MarkerOptions().position(location).snippet(city).title(title))
//                marker.showInfoWindow()
//                addCircle(
//                    CircleOptions().center(location)
//                    .strokeColor(Color.argb(50,0,70,70))
//                    .fillColor(Color.argb(100,150,150,150)))
//
//            }
//        }


    }

}

data class PlaceOfPower(val name: String, val lat: Double, val long: Double)
