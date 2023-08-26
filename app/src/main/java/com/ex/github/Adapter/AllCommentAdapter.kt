package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Comment
import com.ex.github.databinding.FragmentAllCommentItemBinding


class AllCommentAdapter(var list : List<Comment> ) : RecyclerView.Adapter<AllCommentAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentAllCommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comments : Comment) {
            with(binding) {
                tvName.text = comments.comment
                tvLogin.text = comments.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCommentAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentAllCommentItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var comment = list[position]
        if (comment != null) {
            holder.bind(comment)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}