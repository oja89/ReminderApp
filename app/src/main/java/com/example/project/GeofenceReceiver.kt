package com.example.project

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    lateinit var key: String
    lateinit var text: String

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            val geofencingTransition = geofencingEvent.geofenceTransition

            if (
                geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL
            ) {
                // Retrieve data from intent
                if (intent != null) {
                    key = intent.getStringExtra("key")!!
                    text = intent.getStringExtra("message")!!
                }
            }
        }
    }
}

