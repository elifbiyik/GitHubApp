package com.ex.github.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageItemBinding

class HomePageViewHolderUser(private val binding: FragmentHomePageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userDetailList: User) {
        with(binding) {
            binding.tvLogin.text = userDetailList.login
            binding.tvHtmlUrl.text = userDetailList.html_url
            if (userDetailList.avatar_url != null) {
                binding.imageView.ImageLoad(userDetailList.avatar_url.toString())
            } else if (userDetailList.storage != null) {
                binding.imageView.ImageLoad(userDetailList.storage.toString())
            }        }
    }
}




