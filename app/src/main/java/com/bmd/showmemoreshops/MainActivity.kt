package com.bmd.showmemoreshops

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.databinding.ActivityMainBinding
import com.bmd.showmemoreshops.ui.auth.AuthFragment
import com.bmd.showmemoreshops.ui.introduction.ActivityIntroduction
import com.bmd.showmemoreshops.ui.map.AddMarkerFragment
import com.bmd.showmemoreshops.ui.map.MapFragment
import com.bmd.showmemoreshops.ui.market.allmarkets.AllMarketsFragment
import com.bmd.showmemoreshops.ui.settings.SettingsFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var search: SearchView
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("introduction", false)) {
            startActivity(Intent(this, ActivityIntroduction::class.java))
            val editor = sharedPreferences.edit()
            editor.putBoolean("introduction", java.lang.Boolean.TRUE)
            editor.apply()
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        search = toolbar.findViewById(R.id.search)
        // Navigation header
        val switchCompat: SwitchCompat = navigationView.getHeaderView(0).findViewById(R.id.satellite)
        switchCompat.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.liveDataIsSatellite.value = isChecked
            if (switchCompat.isChecked) {
                MapFragment.googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            } else MapFragment.googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        mainViewModel.liveDataUser.observe(this , {
            val textViewName : TextView = navigationView.getHeaderView(0).findViewById(R.id.name)
            val textViewEmail : TextView = navigationView.getHeaderView(0).findViewById(R.id.email)
            textViewName.text = it.name
            textViewEmail.text = it.email
        })

        //
        setSupportActionBar(toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                search.onActionViewCollapsed()
                imm.hideSoftInputFromWindow(navigationView.windowToken, 0)
            }
        })
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        navigationView.setCheckedItem(R.id.nav_map)
        navigationView.setNavigationItemSelectedListener(this)
        actionBarDrawerToggle.syncState()
        onNavigationItemSelected(navigationView.menu.findItem(R.id.nav_map))



        // Temporary block (im hope)
        if (mainViewModel.liveDataToSettings.value == true) {
            changeFragment(SettingsFragment())
            mainViewModel.liveDataToSettings.value = false
        }
        // Theming block, changing colors
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> mainViewModel.liveDataTheme.value = true
            Configuration.UI_MODE_NIGHT_NO -> mainViewModel.liveDataTheme.value = false
        }
    }
    // Navigation block
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.nav_map -> { changeFragment(MapFragment()) }
            R.id.nav_all -> { changeFragment(AllMarketsFragment()) }
            R.id.nav_login -> { changeFragment(AuthFragment()) }
            R.id.nav_add -> { addMarket() }
            R.id.nav_location -> if (!mainViewModel.liveDataIsViewMap.value!!) {
                Snackbar.make(navigationView, resources.getText(R.string.switch_map), Snackbar.LENGTH_LONG).show()
            }
            else {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        { getLastKnownLocation() } else askLocationPermission()
            }
            R.id.nav_settings -> { changeFragment(SettingsFragment()) }
            R.id.nav_exit -> { finish() }
        }

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        search.onActionViewCollapsed()
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        return true
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment, fragment)
            .commit()
        mainViewModel.liveDataIsViewMap.value = fragment is MapFragment
    }
    // Adding market block
    private fun addMarket(){
        if(mainViewModel.liveDataIsViewMap.value == true) {
            if(mainViewModel.liveDataUser.value?.email != "Email"){
                MapFragment.googleMap.setOnMapClickListener {
                    if(mainViewModel.dataBase.marker != null) mainViewModel.dataBase.marker?.remove()
                    mainViewModel.dataBase.marker = MapFragment.googleMap.addMarker(MarkerOptions().position(it).draggable(true))!!
                    if(mainViewModel.dataBase.oneView){
                        supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.fragment, AddMarkerFragment()).addToBackStack("AddMarket")
                            .commit()
                        mainViewModel.dataBase.oneView = false
                    }
                }
                MapFragment.googleMap.setOnMarkerDragListener(object  : GoogleMap.OnMarkerDragListener {
                    override fun onMarkerDrag(p0: Marker) {}
                    override fun onMarkerDragEnd(p0: Marker) {}
                    override fun onMarkerDragStart(p0: Marker) { showSnack(navigationView,getString(R.string.hold_and_drag)) }
                })
                showSnack(navigationView,getString(R.string.put_marker_on_map))
            }
            else { showSnack(navigationView,getString(R.string.please_login)) }
        }
        else { showSnack(navigationView,getString(R.string.switch_map)) }
    }

    // Location block
    private fun getLastKnownLocation()  {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) { return }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    MapFragment.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition(LatLng(location.latitude,location.longitude),16f,0f,0f)))
                }
                else {
                    Snackbar.make(this.navigationView, resources.getText(R.string.gps_error), Snackbar.LENGTH_LONG).show()
                }
            }
    }
    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(this.navigationView.rootView, resources.getText(R.string.gps_error), Snackbar.LENGTH_LONG).show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10001)
        }
    }

    // Other methods
    fun showSnack(view: View, string : String){
        Snackbar.make(view,string,Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }

    // if drawerLayout is open we close it or exit app
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }
}