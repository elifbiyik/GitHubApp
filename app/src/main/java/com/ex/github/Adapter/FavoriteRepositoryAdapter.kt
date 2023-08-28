package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Repositories
import com.ex.github.databinding.FragmentFavoriteRepositoryItemBinding

class FavoriteRepositoryAdapter(
    var list: List<Repositories>,
    private val onClick: (Repositories) -> Unit
) :
    RecyclerView.Adapter<FavoriteRepositoryAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FragmentFavoriteRepositoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteRepo: Repositories) {
            binding.apply {
                tvName.text = favoriteRepo.name
                tvLogin.text = favoriteRepo.repoIsWhose

                root.setOnClickListener { onClick(favoriteRepo) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentFavoriteRepositoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var favoriteRepo = list[position]
        holder.bind(favoriteRepo)
    }
}



