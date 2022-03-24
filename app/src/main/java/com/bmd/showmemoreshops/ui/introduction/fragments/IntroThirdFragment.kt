package com.bmd.showmemoreshops.ui.introduction.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bmd.showmemoreshops.R
import com.google.android.material.button.MaterialButton

class IntroThirdFragment : Fragment() {

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_intro_third,null,false)
        val nextButton : MaterialButton = view.findViewById(R.id.next)
        nextButton.setOnClickListener{
            activity?.finish()
        }
        return view
    }

}