package com.bmd.showmemoreshops.ui.market.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.models.Category

class CategoryAdapter(private val categories: ArrayList<Category>,private val onClickListener: OnCategoryClick) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClick { fun onCategoryClick(category: Category,position: Int)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.nameCategory.text = categories[position].nameCategory
        holder.itemView.setOnClickListener { onClickListener.onCategoryClick(categories[position],position) }
    }

    override fun getItemCount(): Int { return categories.size }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameCategory: TextView = itemView.findViewById(R.id.categoryName)
    }

}