package com.ex.github.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ex.github.Note
import com.ex.github.databinding.FragmentAccountAllNoteItemBinding

class AccountAllAdapter(var list : List<Note> ) : RecyclerView.Adapter<AccountAllAdapter.ViewHolder> () {

    inner class ViewHolder (var binding : FragmentAccountAllNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notes : Note) {
            with(binding) {
                tvLogin.text = notes.login
                tvCom.text = notes.note
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAllAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentAccountAllNoteItemBinding.inflate(inflater, parent, false)
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