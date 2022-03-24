package com.bmd.showmemoreshops.ui.market.product

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.models.Currency
import com.bmd.showmemoreshops.data.models.Product
import java.text.DecimalFormat

class ProductAdapter(private val products: ArrayList<Product>, val onContextMenu: OnContextMenu,val currency: Currency,val adminUser : Boolean) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    interface OnContextMenu { fun onRemove(key : String ,position: Int) fun onEdit(key : String ,position: Int)  }

    override fun getItemViewType(position: Int): Int {
        // TODO: 05.03.2022 make viewholder for disount prducts
        return position % 2 * 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) { holder.bind(products[position]) }

    override fun getItemCount(): Int { return products.size }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener  {
        var nameProduct: TextView = itemView.findViewById(R.id.productName)
        var priceProduct: TextView = itemView.findViewById(R.id.productPrice)
        var amountProduct: TextView = itemView.findViewById(R.id.productAmount)
        private lateinit var key : String
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            nameProduct.text = product.nameProduct.trim()
            if (product.isDiscount) {
                priceProduct.setTextColor(Color.parseColor( "#FFF816"))
                priceProduct.typeface = Typeface.DEFAULT_BOLD
            }
            val decimalFormat = DecimalFormat("#0.00")
            val price = decimalFormat.format(product.price / currency.rate)

            priceProduct.text = price + currency.shortName
            amountProduct.text = product.amount.toString() + "шт"
            key = product.productUUID
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if(adminUser){
                menu?.add(0, v!!.id, 0, "Edit")
                    ?.setOnMenuItemClickListener {
                        onContextMenu.onEdit(key,adapterPosition)
                        true
                    }
                menu?.add(0, v!!.id, 0, "Remove")
                    ?.setOnMenuItemClickListener {
                        onContextMenu.onRemove(key, adapterPosition)
                        true
                    }
            }
        }

    }
}

