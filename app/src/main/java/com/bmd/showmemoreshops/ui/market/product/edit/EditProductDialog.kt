package com.bmd.showmemoreshops.ui.market.product.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.google.android.material.button.MaterialButton

class EditProductDialog : Fragment() {
    private val mainViewModel : MainViewModel by activityViewModels()

    @SuppressLint("StaticFieldLeak", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_product_edit, container, false)

        val name : EditText = view.findViewById(R.id.name)
        val price : EditText = view.findViewById(R.id.price)
        val amount : EditText = view.findViewById(R.id.amount)
        val discount : SwitchCompat = view.findViewById(R.id.discount)
        val cancel : MaterialButton = view.findViewById(R.id.cancel)
        val confirm : MaterialButton = view.findViewById(R.id.confirm)

        cancel.setOnClickListener { cancel() }
        confirm.setOnClickListener { confirm(name, price, amount, discount)}

        mainViewModel.liveDataProduct.observe(this, {

            name.setText(it.nameProduct)
            price.setText(it.price.toString())
            amount.setText(it.amount.toString())
            discount.isChecked = it.isDiscount

        })

        return view
    }

    private fun cancel(){ activity?.onBackPressed() }

    private fun confirm(name : EditText, price : EditText, amount: EditText,discount : SwitchCompat){

        mainViewModel.liveDataProduct.value!!.nameProduct = name.text.toString()
        mainViewModel.liveDataProduct.value!!.price = price.text.toString().toDouble()
        mainViewModel.liveDataProduct.value!!.amount = amount.text.toString().toInt()
        mainViewModel.liveDataProduct.value!!.isDiscount = discount.isChecked

        mainViewModel.dataBase.updateUserDB()

        activity?.onBackPressed()
    }
}