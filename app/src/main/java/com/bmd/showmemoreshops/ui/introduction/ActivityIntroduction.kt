package com.bmd.showmemoreshops.ui.introduction

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.databinding.ActivityIntroBinding
import com.bmd.showmemoreshops.ui.introduction.fragments.IntroSecondFragment
import com.bmd.showmemoreshops.ui.introduction.fragments.IntroThirdFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ActivityIntroduction : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    companion object {
        lateinit var viewPager: ViewPager2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = findViewById(R.id.viewPager)
        val tabLayout : TabLayout = findViewById(R.id.tabLayout)
        val adapterViewPager = PagerAdapter(this)
        viewPager.adapter = adapterViewPager

        TabLayoutMediator(tabLayout,viewPager,true){ tab, position ->
            tab.text = position.toString()
        }.attach()
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
                val colorFrom: Int = tabLayout.solidColor
                val colors = intArrayOf(Color.parseColor("#ff0000"), Color.parseColor("#002eff"), Color.parseColor("#46b904"))
                val colorTo = colors[position]
                val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
                colorAnimation.duration = 300
                colorAnimation.addUpdateListener { animator: ValueAnimator ->
                    tabLayout.setBackgroundColor(animator.animatedValue as Int)
                    window.navigationBarColor = animator.animatedValue as Int
                    window.statusBarColor = animator.animatedValue as Int
                }
                colorAnimation.start()
            }
        })

    }

    private class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity){

        override fun getItemCount(): Int { return 3 }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> IntroFirstFragment()
                1 -> IntroSecondFragment()
                2 -> IntroThirdFragment()
                else -> IntroFirstFragment()
            }
        }
    }
    class IntroFirstFragment : Fragment() {

        @SuppressLint("InflateParams")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_intro_first,null,false)
            val nextButton : MaterialButton = view.findViewById(R.id.next)
            nextButton.setOnClickListener{
                viewPager.setCurrentItem(1,true)
            }
            return view
        }
    }
}