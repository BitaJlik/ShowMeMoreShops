package com.bmd.showmemoreshops.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.data.models.Market
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import nl.joery.timerangepicker.TimeRangePicker
import java.util.*
import kotlin.collections.ArrayList

class AddMarketDialog : BottomSheetDialogFragment() {
    private val mainViewModel : MainViewModel by activityViewModels()

    @SuppressLint("StaticFieldLeak", "SimpleDateFormat","SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_map_add_market, container, false)
        val name : EditText = view.findViewById(R.id.name)
        val time : TextView = view.findViewById(R.id.time)
        val cancel : MaterialButton = view.findViewById(R.id.cancel)
        val confirm : MaterialButton = view.findViewById(R.id.confirm)
        val spinner : Spinner = view.findViewById(R.id.spinner)
        val timeRangePicker : TimeRangePicker = view.findViewById(R.id.picker)
        val imageView : ImageView = view.findViewById(R.id.image)

        var sizeMarket = 0

        val strings = ArrayList(listOf("Small", "Medium", "Large", "BIG"))
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, strings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                sizeMarket = position
                when(position){
                    0 -> imageView.setImageDrawable(AppCompatResources.getDrawable( requireContext(),R.drawable.market_small))
                    1 -> imageView.setImageDrawable(AppCompatResources.getDrawable( requireContext(),R.drawable.market_medium))
                    2 -> imageView.setImageDrawable(AppCompatResources.getDrawable( requireContext(),R.drawable.market_big))
                    3 -> imageView.setImageDrawable(AppCompatResources.getDrawable( requireContext(),R.drawable.market_large))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { sizeMarket = 0 } }
        time.text = "9:00  -  22:00"
        timeRangePicker.setOnTimeChangeListener(object  : TimeRangePicker.OnTimeChangeListener {
            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                time.text = "${timeRangePicker.startTime}  -  ${timeRangePicker.endTime}"
            }
            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                time.text = "${timeRangePicker.startTime}  -  ${timeRangePicker.endTime}"
            }
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                time.text = "${timeRangePicker.startTime}  -  ${timeRangePicker.endTime}"
            }
        })
        cancel.setOnClickListener { dialog?.dismiss() ; mainViewModel.dataBase.marker?.remove() }
        confirm.setOnClickListener {
            val market = Market()
            market.nameMarket = name.text.toString()
            market.latitude = mainViewModel.dataBase.marker?.position?.latitude!!
            market.longitude = mainViewModel.dataBase.marker?.position?.longitude!!
            market.email = mainViewModel.liveDataUser.value?.email!!
            market.openTime = time.text.toString()
            market.owner = mainViewModel.liveDataUser.value?.name!!
            market.userUUID = mainViewModel.liveDataUser.value?.UUID!!
            market.marketUUID = UUID.randomUUID().toString()
            market.sizeMarket = when (sizeMarket) {
                1 ->  Market.SizeMarket.MEDIUM
                2 ->  Market.SizeMarket.LARGE
                3 ->  Market.SizeMarket.BIG
                else ->  Market.SizeMarket.SMALL }

            if(check(name)){
                mainViewModel.liveDataUser.value?.markets?.set(market.marketUUID, market)
                dismiss()
                mainViewModel.dataBase.updateUserDB()
            }
        }

        return view
    }
    private fun check(name : EditText) : Boolean {
        if(name.toString().isEmpty()) {
            name.error = getString(R.string.input_name)
            return false
        }
        if(name.toString().length < 3) {
            Snackbar.make(requireView() ,getString(R.string.input_name) ,Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED

            behaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.dataBase.marker?.remove()
    }
}