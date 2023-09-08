package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Note
import com.ex.github.databinding.FragmentAllNoteItemBinding

class AllNoteAdapter(var list : List<Note> ) : RecyclerView.Adapter<AllNoteAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentAllNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notes : Note) {
            with(binding) {
                tvName.text = notes.note
                tvLogin.text = notes.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllNoteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentAllNoteItemBinding.inflate(inflater, parent, false)
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