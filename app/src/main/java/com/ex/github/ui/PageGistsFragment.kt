
package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.GistsAdapter
import com.ex.github.Adapter.RepositoryAdapter
import com.ex.github.R
import com.ex.github.ViewModel.PageGistsViewModel
import com.ex.github.ViewModel.PageRepositoryViewModel
import com.ex.github.databinding.FragmentPageGistsBinding
import com.ex.github.databinding.FragmentPageRepositoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PageGistsFragment() : Fragment() {
    
}
/*
    private lateinit var binding: FragmentPageGistsBinding
    private val viewModel: PageGistsViewModel by viewModels()
    private lateinit var adapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_page_gists, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        val currentUser = arguments?.getString("login")

        lifecycleScope.launch {
            var list = currentUser?.let { viewModel.getShowUserGists(it) }

            Log.d("xxxxcurrentUser4", currentUser.toString())


            if (list != null) {
                Log.d("xxxx", list.toString())

                adapter = GistsAdapter(list)
                binding.recyclerview.adapter = adapter
                binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                viewModel.currentUserGistsMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            //                        binding.progressBar.visibility = View.GONE
                            adapter.list = it
                            adapter.notifyDataSetChanged()
                        }
                    })
            }

        }
        return binding.root
    }
}*/
