package com.example.summonerz

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import org.json.JSONObject
import java.util.*

class GeofenceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val geofencingTransition = geofencingEvent.geofenceTransition
        var locations: MutableList<Pair<Int,PlaceOfPower>> = mutableListOf<Pair<Int,PlaceOfPower>>()
        var to_remove: List<Pair<Int,PlaceOfPower>> = mutableListOf<Pair<Int,PlaceOfPower>>()
        val places: List<PlaceOfPower> = MapsActivity.places.getplaces(context!!)

        if(geofencingTransition==Geofence.GEOFENCE_TRANSITION_ENTER) {
            locations.add(Pair(intent!!.getIntExtra("id", 0),places[intent!!.getIntExtra("id", 0)]))
        }
        else if(geofencingTransition==Geofence.GEOFENCE_TRANSITION_DWELL) {
            //Activate scan
            //MapsActivity.enablescan() not working currently
        }
        else if(geofencingTransition==Geofence.GEOFENCE_TRANSITION_EXIT) {
            to_remove = locations.filter { (key, value) -> key == intent!!.getIntExtra("id", 0) }
            locations.removeAll(to_remove)
            //Remove place from list
        }
    }
}