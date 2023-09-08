package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Repositories
import com.ex.github.databinding.FragmentHomePageItemBinding

class HomePageAdapterRepository(var list : List<Repositories>, private val onClick : (Repositories) -> Unit ) : RecyclerView.Adapter<HomePageAdapterRepository.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentHomePageItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repositoryDetailList: Repositories) {
           with(binding) {
               binding.tvLogin.text = repositoryDetailList.name
               binding.tvHtmlUrl.text = repositoryDetailList.repoIsWhose
               binding.imageView.visibility = View.GONE

                root.setOnClickListener {onClick(repositoryDetailList) }
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

        var repositoryList = list[position]
        holder.bind(repositoryList)
    }
}




