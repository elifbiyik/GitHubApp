package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.User
import com.ex.github.databinding.FragmentPageFollowingItemBinding


class FollowingAdapter(var list : List<User> ) : RecyclerView.Adapter<FollowingAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageFollowingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(followingList: User) {
            with(binding) {
                following = followingList
                executePendingBindings()
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