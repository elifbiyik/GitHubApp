package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.FollowingAdapter
import com.ex.github.ViewModel.PageFollowingViewModel
import com.ex.github.databinding.FragmentPageFollowingBinding
import com.ex.github.replace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PageFollowingFragment : Fragment() {

    private lateinit var binding: FragmentPageFollowingBinding
    private val viewModel: PageFollowingViewModel by viewModels()
    private lateinit var adapter: FollowingAdapter

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

        binding = FragmentPageFollowingBinding.inflate(inflater, container, false)

        var clickedUserLogin = arguments?.getString("clickedUserLogin")

        lifecycleScope.launch {
            var list = clickedUserLogin?.let { viewModel.getShowUserFollowing(it) }
            if (list != null) {
                adapter = FollowingAdapter(list) {
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

                viewModel.currentUserFollowingMutableLiveData.observe(
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