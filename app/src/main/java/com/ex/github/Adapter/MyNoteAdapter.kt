package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Note
import com.ex.github.databinding.FragmentMyNoteItemBinding


class MyNoteAdapter(var list : List<Note>) : RecyclerView.Adapter<MyNoteAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentMyNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notes : Note) {
            with(binding) {
                tvName.text = notes.note
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNoteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentMyNoteItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var note = list[position]
        if (note != null) {
            holder.bind(note)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}