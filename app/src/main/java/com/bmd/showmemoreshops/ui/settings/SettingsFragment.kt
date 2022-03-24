package com.bmd.showmemoreshops.ui.settings

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.ui.settings.currency.CurrencyBlock
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.util.*

class SettingsFragment : Fragment() {
    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var currencyBlock : CurrencyBlock
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_settings, null, false)
        val darkThemeSwitch: SwitchCompat = view.findViewById(R.id.dark_switch)
        val aboutDev: MaterialButton = view.findViewById(com.bmd.showmemoreshops.R.id.aboutDev)
        val spinner : AppCompatSpinner = view.findViewById(R.id.spinner)
        mainViewModel.liveDataCurrencyBlock.observe(this,{ cb ->
            currencyBlock = cb
            val data = ArrayList<Map<String, String>>()
            (0 until currencyBlock.currencyList.size).forEach {
                val datum: MutableMap<String, String> = HashMap(2)
                datum["First"] = currencyBlock.currencyList[it].name
                datum["Second"] = "1${currencyBlock.currencyList[it].shortName} = ${currencyBlock.currencyList[it].rate}UAH"
                data.add(datum)
            }
            val adapter = SimpleAdapter(requireContext(), data, R.layout.item_currency,
                arrayOf("First", "Second"),
                intArrayOf(R.id.text1, R.id.text2)  )
            spinner.adapter = adapter
        })
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                mainViewModel.liveDataCurrency.value = currencyBlock.currencyList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                mainViewModel.liveDataCurrency.value = currencyBlock.currencyList[0]
            }
        }
        darkThemeSwitch.isChecked = mainViewModel.liveDataTheme.value!!
        darkThemeSwitch.setOnCheckedChangeListener { _: CompoundButton?, _: Boolean ->
            mainViewModel.liveDataTheme.value = !mainViewModel.liveDataTheme.value!!
            mainViewModel.liveDataToSettings.value = true
            if (mainViewModel.liveDataTheme.value == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        aboutDev.setOnClickListener {
            val dialogDev = BottomAboutDev()
            dialogDev.show(childFragmentManager, "aboutDev")
        }

        return view
    }



    class BottomAboutDev : BottomSheetDialogFragment() {
        @SuppressLint("StaticFieldLeak", "SetTextI18n")
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val view: View = inflater.inflate(
                R.layout.fragment_settings_devs,
                container,
                false
            )
            val aboutBitaJlik: MaterialTextView =
                view.findViewById(R.id.about_BitaJlik)
            aboutBitaJlik.text = "Vitali Tkach \n Main developer\n Tech lead"
            val aboutMaxim: MaterialTextView =
                view.findViewById(R.id.about_Max)
            aboutMaxim.text = "Maxim Zaharchuk \n Main  "
            val aboutDenis: MaterialTextView =
                view.findViewById(R.id.about_Denis)
            aboutDenis.text = "Denis Sayetskiy \n Support developer"
            view.setOnClickListener { dialog?.hide() }
            dialog?.setOnShowListener() { dialog: DialogInterface ->
                val d = dialog as BottomSheetDialog
                val bottomSheetInternal = d.findViewById<View>(R.id.design_bottom_sheet)!!
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED)
            }
            return view
        }
    }

}