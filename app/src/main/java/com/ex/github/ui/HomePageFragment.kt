package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.HomePageAdapter
import com.ex.github.ViewModel.HomePageViewModel
import com.ex.github.R
import com.ex.github.User
import com.ex.github.databinding.FragmentHomePageBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            var list = viewModel.getAllUsers()
            Log.d("xx", list.toString())

            adapter = HomePageAdapter(list) {

                var fragment = DetailFragment()
                var bundle = Bundle()
                bundle.putString("login", it.login)
                bundle.putString("image", it.avatar_url)
                bundle.putString("htmlUrl", it.html_url)
                fragment.arguments = bundle
                replace(fragment)
            }

            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            viewModel.usersMutableLiveData.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                } else {
                    Toast.makeText(requireContext(), "Unsuccesfull", Toast.LENGTH_SHORT).show()
                }
            }
        }

        searchView()

        return binding.root
    }


    fun replace(fragment: Fragment) {

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.constraint, fragment)
            .addToBackStack(null)
            .commit()
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
            viewModel.filteredUsersMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Not Found User ", Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.filterRepositories(p0)
            viewModel.filteredUsersMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Not Found Repository ", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
}
