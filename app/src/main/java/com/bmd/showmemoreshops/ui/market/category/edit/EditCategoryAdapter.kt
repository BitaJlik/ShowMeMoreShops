package com.bmd.showmemoreshops.ui.market.category.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.models.Category

class EditCategoryAdapter(private val categories : ArrayList<Category>, private val categoryClickListener: CategoryClickListener) : RecyclerView.Adapter<EditCategoryAdapter.ViewHolder>() {
    interface CategoryClickListener { fun onClick(category: Category,position: Int) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameCategory.text = categories[position].nameCategory
        holder.itemView.setOnClickListener { categoryClickListener.onClick(categories[position], position) }
    }

    override fun getItemCount(): Int { return categories.size }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameCategory: TextView = view.findViewById(R.id.categoryName)
    }
}