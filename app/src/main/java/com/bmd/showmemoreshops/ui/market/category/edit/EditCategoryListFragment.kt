package com.bmd.showmemoreshops.ui.market.category.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.data.models.Category
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class EditCategoryListFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var category : Category
    private lateinit var listener : EditCategoryAdapter.CategoryClickListener
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : EditCategoryAdapter
    private var position = 0
    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_category_list_edit, null, false)
        val categoryName: EditText = view.findViewById(R.id.categoryName)
        val add: MaterialButton = view.findViewById(R.id.nav_add)
        val remove: MaterialButton = view.findViewById(R.id.remove)
        val edit: MaterialButton = view.findViewById(R.id.edit)

        listener =  object : EditCategoryAdapter.CategoryClickListener {
            override fun onClick(category: Category, position: Int) {
                categoryName.setText(category.nameCategory)
                this@EditCategoryListFragment.category = category
                this@EditCategoryListFragment.position = position
            }
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        adapter = EditCategoryAdapter(ArrayList(mainViewModel.liveDataMarket.value!!.categories.values),listener)
        recyclerView.adapter = adapter
        add.setOnClickListener { add(categoryName, adapter) }
        remove.setOnClickListener { remove(adapter) }
        edit.setOnClickListener { edit(categoryName,adapter) }

        mainViewModel.liveDataMarket.observe(this,{
            val parcelable = recyclerView.layoutManager!!.onSaveInstanceState()
            recyclerView.adapter = EditCategoryAdapter(ArrayList(mainViewModel.liveDataMarket.value!!.categories.values),listener)
            recyclerView.layoutManager!!.onRestoreInstanceState(parcelable)
        })

        return view
    }


    private fun add(editText: EditText,adapter: EditCategoryAdapter) {
        if(editText.text.toString().length < 5) {
            val imm = context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            Snackbar.make(view!!,getString(R.string.short_name), Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
            return
        }
        val category = Category(nameCategory = "${editText.text}")
        mainViewModel.liveDataMarket.value!!.categories[category.categoryUUID] = category
        adapter.notifyItemInserted(mainViewModel.liveDataMarket.value!!.categories.size)
        mainViewModel.dataBase.updateUserDB()
    }
    private fun remove(adapter: EditCategoryAdapter) {
        mainViewModel.liveDataMarket.value!!.categories.remove(category.categoryUUID)
        adapter.notifyItemRemoved(position)
        this.adapter = EditCategoryAdapter(ArrayList(mainViewModel.liveDataMarket.value!!.categories.values),listener)
        recyclerView.adapter = this.adapter
        mainViewModel.dataBase.updateUserDB()
    }
    private fun edit(editText: EditText,adapter: EditCategoryAdapter) {
        category.nameCategory = editText.text.toString()
        adapter.notifyItemChanged(position)
        mainViewModel.dataBase.updateUserDB()
    }


}