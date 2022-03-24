package com.bmd.showmemoreshops.ui.introduction.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.ui.introduction.ActivityIntroduction
import com.google.android.material.button.MaterialButton

class IntroSecondFragment : Fragment() {

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_intro_second,null,false)
        val nextButton : MaterialButton = view.findViewById(R.id.next)
        nextButton.setOnClickListener{
            ActivityIntroduction.viewPager.setCurrentItem(2,true)
        }

        return view
    }
}