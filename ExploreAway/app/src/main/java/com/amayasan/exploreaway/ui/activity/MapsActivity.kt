package com.amayasan.exploreaway.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amayasan.exploreaway.AppConstants
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.model.Model

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // TODO: Move venues into ViewModel
    private var venues: List<Model.Venue> = ArrayList<Model.Venue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_activity)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        venues = intent.getParcelableArrayListExtra(AppConstants.VENUES_LIST_KEY)
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

        // Zoom map to Downtown Seattle
        val seattle = LatLng(AppConstants.DOWNTOWN_SEATTLE_LAT, AppConstants.DOWNTOWN_SEATTLE_LNG)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seattle, 13.0f))

        addVenueMarkers()
    }

    private fun addVenueMarkers() {
        for (venue in venues) {
            mMap.addMarker(MarkerOptions().position(venue.location.getLatLng()).title(venue.name))
        }
    }
}
