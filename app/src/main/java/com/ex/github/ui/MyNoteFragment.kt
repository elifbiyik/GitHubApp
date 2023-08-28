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
import com.ex.github.Adapter.MyNoteAdapter
import com.ex.github.ViewModel.MyNoteViewModel
import com.ex.github.databinding.FragmentMyNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyNoteFragment : Fragment() {

    private lateinit var binding: FragmentMyNoteBinding
    private val viewModel: MyNoteViewModel by viewModels()
    private lateinit var adapter: MyNoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyNoteBinding.inflate(inflater, container, false)

        val favorite = arguments?.getString("favorite").toString()
        val isUserOrRepository = arguments?.getString("isUserOrRepository").toString()

        lifecycleScope.launch {
            var loginUser = "mojombo"
            var list = viewModel.getMyNote(loginUser, favorite, isUserOrRepository)

            adapter = MyNoteAdapter(list)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())


            viewModel.currentUserMyNoteMutableLiveData.observe(viewLifecycleOwner, Observer {
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