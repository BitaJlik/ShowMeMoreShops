package com.bmd.showmemoreshops.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.google.android.material.button.MaterialButton

class CommentEditFragment : Fragment() {

    val mainViewModel : MainViewModel by activityViewModels()

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_comment_edit,null,false)
        val comment : TextView = view.findViewById(R.id.comment)
        val save : MaterialButton = view.findViewById(R.id.save)
        val cancel : MaterialButton = view.findViewById(R.id.cancel)
        mainViewModel.liveDataComment.observe(this,{ comment.text = it.comment })

        save.setOnClickListener {
            mainViewModel.liveDataComment.value?.comment = comment.text.toString()
            if(mainViewModel.liveDataIsEditComment.value == false){
                mainViewModel.liveDataMarket.value?.comments?.put(mainViewModel.liveDataComment.value?.commentUUID!!,mainViewModel.liveDataComment.value!!)
            }
            mainViewModel.dataBase.updateCommentDB()
            activity?.onBackPressed()
        }
        cancel.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

}