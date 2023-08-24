package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.User
import com.ex.github.databinding.FragmentFavoriteUserItemBinding


class FavoriteUserAdapter(var list: List<User>, private val onClick : (User) -> Unit) :
    RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FragmentFavoriteUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favUsersList: User) {
            with(binding) {
                favUsers = favUsersList
                root.setOnClickListener {onClick(favUsersList) }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteUserAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentFavoriteUserItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var favUsersList = list[position]
        if (favUsersList != null) {
            holder.bind(favUsersList)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}