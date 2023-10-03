package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Note
import com.ex.github.databinding.FragmentAccountMyNoteItemBinding

class AccountMyAdapter(var list : List<Note> ) : RecyclerView.Adapter<AccountMyAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentAccountMyNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notes : Note) {
            with(binding) {
                tvCommentForWhose.text = notes.noteToUserOrRepository
                tvComment.text = notes.note
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountMyAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentAccountMyNoteItemBinding.inflate(inflater, parent, false)
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