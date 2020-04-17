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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.toast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var client: GoogleApiClient
    private lateinit var request: LocationRequest

    private lateinit var geofencingClient: GeofencingClient
    private val USER_GEOFENCE_ID = "USER_GEOFENCE_ID"
    private val PLACE_GEOFENCE_ID = "PLACE_GEOFENCE_ID"
    private val USER_GEOFENCE_RADIUS = 1000.0
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
        disablescan(this)
        if (BuildConfig.DEBUG) {
            enablescan(this)
        }
        geofencingClient = LocationServices.getGeofencingClient(this)


    }

    fun createUserGeoFence(
        location:LatLng,
        geofencingClient: GeofencingClient
    ) {
            val geofence = Geofence.Builder().setRequestId(USER_GEOFENCE_ID)
                .setCircularRegion(
                    location.latitude,
                    location.longitude,
                    USER_GEOFENCE_RADIUS.toFloat())
                .setExpirationDuration(Geofence.NEVER_EXPIRE) //For now
                .setTransitionTypes(
                    Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()

            val geofenceRequest =
                GeofencingRequest.Builder().setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .addGeofence(geofence).build()

            val intent = Intent(this, GeofenceReceiver::class.java)
                .putExtra("type", "user")
    }

    fun createPlaceGeoFence(
        location:LatLng,
        geofencingClient: GeofencingClient
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

        val intent = Intent(this, GeofenceReceiver::class.java)
            .putExtra("type", "place")

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        geofencingClient.addGeofences(geofenceRequest,pendingIntent)
    }

    fun disablescan(context: Context) {
        scan_button.isEnabled = false
    }

    fun enablescan(context: Context) {
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

    /**
     * Google places getting places
     * Needs URL work to get place data
     * Needs JSON parser to get places
     * Very mysterious
    */
    companion object places {

        fun getplaces(): List<LatLng> {
            var yo: LatLng = LatLng(65.060828, 25.464570)   //university
            var ksk: LatLng = LatLng(65.012962, 25.466919)  //downtown
            var tra: LatLng = LatLng(65.021997, 25.469383)  //tuira
            val places = listOf(yo, ksk, tra)
            return places
        }

    }
    /*
    private fun getplaces(
        map: GoogleMap,
        location: LatLng
    ) {

        var places = "grocery store"
        var key: String = R.values.google_maps_api //Fix API key
        var ad = java.lang.String.format(
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=%s&location=%7f,%7f&radius=2000&type=%s",
            key, location.latitude, location.longitude,places
        )
        var url: Uri = Uri.parse(ad)

        map.clear()
        //returns list of latlongs
        //draw geofences from them

    }*/

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
                    var locs: List<LatLng> = getplaces()
                    gMap.clear()
                    createUserGeoFence(latLong,geofencingClient)
                    var circleoptions: CircleOptions = CircleOptions()
                    circleoptions.center(latLong)
                    circleoptions.radius(USER_GEOFENCE_RADIUS)
                    circleoptions.strokeColor(Color.RED)
                    circleoptions.fillColor(Color.GREEN)
                    gMap.addCircle(circleoptions)
                    for (loc in locs) {
                        circleoptions.center(loc)
                        circleoptions.radius(30.0)
                        circleoptions.strokeColor(Color.WHITE)
                        circleoptions.fillColor(Color.YELLOW)
                        gMap.addCircle(circleoptions)
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
