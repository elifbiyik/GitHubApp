package com.ex.github.Adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Color
import com.ex.github.R
import com.ex.github.Repositories
import com.ex.github.ViewModel.PageRepositoryViewModel
import com.ex.github.databinding.FragmentPageRepositoryItemBinding
import com.google.android.play.integrity.internal.x


class RepositoryAdapter(
    var list: List<Repositories>,
    var listFavoriteRepository: ArrayList<Repositories>,
    var clickedUserLogin: String,
    val viewModel: PageRepositoryViewModel,
    private val onClickFavorite: (Repositories) -> Unit
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    init {
        list.forEach {
            it.repoIsWhose = clickedUserLogin
            Log.d("xxxxxxxWhose", it.repoIsWhose.toString())

            for (repo in listFavoriteRepository) {
                Log.d("xxxxxxxListFav", listFavoriteRepository.toString())
                Log.d("xxxxxxRepo", repo.toString())


                var x = it.name == repo.name && it.repoIsWhose.equals(repo.repoIsWhose)
                Log.d("xxxxxxx", x.toString())
                Log.d("xxxxxxItName", it.name)
                Log.d("xxxxxxRepoName", repo.name)
                Log.d("xxxxxxItRepos", it.repoIsWhose.toString())
                Log.d("xxxxxxRepoRepos", repo.repoIsWhose.toString())

                it.isFavorite = x

                Log.d("xxxxxxisFavorite", it.toString())
                Log.d("xxxxxxx", it.isFavorite.toString())
            }
        }
        Log.d("xxxxxx", list.toString())
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
        }
    }

// recyclerviewDe bütün her şey ramda tutulmaz aşağı indirdikçe (scrool) eklenir.
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

            val isFavorite = listFavoriteRepository.any { it.name == repo.name && it.repoIsWhose == repo.repoIsWhose }

            if (isFavorite) {
                holder.binding.ivStar.Color(R.color.yellow)
            } else {
                holder.binding.ivStar.Color(R.color.black)
            }

            holder.binding.clStar.setOnClickListener {// Her yıldıza click tanım.

                repo.isFavorite = !repo.isFavorite  // Listedeki bilgiyi değiştir tıkladığında

                Log.d("xxxisFav", repo.toString())
                onClickFavorite(repo)               // Tıklandığında çağırıcak. Ve bilg. gönd.

                if (repo.isFavorite) {
                    listFavoriteRepository.add(repo)        // Eğer tıkladığında true olursa listeye  ekleyecek.
                } else {
                    listFavoriteRepository.removeIf { it.name == repo.name && it.repoIsWhose == repo.repoIsWhose }
                }
                notifyItemChanged(position)  // onBindViewHolder tekrar çağırmayı sağladık.
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun isFavoriteRepository(repositoryName: String, ivStar: ImageView) {
        var item = Repositories(repositoryName, clickedUserLogin, null, null, null, null)
        if (listFavoriteRepository.contains(item)) {
            ivStar.Color(R.color.yellow)
            Log.d("burdayım sarı", "hayır burdayım")
            //        viewModel.deleteFavoriteRepository("Elif B", repositoryName)
        } else {
            ivStar.Color(R.color.black)
            Log.d("burdayım", "evet burdayım")
            //        viewModel.addFavoriteRepository("Elif B", clickedUserLogin, repositoryName)
        }
    }
}