package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageItemBinding

class HomePageAdapter(var list : List<User>, private val onClick : (User) -> Unit ) : RecyclerView.Adapter<HomePageAdapter.ViewHolder> () {


    inner class ViewHolder (var binding : FragmentHomePageItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userDetailList: User) {

           with(binding) {
                user = userDetailList
                root.setOnClickListener {onClick(userDetailList) }
                executePendingBindings()
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




