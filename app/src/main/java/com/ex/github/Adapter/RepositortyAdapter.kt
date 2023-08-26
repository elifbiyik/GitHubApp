package com.ex.github.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Repositories
import com.ex.github.databinding.FragmentPageRepositoryItemBinding


class RepositoryAdapter(var list : List<Repositories>, private val onClick : (ImageView, Repositories) -> Unit ) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentPageRepositoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repoList: Repositories) {

            with(binding) {
                tvName.text = repoList.name
                tvVisibility.text = repoList.visibility
                tvLanguage.text = repoList.language
                tvStar.text = repoList.stargazers_count

                root.setOnClickListener { onClick(binding.ivStar, repoList) }

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