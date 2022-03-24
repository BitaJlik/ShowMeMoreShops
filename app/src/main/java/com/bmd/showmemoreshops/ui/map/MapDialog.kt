package com.bmd.showmemoreshops.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.data.models.Comment
import com.bmd.showmemoreshops.ui.market.category.CategoryListFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MapDialog : BottomSheetDialogFragment() {
    private val mainViewModel : MainViewModel by activityViewModels()

    @SuppressLint("StaticFieldLeak", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_info_market, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_comments)

        val contextMenu = object : CommentAdapter.OnContextMenu {
            override fun edit(key: String,position: Int) {
                mainViewModel.liveDataComment.value = mainViewModel.liveDataMarket.value?.comments?.get(key)
                mainViewModel.liveDataIsEditComment.value = true
                editComment()
            }

            override fun remove(key: String,position: Int) {
                mainViewModel.liveDataMarket.value!!.comments.remove(key)
                mainViewModel.dataBase.removeCommentDB(keyComment = key)
                recyclerView.adapter?.notifyItemRemoved(position)
            }

            override fun like(key: String,position: Int) {
                mainViewModel.liveDataMarket.value!!.comments[key]?.warns = mainViewModel.liveDataMarket.value!!.comments[key]?.warns!! + 1
                recyclerView.adapter?.notifyItemChanged(position)
                Toast.makeText(context, context?.applicationContext?.getString(R.string.liked), Toast.LENGTH_SHORT).show()
                mainViewModel.dataBase.updateCommentDB(keyComment = key)
            }

            override fun warn(key: String,position: Int) {
                mainViewModel.liveDataMarket.value!!.comments[key]?.warns = mainViewModel.liveDataMarket.value!!.comments[key]?.warns!! -1
                Toast.makeText(context, context?.applicationContext?.getString(R.string.warned), Toast.LENGTH_SHORT).show()
                recyclerView.adapter?.notifyItemChanged(position)
                mainViewModel.dataBase.updateCommentDB(keyComment = key)
            }

            override fun check(key: String,position: Int): Boolean {
                Toast.makeText(activity, resources.getString(R.string.hold_for_more), Toast.LENGTH_SHORT).show()

                var isAdmin = false
                val comments : ArrayList<Comment> = ArrayList(mainViewModel.liveDataMarket.value?.comments?.values!!)

                for (i in comments.indices) {
                    if (position == i) { // if this comment written this owner
                            isAdmin = comments[i].email == mainViewModel.liveDataUser.value?.email
                        break
                    }
                }
                if(mainViewModel.liveDataUser.value?.email == "Email"){
                    Toast.makeText(context, context?.applicationContext?.getString(R.string.please_login), Toast.LENGTH_SHORT).show()

                }
                return isAdmin
            }

        }

        val adapter = CommentAdapter(ArrayList(mainViewModel.liveDataMarket.value!!.comments.values),contextMenu)
        val linearLayoutManager = LinearLayoutManager(context)
        val nameMarket : TextView = view.findViewById(R.id.nameMarket)
        val time : TextView = view.findViewById(R.id.timeMarket)
        val owner : TextView = view.findViewById(R.id.contactInfoOwner)
        val email : TextView = view.findViewById(R.id.contactInfoEmail)
        val goToCategories: MaterialButton = view.findViewById(R.id.toCategories)
        val goToComments: MaterialButton = view.findViewById(R.id.expand_button)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)

        if (mainViewModel.liveDataUser.value!!.email == "Email") goToComments.visibility = View.GONE else goToComments.visibility = View.VISIBLE

        goToCategories.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down,R.anim.slide_up,R.anim.slide_down)
                .replace(R.id.fragment , CategoryListFragment())
                .addToBackStack("Category").commit()
        }
        goToComments.setOnClickListener {
            @SuppressLint("SimpleDateFormat")
            val timeStamp = SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().time)
            mainViewModel.liveDataComment.value =  Comment(mainViewModel.liveDataUser.value!!.name, "Edit this", mainViewModel.liveDataUser.value!!.email, timeStamp)
            mainViewModel.liveDataIsEditComment.value = false
            editComment()

        }

        mainViewModel.liveDataMarket.observe(this,{
            nameMarket.text = it.nameMarket
            time.text = it.openTime
            owner.text = it.owner
            email.text = it.email
            recyclerView.adapter = CommentAdapter(ArrayList(it.comments.values),contextMenu)
        })

        return view
    }

    private fun editComment(){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down,R.anim.slide_up,R.anim.slide_down)
            .replace(R.id.fragment , CommentEditFragment())
            .addToBackStack("CommentEdit").commit()
    }
}