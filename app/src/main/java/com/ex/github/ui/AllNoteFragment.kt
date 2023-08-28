package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.AllNoteAdapter
import com.ex.github.ViewModel.AllNoteViewModel
import com.ex.github.databinding.FragmentAllNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNoteFragment : Fragment() {

    private lateinit var binding: FragmentAllNoteBinding
    private val viewModel: AllNoteViewModel by viewModels()
    private lateinit var adapter : AllNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllNoteBinding.inflate(inflater, container, false)

        val favorite = arguments?.getString("favorite").toString()
        val isUserOrRepository = arguments?.getString("isUserOrRepository").toString()

        lifecycleScope.launch {
            var list = viewModel.getAllNote(favorite, isUserOrRepository)
            adapter = AllNoteAdapter(list)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            viewModel.currentUserAllNoteMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "...", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return binding.root
    }
}