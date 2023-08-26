package com.ex.github.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ex.github.ImageLoad
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageItemBinding
import dagger.hilt.android.qualifiers.ApplicationContext

class HomePageAdapter(var list : List<User>, private val onClick : (User) -> Unit ) : RecyclerView.Adapter<HomePageAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentHomePageItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userDetailList: User) {

           with(binding) {
               binding.tvLogin.text = userDetailList.login
               binding.tvHtmlUrl.text = userDetailList.html_url
               binding.imageView.ImageLoad(userDetailList.avatar_url.toString())
                root.setOnClickListener {onClick(userDetailList) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentHomePageItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var userList = list[position]
        holder.bind(userList)
    }
}




