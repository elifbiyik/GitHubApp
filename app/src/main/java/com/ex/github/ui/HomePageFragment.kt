package com.ex.github.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.HomePageAdapter
import com.ex.github.Repositories
import com.ex.github.User
import com.ex.github.ViewModel.HomePageViewModel
import com.ex.github.databinding.FragmentHomePageBinding
import com.ex.github.replace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private val viewModel: HomePageViewModel by viewModels()
    private lateinit var adapter: HomePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.usersMutableLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter.list = it
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            var listUsers = viewModel.getAllUsers()
            var listRepo = viewModel.getAllRepositories()

            var list = listUsers + listRepo

            adapter = HomePageAdapter(list) {
                val fragment = DetailFragment().apply {
                    val bundle = Bundle().apply {
                        when (it) {
                            is User -> {
                                if (!it.isFirebase!!) {
                                    putString("clickedUserLogin", it.login)
                                    putString("clickedUserHtmlUrl", it.html_url)
                                    putString("clickedUserAvatarUrl", it.avatar_url)
                                } else {
                                    putString("clickedUserLogin", it.login)
                                    putString("clickedUserNumber", it.phoneNumber)
                                }
                            }

                            is Repositories -> {
                                putString("clickedRepoName", it.name)
                                putString("clickedRepoIsWhose", it.full_name)
                            }
                        }
                    }
                    arguments = bundle
                }
                replace(fragment)
            }

            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            binding.progressBar.visibility = View.GONE
        }

        searchView()

        binding.ivFavorite.setOnClickListener {
            replace(FavoriteFragment())
        }

        binding.ivAccount.setOnClickListener {
            replace(AccountFragment())
        }

        return binding.root
    }

    fun searchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    searchText(p0)
                }
                return true
            }
        })
    }

    fun searchText(p0: String) {
        lifecycleScope.launch {

            viewModel.filterUsers(p0)
            viewModel.filterRepositories(p0)

            viewModel.filteredMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Not Found User ", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
