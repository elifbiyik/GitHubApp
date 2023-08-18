package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.User
import com.ex.github.databinding.FragmentPageFollowersItemBinding


class FollowerAdapter(var list : List<User?> ) : RecyclerView.Adapter<FollowerAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageFollowersItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userDetailList: User?) {

            with(binding) {
                follower = userDetailList
                executePendingBindings()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentPageFollowersItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var followers = list[position]
        if (followers != null) {
            holder.bind(followers)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}