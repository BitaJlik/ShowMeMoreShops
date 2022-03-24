package com.bmd.showmemoreshops.ui.market.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.bmd.showmemoreshops.data.models.Category
import com.bmd.showmemoreshops.ui.market.category.edit.EditCategoryListFragment
import com.bmd.showmemoreshops.ui.market.product.ProductListFragment
import com.google.android.material.button.MaterialButton

class CategoryListFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: CategoryAdapter
    private lateinit var listener: CategoryAdapter.OnCategoryClick
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_category_list, null, false)
        val marketName: TextView = view.findViewById(R.id.nameMarket)
        val edit: MaterialButton = view.findViewById(R.id.edit)
        val emptyText : TextView = view.findViewById(R.id.emptyText)
        val layoutManager = LinearLayoutManager(context)

        edit.setOnClickListener { edit() }
        if (mainViewModel.liveDataMarket.value!!.email == mainViewModel.liveDataUser.value!!.email )
        { edit.visibility = View.VISIBLE } else { edit.visibility = View.GONE }

        listener = object : CategoryAdapter.OnCategoryClick {
            override fun onCategoryClick(category: Category, position: Int) {
                Log.i("Category", "Creating fragment ${category.nameCategory}")
                mainViewModel.liveDataCategory.value = category
                parentFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                    .replace(R.id.fragment, ProductListFragment())
                    .addToBackStack("ProductList").commit()
            }
        }

        recyclerView = view.findViewById(R.id.rList)
        adapter = CategoryAdapter(ArrayList(mainViewModel.liveDataMarket.value!!.categories.values), listener)
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager


        mainViewModel.liveDataMarket.observe(this,{
            val parcelable =  layoutManager.onSaveInstanceState()
            recyclerView.adapter = CategoryAdapter(ArrayList(it.categories.values), listener)
            layoutManager.onRestoreInstanceState(parcelable)
            if(it.categories.size == 0){ emptyText.visibility = View.VISIBLE } else{ emptyText.visibility = View.GONE }
            marketName.text = mainViewModel.liveDataMarket.value!!.nameMarket
        })
        return view
    }

    fun edit() {
        Log.i("Category", "Creating editing fragment  ")
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
            .replace(R.id.fragment, EditCategoryListFragment())
            .addToBackStack("EditCategories").commit()
    }

}