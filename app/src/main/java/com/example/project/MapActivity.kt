package com.example.project

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map ) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val defStart = LatLng(65.059, 25.467)
        val zoomLvl = 15f

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(defStart, zoomLvl)
        )

        map.addMarker(
            MarkerOptions().position(defStart).title("Unioulu")
        )

        setLongClick(map)
        setPoiOnClick(map)
    }

    private fun setLongClick(googleMap: GoogleMap) {
        // set a new point of interest
        googleMap.setOnMapLongClickListener {
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Lng %2$.5f",
                it.latitude,
                it.longitude
            )
            googleMap.addMarker(
                MarkerOptions().position(it)
                    .title("Been here")
                    .snippet(snippet)
            )
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
        else -> super.onOptionsItemSelected(item)
    }

}