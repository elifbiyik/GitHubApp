package com.ex.github.Adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Color
import com.ex.github.R
import com.ex.github.Repositories
import com.ex.github.databinding.FragmentPageRepositoryItemBinding

class RepositoryAdapter(
    var list: List<Repositories>,
    var listFavoriteRepository: ArrayList<Repositories>,
    var clickedUserLogin: String,
    var clickedUserisFirebase : Boolean,
    private val onClickFavorite: (Repositories) -> Unit
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    init {
        list.forEach {
            it.full_name = clickedUserLogin
            for (repo in listFavoriteRepository) {
                var x = it.name == repo.name && it.full_name.equals(repo.full_name)
                it.isFavorite = x
            }
        }
    }

    // Sadece bilgiler view'e gönderilir.
    inner class ViewHolder(var binding: FragmentPageRepositoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(repoList: Repositories) {
            with(binding) {
                tvName.text = repoList.name
                tvVisibility.text = repoList.visibility
                tvLanguage.text = repoList.language
                tvStar.text = repoList.stargazers_count
            }

            if(clickedUserisFirebase) {
                binding.ivStar.visibility = View.GONE
                binding.ivColor.visibility = View.GONE
                binding.tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            } else {
                binding.ivStar.visibility = View.VISIBLE
            }
        }
    }

    // recyclerviewDe bütün her şey ram'd tutulmaz aşağı indirdikçe eklenir.
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RepositoryAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentPageRepositoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // her scrool edildiğinde burası güncellenir.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var repo = list[position]
        if (repo != null) {
            holder.bind(repo)

            val isFavorite =
                listFavoriteRepository.any { it.name == repo.name && it.full_name == repo.full_name }

            if (isFavorite) {
                holder.binding.ivStar.Color(R.color.yellow)
            } else {
                holder.binding.ivStar.Color(R.color.black)
            }

            holder.binding.clStar.setOnClickListener {
                repo.isFavorite = !repo.isFavorite  // Listedeki bilgiyi değiştir tıkladığında
                onClickFavorite(repo)               // Tıklandığında çağırıcak. Ve bilg. gönd.

                if (repo.isFavorite) {
                    listFavoriteRepository.add(repo)        // Eğer tıkladığında true olursa listeye  ekleyecek.
                } else {
                    listFavoriteRepository.removeIf { it.name == repo.name && it.full_name == repo.full_name }
                }
                notifyItemChanged(position)  // onBindViewHolder tekrar çağırmayı sağladık.
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}