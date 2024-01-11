package com.ex.github.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.R
import com.ex.github.Repositories
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageItemBinding

class HomePageAdapter(var list: List<Any>?, private val onClick: (Any) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_REPOSITORIES = 2
    }

//Farklı türde öğeleri görüntülemek ve bağlamak için kullanılan görünümleri (views) içerir.
    inner class RepositoryViewHolder(var binding: FragmentHomePageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repoList: Repositories) {
            with(binding) {
                binding.tvLogin.text = repoList.name
                binding.tvHtmlUrl.text = repoList.full_name
                val drawableResId = R.drawable.r
                imageView.setImageResource(drawableResId)

                root.setOnClickListener { onClick(repoList) }
            }
        }
    }

    inner class UserViewHolder(var binding: FragmentHomePageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userList: User) {
            with(binding) {
                tvLogin.text = userList.login
                tvHtmlUrl.text = userList.html_url
                if (userList.avatar_url != null) {
                    imageView.ImageLoad(userList.avatar_url.toString())
                } else if (userList.storage != null) {
                    imageView.ImageLoad(userList.storage.toString())
                } else {
                    val drawableResId = R.drawable.ic_account_circle_24
                    val uri = Uri.parse("android.resource://com.ex.github/$drawableResId")
                    imageView.ImageLoad(uri.toString())
                }
                root.setOnClickListener { onClick(userList) }
            }
        }
    }

// Verinin türüne göre öğelerin görünüm türünü belirler. Eğer öğe bir User ise VIEW_TYPE_USER, eğer bir Repositories ise VIEW_TYPE_REPOSITORIES döner.
    override fun getItemViewType(position: Int): Int {
        return when (list?.get(position)) {
            is User -> VIEW_TYPE_USER
            is Repositories -> VIEW_TYPE_REPOSITORIES
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    // Her bir görünüm türü için ayrı bir FragmentHomePageItemBinding nesnesi oluşturulur
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = FragmentHomePageItemBinding.inflate(layoutInflater, parent, false)
                UserViewHolder(binding)
            }

            VIEW_TYPE_REPOSITORIES -> {
                val binding = FragmentHomePageItemBinding.inflate(layoutInflater, parent, false)
                RepositoryViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    // belirtilen pozisyonda bulunan öğeyi bağlar.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_USER -> {
                val user = list?.get(position) as User
                val userViewHolder = holder as UserViewHolder
                userViewHolder.bind(user)
            }

            VIEW_TYPE_REPOSITORIES -> {
                val repository = list?.get(position) as Repositories
                val repositoryViewHolder = holder as RepositoryViewHolder
                repositoryViewHolder.bind(repository)
            }
        }
    }
}

