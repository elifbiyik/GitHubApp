package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.User
import com.ex.github.databinding.FragmentPageFollowingItemBinding


class FollowingAdapter(var list : List<User>, private val onClick : (User) -> Unit ) : RecyclerView.Adapter<FollowingAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageFollowingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(followingList: User) {
            with(binding) {
                tvName.text = followingList.login
                tvLogin.text = followingList.html_url
                binding.imageView.ImageLoad(followingList.avatar_url.toString())

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