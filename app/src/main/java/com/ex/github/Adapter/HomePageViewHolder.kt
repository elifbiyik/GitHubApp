package com.ex.github.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageItemBinding

class HomePageViewHolder(private val binding: FragmentHomePageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userDetailList: User) {
        with(binding) {
            binding.tvLogin.text = userDetailList.login
            binding.tvHtmlUrl.text = userDetailList.html_url
            binding.imageView.ImageLoad(userDetailList.avatar_url.toString())
        }
    }
}