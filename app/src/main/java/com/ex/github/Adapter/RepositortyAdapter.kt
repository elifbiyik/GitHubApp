package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Repositories
import com.ex.github.databinding.FragmentPageRepositoryItemBinding


class RepositoryAdapter(var list : List<Repositories> ) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageRepositoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repoList: Repositories) {

            with(binding) {
                repository = repoList
                executePendingBindings()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentPageRepositoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var repoList = list[position]
        if (repoList != null) {
            holder.bind(repoList)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}