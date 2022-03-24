package com.bmd.showmemoreshops.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bmd.showmemoreshops.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AuthFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        super.onCreate(savedInstanceState)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabLayout : TabLayout = view.findViewById(R.id.tabLayout)
        val adapterViewPager = PagerAdapter(this)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            when (position) {
                0 -> { tab.text = getString(R.string.login) }
                1 -> { tab.text = getString(R.string.register) } } }.attach()




        return view
    }

    private class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> LoginFragment()
                1 -> RegisterFragment()
                else -> RegisterFragment()
            }
        }
    }

}