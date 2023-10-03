package com.ex.github.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.AccountMyAdapter
import com.ex.github.ViewModel.AccountMyNoteViewModel
import com.ex.github.databinding.FragmentAccountMyNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountMyNoteFragment : Fragment() {

    private lateinit var binding: FragmentAccountMyNoteBinding
     private val viewModel: AccountMyNoteViewModel by viewModels()
    private lateinit var adapter : AccountMyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountMyNoteBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            var currentUser = viewModel.currentUser()
            var loginUser = currentUser[0]
            var list = viewModel.getMyNote(loginUser)
            adapter = AccountMyAdapter(list)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            if (view != null) {
                viewModel.currentUserAccountMyNoteMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            adapter.list = it
                            adapter.notifyDataSetChanged()
                        } else {
                         //   Toast.makeText(context, "...", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
        return binding.root
    }
}