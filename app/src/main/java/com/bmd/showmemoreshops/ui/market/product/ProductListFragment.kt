package com.bmd.showmemoreshops.ui.market.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.data.models.Product
import com.bmd.showmemoreshops.ui.market.product.edit.EditProductDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class ProductListFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var listener : ProductAdapter.OnContextMenu
    private lateinit var products : ArrayList<Product>
    private var adminUser : Boolean = false
    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, null, false)
        val emptyText : TextView = view.findViewById(R.id.emptyText)
        val sortButton: MaterialButton = view.findViewById(R.id.sort)
        val addButton: MaterialButton = view.findViewById(R.id.nav_add)
        val categoryName: TextView = view.findViewById(R.id.categoryName)

        adminUser = mainViewModel.liveDataMarket.value!!.email == mainViewModel.liveDataUser.value!!.email
        sortButton.setOnClickListener { sort(sortButton) }
        addButton.setOnClickListener { add() }

        listener = object : ProductAdapter.OnContextMenu {
            override fun onRemove(key : String,position: Int) { remove(key,position) }
            override fun onEdit(key : String, position: Int) { edit(key) }
        }
        if (adminUser) addButton.visibility = View.VISIBLE  else addButton.visibility = View.GONE

        products = ArrayList(mainViewModel.liveDataCategory.value!!.products.values)
        products.sortWith(compareBy { it.nameProduct })

        recyclerView = view.findViewById(R.id.recyclerView)
        products.sortWith(compareBy { it.nameProduct })
        adapter = ProductAdapter(products,listener, mainViewModel.liveDataCurrency.value!! ,adminUser)
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        mainViewModel.liveDataCategory.observe(this, { category ->
            products = ArrayList(mainViewModel.liveDataCategory.value!!.products.values)
            products.sortWith(compareBy { it.nameProduct })
            categoryName.text = category.nameCategory
            setupRecyclerView()
            if(mainViewModel.liveDataCategory.value!!.products.size == 0){ emptyText.visibility = View.VISIBLE } else { emptyText.visibility = View.GONE }
        })

        return view
    }

    private var typeSort = 1
    @SuppressLint("NotifyDataSetChanged")
    private fun sort(sort: MaterialButton) {
        typeSort++
        when (typeSort) {
            1 -> {
                products.sortWith(compareBy { it.nameProduct })
                sort.text = context?.getString(R.string.name)
            }
            2 -> {
                products.sortWith(compareBy { it.price })
                sort.text = context?.getString(R.string.price)
            }
            3 -> {
                typeSort = 0
                products.sortWith(compareBy { it.amount })
                sort.text = context?.getString(R.string.amount)
            }
        }
        setupRecyclerView()
    }

    private fun add(){
        val product = Product()
        mainViewModel.liveDataCategory.value!!.products[product.productUUID] = product
        mainViewModel.dataBase.updateUserDB()
        setupRecyclerView()

        Snackbar.make(view!!,getString(R.string.hold_for_more),Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }
    private fun remove(key : String ,position : Int){
        val parcelable = recyclerView.layoutManager!!.onSaveInstanceState()
        mainViewModel.liveDataCategory.value!!.products.remove(key)
        adapter.notifyItemRemoved(position)
        recyclerView.adapter = ProductAdapter(products,listener, mainViewModel.liveDataCurrency.value!! ,adminUser)
        recyclerView.layoutManager!!.onRestoreInstanceState(parcelable)
        mainViewModel.dataBase.updateUserDB()
    }
    private fun edit(key: String){
        mainViewModel.liveDataProduct.value = mainViewModel.liveDataCategory.value!!.products[key]
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
            .replace(R.id.fragment, EditProductDialog())
            .addToBackStack("EditCategories").commit()
    }

    private fun setupRecyclerView() {
        val parcelable = recyclerView.layoutManager!!.onSaveInstanceState()
        adapter = ProductAdapter(products ,listener, mainViewModel.liveDataCurrency.value!! ,adminUser)
        recyclerView.adapter = adapter
        recyclerView.layoutManager!!.onRestoreInstanceState(parcelable)
    }

}