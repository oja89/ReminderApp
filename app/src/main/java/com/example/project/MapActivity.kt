package com.example.project

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

const val LOCATION_REQUEST_CODE = 123
const val GEOFENCE_LOCATION_REQUEST_CODE = 12345
const val CAMERA_ZOOM_LEVEL = 13f
const val GEOFENCE_RADIUS = 500
const val GEOFENCE_ID = "REMINDER_GEOFENCE_ID"
const val GEOFENCE_EXPIRATION = 10 * 24 * 60 * 60 * 1000 // 10 days
const val GEOFENCE_DWELL_DELAY = 10 * 1000 // 10 sec

private val TAG = MapActivity::class.java.simpleName

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private var mockEnable: Boolean = false //also checkbox false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map ) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        if (!isLocationPermissionGranted()) {
            val permissions = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                LOCATION_REQUEST_CODE
            )
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true

           // last data
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    with(map) {
                        val latlng = LatLng(it.latitude, it.longitude)
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, CAMERA_ZOOM_LEVEL))
                    }
                } else {
                    with(map) {
                        moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(65.059, 25.467),
                                CAMERA_ZOOM_LEVEL
                            )
                        )
                    }
                }
            }
        }

        val defStart = LatLng(65.059, 25.467)

        map.addMarker(
            MarkerOptions().position(defStart).title("Unioulu")
        )

/*        map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(defStart, CAMERA_ZOOM_LEVEL)
        )*/
        setLongClick(map)
        setPoiOnClick(map)
    }

    private fun isLocationPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == GEOFENCE_LOCATION_REQUEST_CODE) {
            if (permissions.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "This app needs background location to work on Android 10 and higher",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) ||
                (grantResults[1] == PackageManager.PERMISSION_GRANTED)
            ) {
                map.isMyLocationEnabled = true
                onMapReady(map)
            } else {
                Toast.makeText(
                    this,
                    "The app needs location permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setLongClick(googleMap: GoogleMap) {

        // set a new geofence area with locg
        map.setOnMapLongClickListener { latLng ->

            // select mode with menu
            if (mockEnable) {
                // placing virtual location...


                val mockLocation = Location("Mock")
                mockLocation.latitude = latLng.latitude
                mockLocation.longitude = latLng.longitude
                mockLocation.accuracy = 1.0f // try to add accuracy too
                mockLocation.time = System.currentTimeMillis()
                mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mockLocation.bearingAccuracyDegrees = 0.1f
                    mockLocation.verticalAccuracyMeters = 0.1f
                    mockLocation.speedAccuracyMetersPerSecond = 0.1f
                }


                Toast.makeText(
                    this,
                    "MOCK ${mockLocation.latitude} and ${mockLocation.longitude}",
                    Toast.LENGTH_SHORT
                ).show()

                fusedLocationClient.setMockLocation(mockLocation)


            } else {

                map.addMarker(
                    MarkerOptions().position(latLng)
                        .title("Current location")
                ).showInfoWindow()
                map.addCircle(
                    CircleOptions()
                        .center(latLng)
                        .strokeColor(Color.argb(50, 70, 70, 70))
                        .fillColor(Color.argb(70, 150, 150, 150))
                        .radius(GEOFENCE_RADIUS.toDouble())
                )
                // move camera
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_LEVEL))

                // push to database
                val key = "test"
                // if key != null...
                // val reminder = Reminder(key, latlng...

                //createGeofence(...
                //ReminderInfo
                createGeofence(latLng, key!!, geofencingClient)
            }
        }
    }

    private fun createGeofence(location: LatLng, key: String, geofencingClient: GeofencingClient) {
        val geofence = Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(location.latitude, location.longitude, GEOFENCE_RADIUS.toFloat())
            .setExpirationDuration(GEOFENCE_EXPIRATION.toLong())
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(GEOFENCE_DWELL_DELAY)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(this, GeofenceReceiver::class.java)
            .putExtra("key", key)
            .putExtra("message", "GeoFence!")

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    GEOFENCE_LOCATION_REQUEST_CODE
                )
            } else {
                geofencingClient.addGeofences(geofenceRequest, pendingIntent)
            }
        } else {
            geofencingClient.addGeofences(geofenceRequest, pendingIntent)
        }
    }

    private fun setPoiOnClick(googleMap: GoogleMap) {
        // when you click on a point of interest
        googleMap.setOnPoiClickListener{ pointOfInterest ->
            val poiMarker = googleMap.addMarker(
                MarkerOptions().position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
            )
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        R.id.mock_location -> {
            if (item.isChecked) { // change to not checked...
                fusedLocationClient.setMockMode(false)
                mockEnable = false
                map.isMyLocationEnabled = true
                item.isChecked = false

            } else {
                fusedLocationClient.setMockMode(true)
                mockEnable = true
                //map.isMyLocationEnabled = false
                item.isChecked = true
            }
            Toast.makeText(
                    this,
                "MOCK $mockEnable",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}