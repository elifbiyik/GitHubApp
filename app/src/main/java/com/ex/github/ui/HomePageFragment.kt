package com.ex.github.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.HomePageAdapter
import com.ex.github.R
import com.ex.github.Repositories
import com.ex.github.User
import com.ex.github.ViewModel.HomePageViewModel
import com.ex.github.databinding.FragmentHomePageBinding
import com.ex.github.replace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log


@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private val viewModel: HomePageViewModel by viewModels()
    private lateinit var adapter: HomePageAdapter

    // Accountta profil güncellendikten sonra HomePage dönünce güncellemyior.
    override fun onPause() {
        super.onPause()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        var loginPhone = arguments?.getString("loginPhone")

        lifecycleScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            binding.searchView.visibility = View.GONE
            binding.ivFavorite.visibility = View.GONE
            binding.ivAccount.visibility = View.GONE

            var listUsersApi = viewModel.getAllUsersFromApi(requireContext())
            var listUsersFirebase = viewModel.getAllUsersFromFirebase()
            var listRepo = viewModel.getAllRepositories(requireContext())

            var list: List<Any>? = null
            if (listUsersApi == null) {
                if (listUsersFirebase != null && listRepo != null) {
                    list = listUsersFirebase + listRepo
                } else if (listUsersFirebase != null && listRepo == null) {
                    list = listUsersFirebase
                } else if (listUsersFirebase == null && listRepo != null) {
                    list = listRepo
                }
            } else if (listUsersFirebase == null) {
                if (listUsersApi != null && listRepo != null) {
                    list = listUsersApi + listRepo
                } else if (listUsersApi != null && listRepo == null) {
                    list = listUsersApi
                } else if (listUsersApi == null && listRepo != null) {
                    list = listRepo
                }
            } else if (listRepo == null) {
                if (listUsersApi != null && listUsersFirebase != null) {
                    list = listUsersApi + listUsersFirebase
                } else if (listUsersApi != null && listUsersFirebase == null) {
                    list = listUsersApi
                } else if (listUsersApi == null && listUsersFirebase != null) {
                    list = listUsersFirebase
                }
            } else {
                list = listUsersFirebase + listUsersApi + listRepo
            }

            adapter = HomePageAdapter(list) {
                val fragment = DetailFragment().apply {
                    val bundle = Bundle().apply {
                        when (it) {
                            is User -> {
                                if (!it.isFirebase!!) {
                                    putString("clickedUserLogin", it.login)
                                    putString("clickedUserHtmlUrl", it.html_url)
                                    putString("clickedUserAvatarUrl", it.avatar_url)
                                    putString("isFirebase", it.isFirebase.toString())
                                } else {
                                    putString("clickedUserLogin", it.login)
                                    putString("clickedUserNumber", it.phoneNumber)
                                    putString("clickedUserAvatarUrl", it.storage?.toString())
                                    putString("isFirebase", it.isFirebase.toString())

                                    putString("loginPhone", loginPhone)
                                }
                            }

                            is Repositories -> {
                                var image = R.drawable.r
                                putString("clickedRepoName", it.name)
                                putString("clickedRepoIsWhose", it.full_name)
                                putString("clickedUserAvatarUrl", image.toString())
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
            binding.searchView.visibility = View.VISIBLE
            binding.ivFavorite.visibility = View.VISIBLE
            binding.ivAccount.visibility = View.VISIBLE
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

            viewModel.filterUsers(p0, requireContext())
            viewModel.filterRepositories(p0, requireContext())

            // Can't access the Fragment View's LifecycleOwner for FavoriteUserFragment{8f7a67a} (1c1ddaaa-859b-40bf-8403-591016a298b2 tag=f0) when getView() is null i.e., before onCreateView() or after onDestroyView()
            // hatası veriyor. Bu yüzden ->  if (view != null)  bunu yaz !!
            if (view != null) {
                viewModel.filteredUsersMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer { user ->
                        viewModel.filteredRepositoriesMutableLiveData.observe(
                            viewLifecycleOwner,
                            Observer { repo ->
                                if (repo != null) {
                                    if (repo.isNotEmpty() || user.isNotEmpty()) {
                                        if (repo.isEmpty()) {
                                            adapter.list = user
                                            adapter.notifyDataSetChanged()
                                        } else if (user.isEmpty()) {
                                            adapter.list = repo
                                            adapter.notifyDataSetChanged()
                                        } else {
                                            adapter.list = user + repo
                                            adapter.notifyDataSetChanged()
                                        }
                                    } else {
                                        adapter.list = user + repo
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            })
                    })
            }
        }
    }
}
