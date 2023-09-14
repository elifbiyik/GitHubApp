package com.ex.github.Adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.ImageLoad
import com.ex.github.R
import com.ex.github.User
import com.ex.github.databinding.FragmentFavoriteUserItemBinding

class FavoriteUserAdapter(var list: List<User>, private val onClick : (User) -> Unit) :
    RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FragmentFavoriteUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favUsersList: User) {
            with(binding) {
                tvName.text = favUsersList.favLogin

                Log.d("FavUSerAdapter", favUsersList.toString())

                if(favUsersList.html_url != null) {
                    tvLogin.text = favUsersList.html_url
                } else if (favUsersList.html_url == null) {
                    tvLogin.visibility = View.GONE
                }

                if(favUsersList.avatar_url != null) {
                    binding.imageView.ImageLoad(favUsersList.avatar_url.toString())
                } else if (favUsersList.storage != null) {
                    binding.imageView.ImageLoad(favUsersList.storage.toString())
                }else {
                    val drawableResId = R.drawable.ic_account_circle_24
                    val uri = Uri.parse("android.resource://com.ex.github/$drawableResId")
                    binding.imageView.ImageLoad(uri.toString())
                }


                root.setOnClickListener {onClick(favUsersList) }
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