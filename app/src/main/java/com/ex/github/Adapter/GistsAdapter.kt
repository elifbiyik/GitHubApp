package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.User
import com.ex.github.databinding.FragmentPageGistsItemBinding


class GistsAdapter(var list : List<User?> ) : RecyclerView.Adapter<GistsAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageGistsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gistsList: User?) {

            with(binding) {
                gists = gistsList
                executePendingBindings()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GistsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentPageGistsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var gistsList = list[position]
        if (gistsList != null) {
            holder.bind(gistsList)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}