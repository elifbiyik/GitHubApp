package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.FollowerAdapter
import com.ex.github.R
import com.ex.github.ViewModel.PageFollowersViewModel
import com.ex.github.databinding.FragmentPageFollowersBinding
import com.ex.github.replace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PageFollowersFragment() : Fragment() {

    private lateinit var binding: FragmentPageFollowersBinding
    private val viewModel: PageFollowersViewModel by viewModels()
    private lateinit var adapter: FollowerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPageFollowersBinding.inflate(inflater, container, false)

        val clickedUserLogin = arguments?.getString("clickedUserLogin")

            lifecycleScope.launch {
                var list = clickedUserLogin?.let { viewModel.getShowUserFollowers(it) }

                if (list != null) {
                    adapter = FollowerAdapter(list) {
                        var fragment = DetailFragment()
                        var bundle = Bundle().apply {
                            putString("clickedUserLogin", it.login)
                            putString("clickedUserHtmlUrl", it.html_url)
                            putString("clickedUserAvatarUrl", it.avatar_url)
                        }
                        fragment.arguments = bundle
                        replace(fragment)
                    }
                    binding.recyclerview.adapter = adapter
                    binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                    viewModel.currentUserFollowersMutableLiveData.observe(
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