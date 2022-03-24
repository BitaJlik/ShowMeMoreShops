package com.bmd.showmemoreshops.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class MapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnGroundOverlayClickListener {
    companion object{ lateinit var googleMap : GoogleMap }
    private val mainViewModel : MainViewModel by activityViewModels()
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val geocoder = Geocoder(context)
        var address: Address? = null
        val searchView = activity?.findViewById<SearchView>(R.id.search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (mainViewModel.liveDataIsViewMap.value == true) {
                    try {
                        try {
                            address = geocoder.getFromLocationName(query, 1)[0]
                        } catch (e: IOException) {
                        }
                    }catch (e : IndexOutOfBoundsException){
                        Snackbar.make(view!!,getString(R.string.cant_find_this),Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                    }
                    if (address != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address!!.latitude, address!!.longitude), 16.0f))
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { return searchView.hasFocus() && newText.isNotEmpty() }
        })
        getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapFragment.googleMap = googleMap
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.style_json) })
        googleMap.setOnGroundOverlayClickListener(this)
        mainViewModel.dataBase.drawMarkets()

        mainViewModel.liveDataMapPosition.observe(this,{ latLng ->
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng,16f)))
        })
        mainViewModel.liveDataIsSatellite.observe(this,{ t ->
                if (t == true) { googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                } else googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            })
        mainViewModel.liveDataMoveMarket.observe( this,{
            Handler(Looper.myLooper()!!).postDelayed({
                if(it){
                    val marketLng = LatLng(mainViewModel.liveDataMarket.value!!.latitude + 0.00034, mainViewModel.liveDataMarket.value!!.longitude)
                    val markerOptions = MarkerOptions().position(marketLng)
                    markerOptions.title(mainViewModel.liveDataMarket.value!!.nameMarket)
                    googleMap.addMarker(markerOptions)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marketLng, 16.0f))
                    mainViewModel.liveDataMoveMarket.value = false
                }
            },1000)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.liveDataMapPosition.value = LatLng(googleMap.cameraPosition.target.latitude,googleMap.cameraPosition.target.longitude)
    }

    override fun onGroundOverlayClick(groundOverlay: GroundOverlay) {
        for(market in mainViewModel.dataBase.getAllMarkets()) {
            if(market.latitude == groundOverlay.position.latitude && market.longitude == groundOverlay.position.longitude){
                mainViewModel.liveDataMarket.value = market
                val dialog = MapDialog()
                dialog.show(childFragmentManager, "Dialog")
                break
            }
        }
    }

}