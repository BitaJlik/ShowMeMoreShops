package com.bmd.showmemoreshops.ui.market.allmarkets

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.models.Market

class MarketAdapter(private val markets : ArrayList<Market>,val onMarketClick: OnMarketClick) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>(), Filterable {
    interface OnMarketClick { fun onClick(market: Market,position: Int)}
    private val filteredMarket = ArrayList<Market>(markets)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_market, parent, false)
        return MarketViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) { holder.bind(markets[position]) }
    override fun getItemCount(): Int { return markets.size }

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val name : TextView = itemView.findViewById(R.id.name)
        private val time : TextView = itemView.findViewById(R.id.time)

        fun bind(market: Market){
            name.text = market.nameMarket
            time.text = market.openTime
            itemView.setOnClickListener{onMarketClick.onClick(markets[adapterPosition],adapterPosition)}
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filtered: MutableList<Market> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filtered.addAll(filteredMarket)
            } else {
                val stringPattern = constraint.toString().lowercase().trim { it <= ' ' }
                for (market in filteredMarket) {
                    if (market.nameMarket.lowercase().contains(stringPattern)) {
                        filtered.add(market)
                    }
                }
            }
            val results = FilterResults()
            results.values = filtered
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            markets.clear()
            markets.addAll(results.values as List<Market>)
            notifyDataSetChanged()
        }
    }
}

