package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.FollowerAdapter
import com.ex.github.Adapter.RepositoryAdapter
import com.ex.github.Color
import com.ex.github.R
import com.ex.github.ViewModel.PageRepositoryViewModel
import com.ex.github.databinding.FragmentPageRepositoryBinding
import com.ex.github.databinding.FragmentPageRepositoryItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PageRepositoryFragment() : Fragment() {

    private lateinit var binding: FragmentPageRepositoryBinding
    private lateinit var bindingItem: FragmentPageRepositoryItemBinding
    private val viewModel: PageRepositoryViewModel by viewModels()
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_page_repository, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        bindingItem = DataBindingUtil.inflate(inflater, R.layout.fragment_page_repository_item, container, false)


        val currentUser = arguments?.getString("login")
        lifecycleScope.launch {
            var list = currentUser?.let { viewModel.getShowUserRepository(it) }
            if (list != null) {
                Log.d("xxxx", list.toString())

                adapter = RepositoryAdapter(list)
                binding.recyclerview.adapter = adapter
                binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                viewModel.currentUserRepositoryMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            adapter.list = it
                            adapter.notifyDataSetChanged()
                        }
                    })
            }
        }


        bindingItem.constraintLayout.setOnClickListener {

            Toast.makeText(context, "Click", Toast.LENGTH_LONG).show()

            bindingItem.ivStar.Color(R.color.yellow)
            var repositoryName = bindingItem.tvName.text.toString()

            lifecycleScope.launch {
                viewModel.addFavoriteRepository(currentUser.toString(), repositoryName )
                bindingItem.tvStar.text = bindingItem.tvStar.text
            }
        }







        return binding.root
    }
}