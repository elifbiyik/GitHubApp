package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Comment
import com.ex.github.databinding.FragmentMyCommentItemBinding


class MyCommentAdapter(var list : List<Comment>) : RecyclerView.Adapter<MyCommentAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentMyCommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comments : Comment) {
            with(binding) {
                comment = comments
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentMyCommentItemBinding.inflate(inflater, parent, false)
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