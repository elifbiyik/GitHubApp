package com.ex.github.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.User
import com.ex.github.databinding.FragmentPageFollowingItemBinding


class FollowingAdapter(var list : List<User>, var clickedUserisFirebase : Boolean, private val onClick : (User) -> Unit ) : RecyclerView.Adapter<FollowingAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageFollowingItemBinding) : RecyclerView.ViewHolder(binding.root) {


        //  false api
        fun bind(followingList: User) {
            with(binding) {

                Log.d("xxxxxxxxxxfavLogin", followingList.toString())

                if(clickedUserisFirebase == false) { // Apiden gelenler
                    tvName.text = followingList.login
                    if(followingList.html_url == "null") {
                        tvLogin.text = ""
                    } else {
                        tvLogin.text = followingList.html_url
                    }
                } else {
                    tvName.text = followingList.favLogin
                    if(followingList.html_url == "null") {
                        tvLogin.text = ""
                    } else {
                        tvLogin.text = followingList.html_url
                    }
                }




                imageView.ImageLoad(followingList.avatar_url.toString())

                root.setOnClickListener {onClick(followingList) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentPageFollowingItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var followingList = list[position]
        holder.bind(followingList)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}