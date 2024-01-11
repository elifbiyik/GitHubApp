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
import com.ex.github.Adapter.AccountAllAdapter
import com.ex.github.ViewModel.AccountAllNoteViewModel
import com.ex.github.databinding.FragmentAccountAllNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountAllNoteFragment : Fragment() {

    private lateinit var binding: FragmentAccountAllNoteBinding
    private val viewModel: AccountAllNoteViewModel by viewModels()
    private lateinit var adapter : AccountAllAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAllNoteBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            var currentUser = viewModel.currentUser()
            var loginUser = currentUser[0]
            var list = viewModel.getAllNote(loginUser)
            adapter = AccountAllAdapter(list)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            if (view != null) {
                viewModel.currentUserAccountAllNoteMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            adapter.list = it
                            adapter.notifyDataSetChanged()
                        }
                    })
            }
        }
        return binding.root
    }
}



