/*

package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageItemBinding

class HomePageAdapter(var list: List<User>, private val onClick: (User) -> Unit) :
    RecyclerView.Adapter<HomePageAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FragmentHomePageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userDetailList: User) {
            with(binding) {
                binding.tvLogin.text = userDetailList.login
                binding.tvHtmlUrl.text = userDetailList.html_url

                if (userDetailList.avatar_url != null) {
                    binding.imageView.ImageLoad(userDetailList.avatar_url.toString())
                } else if (userDetailList.storage != null) {
                    binding.imageView.ImageLoad(userDetailList.storage.toString())
                }
                root.setOnClickListener { onClick(userDetailList) }
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
*/



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

    override fun getItemViewType(position: Int): Int {
        return when (list?.get(position)) {
            is User -> VIEW_TYPE_USER
            is Repositories -> VIEW_TYPE_REPOSITORIES
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

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

