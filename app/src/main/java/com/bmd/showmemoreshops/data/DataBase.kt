package com.bmd.showmemoreshops.data

import android.util.Log
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.models.Market
import com.bmd.showmemoreshops.data.models.User
import com.bmd.showmemoreshops.ui.map.MapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlin.collections.set

class DataBase(private val mainViewModel: MainViewModel,var isReady: Boolean = false) {
    val debugMode = false
    // if true all buttons are visible !!!

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.getReference("Users")
    var oneView = true
    var marker : Marker? = null
    init {

        val postListener : ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mainViewModel.liveDataUsers.value!!.clear()
                    for (snapshotUser in dataSnapshot.children) { // UiD User
                        val user = snapshotUser.getValue<User>() ?: break
                        mainViewModel.liveDataUsers.value!![user.UUID] = user // adding in LOCAL DB
                        val markets = HashMap<String,Market>() // Markets for user
                        // getting from ONLINE DB from user markets
                        for (dataMarket in snapshotUser.child("markets").children) {
                            val market = dataMarket.getValue<Market>()
                            if (market != null) { markets[market.marketUUID] = (market) }
                        }
                        user.markets = markets
                    }

                    reloadInfo()
                    drawMarkets()
                    if(debugMode){
                        auth.signInWithEmailAndPassword("vtkach4488@gmail.com","qweqwe")
                        mainViewModel.liveDataUser.value = mainViewModel.liveDataUsers.value!![auth.uid]
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("DATABASE", "loadPost:onCancelled", databaseError.toException())
                }
            }
        reference.addValueEventListener(postListener)
        isReady = true
    }

    fun updateUserDB() {
        val uid = auth.currentUser?.uid ?: return
        val data: MutableMap<String, Any> = HashMap()
        data[uid] = mainViewModel.liveDataUser.value!!
        reference.updateChildren(data)
    }
    fun updateCommentDB(
        keyUser : String = mainViewModel.liveDataMarket.value!!.userUUID,
        keyMarket: String = mainViewModel.liveDataMarket.value!!.marketUUID,
        keyComment : String = mainViewModel.liveDataComment.value!!.commentUUID)
    {
        val comment = mainViewModel.liveDataUsers.value?.get(keyUser)?.markets?.get(keyMarket)?.comments?.get(keyComment)
        if(comment != null) {
            val data: MutableMap<String, Any> = HashMap()
            data[keyComment] = comment
            reference.child(keyUser)
                .child("markets").child(keyMarket)
                .child("comments").updateChildren(data)
        }
    }

    fun removeCommentDB(
        keyUser : String = mainViewModel.liveDataMarket.value!!.userUUID,
        keyMarket: String = mainViewModel.liveDataMarket.value!!.marketUUID,
        keyComment : String = mainViewModel.liveDataComment.value!!.commentUUID)
    {
        reference.child(keyUser)
            .child("markets").child(keyMarket)
            .child("comments").child(keyComment).removeValue()
    }

    fun drawMarkets() {
        MapFragment.googleMap.clear()
        for (market in getAllMarkets()) {
            when (market.sizeMarket) {
                Market.SizeMarket.SMALL -> {
                    MapFragment.googleMap.addGroundOverlay(
                        GroundOverlayOptions().position(
                            LatLng(market.latitude, market.longitude), 60f, 60f)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.market_small))
                            .clickable(true)
                    )
                }
                Market.SizeMarket.MEDIUM -> {
                    MapFragment.googleMap.addGroundOverlay(
                        GroundOverlayOptions().position(
                            LatLng(market.latitude, market.longitude),
                            75f,
                            75f
                        )
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.market_medium))
                            .clickable(true)
                    )
                }
                Market.SizeMarket.LARGE -> {
                    MapFragment.googleMap.addGroundOverlay(
                        GroundOverlayOptions().position(
                            LatLng(market.latitude, market.longitude),
                            100f,
                            100f
                        )
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.market_large))
                            .clickable(true)
                    )
                }
                Market.SizeMarket.BIG -> {
                    MapFragment.googleMap.addGroundOverlay(
                        GroundOverlayOptions().position(
                            LatLng(market.latitude, market.longitude),
                            120f,
                            120f
                        )
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.market_big))
                            .clickable(true)
                    )
                }
            }
        }
    }
    fun reloadInfo() {

        for(market in getAllMarkets()){
            if(market.marketUUID == mainViewModel.liveDataMarket.value!!.marketUUID){
                mainViewModel.liveDataMarket.value = market

                for(category in mainViewModel.liveDataMarket.value!!.categories){
                    if(category.value.categoryUUID == mainViewModel.liveDataCategory.value!!.categoryUUID ){
                            mainViewModel.liveDataCategory.value = category.value

                        for(product in category.value.products){
                            if(product.value.productUUID == mainViewModel.liveDataProduct.value!!.productUUID){
                                mainViewModel.liveDataProduct.value = product.value
                                break // product
                            }
                        }
                        break // category
                    }
                }
                break // market
            }
        }
    }
    fun getAllMarkets() : ArrayList<Market> {
        val markets = ArrayList<Market>()
        for (user in mainViewModel.liveDataUsers.value!!) {
            if (user.value.markets.size > 0) {
                for(market in user.value.markets.values){
                    markets.add(market)
                }
            }
        }
        return markets
    }


}
