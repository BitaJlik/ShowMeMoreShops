package com.bmd.showmemoreshops.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.button.MaterialButton

class AddMarkerFragment : Fragment() {

    val mainViewModel : MainViewModel by activityViewModels()

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_map_add_marker,null,false)
        val confirm : MaterialButton = view.findViewById(R.id.confirm)
        val cancel : MaterialButton = view.findViewById(R.id.cancel)

        cancel.setOnClickListener {
            removeListeners()
            activity?.onBackPressed()
            mainViewModel.dataBase.marker?.remove()
        }
        confirm.setOnClickListener {
            removeListeners()
            activity?.onBackPressed()
            val addMarketDialog = AddMarketDialog()
            addMarketDialog.show(parentFragmentManager,"AddDialog")
        }

        return view
    }
    private fun removeListeners(){
        mainViewModel.dataBase.oneView = true
        MapFragment.googleMap.setOnMapClickListener {  }
        MapFragment.googleMap.setOnMarkerDragListener(object  : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {}
            override fun onMarkerDragEnd(p0: Marker) {}
            override fun onMarkerDragStart(p0: Marker) {}
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.dataBase.oneView = true
    }
}