package com.bmd.showmemoreshops.ui.map

import android.graphics.Color
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.models.Comment

class CommentAdapter(private val comments: ArrayList<Comment>,private val onContextMenu: OnContextMenu)
    : RecyclerView.Adapter<CommentAdapter.ProductHolder>() {
    
    interface OnContextMenu {
        fun edit(key : String,position: Int)
        fun remove(key : String, position: Int)
        fun like(key : String,position: Int)
        fun warn(key : String,position: Int)
        fun check(key : String,position: Int) : Boolean
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ProductHolder(v)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) { holder.bind(comments[position]) }

    override fun getItemCount(): Int { return comments.size }

    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnCreateContextMenuListener {
        var name: TextView = itemView.findViewById(R.id.item_user)
        var comment: TextView = itemView.findViewById(R.id.item_comment)
        var date: TextView = itemView.findViewById(R.id.item_time)
        lateinit var text: String
        lateinit var key : String

        fun bind(comment: Comment) {
            name.text = comment.user
            this.comment.text = comment.comment
            date.text = comment.date
            if (comment.warns >= 3) {
                name.setTextColor(Color.GREEN)
            } else if (comment.warns <= -3) {
                name.setTextColor(Color.parseColor("#FF4C4C"))
            }
            key = comment.commentUUID
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
                if(onContextMenu.check(key, adapterPosition)){
                    menu!!.add(0, v!!.id, 0, "Edit")
                        .setOnMenuItemClickListener {
                            onContextMenu.edit(key,adapterPosition)
                            true
                        }
                    menu.add(0, v.id, 0, "Remove")
                        .setOnMenuItemClickListener {
                            onContextMenu.remove(key,adapterPosition)
                            true
                        }
                }
                menu!!.add(0, v!!.id, 0, "Like")
                    .setOnMenuItemClickListener {
                        onContextMenu.like(key,adapterPosition)
                        true
                }
                menu.add(0, v.id, 0, "Warn")
                    ?.setOnMenuItemClickListener {
                        onContextMenu.warn(key,adapterPosition)
                        true
                    }
            }
    }
}