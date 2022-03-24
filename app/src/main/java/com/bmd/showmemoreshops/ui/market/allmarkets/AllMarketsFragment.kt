package com.bmd.showmemoreshops.ui.market.allmarkets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.data.models.Market
import com.bmd.showmemoreshops.ui.map.MapFragment

class AllMarketsFragment : Fragment() {

    private val mainViewModel : MainViewModel by activityViewModels()
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val view : View = inflater.inflate(R.layout.fragment_all_markets,null,false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView)
        val listener = object : MarketAdapter.OnMarketClick{
            override fun onClick(market: Market, position: Int) {
                val fragment = MapFragment()
                parentFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment, fragment)
                    .commit()
                mainViewModel.liveDataMarket.value = market
                mainViewModel.liveDataMoveMarket.value = true
                mainViewModel.liveDataIsViewMap.value = true
            }
        }
        var adapter = MarketAdapter(mainViewModel.dataBase.getAllMarkets(),listener)
        val searchView = activity?.findViewById<SearchView>(R.id.search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { return searchView.hasFocus() && newText.isNotEmpty() }
        })

        mainViewModel.liveDataUsers.observe(this, {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
            adapter = MarketAdapter(mainViewModel.dataBase.getAllMarkets(),listener)
            recyclerView.adapter = adapter
        })
        return view
    }

}