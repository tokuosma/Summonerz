package com.example.summonerz

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val geofencingTransition = geofencingEvent.geofenceTransition

        if(intent!!.getStringExtra("type")=="user") {
            if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                //Get places with MapsActivity.getplaces
                //Create place geofences with createPlaceGeofence
            } else if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                //Delete user geofence
                //Get user location
                //Create new user geofence
            }
        } else if(intent!!.getStringExtra("type")=="place") {
            if(geofencingTransition==Geofence.GEOFENCE_TRANSITION_ENTER) {
                //Add place to list
            }
            else if(geofencingTransition==Geofence.GEOFENCE_TRANSITION_DWELL) {
                //Activate scan
                //MapsActivity.enablescan(context!!) not working currently
            }
            else if(geofencingTransition==Geofence.GEOFENCE_TRANSITION_EXIT) {
                //Remove place from list
            }
        }
    }
}