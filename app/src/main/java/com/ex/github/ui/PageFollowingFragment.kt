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
import com.ex.github.Adapter.FollowingAdapter
import com.ex.github.ViewModel.PageFollowingViewModel
import com.ex.github.databinding.FragmentPageFollowingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PageFollowingFragment : Fragment() {

    private lateinit var binding : FragmentPageFollowingBinding
    private val viewModel : PageFollowingViewModel by viewModels()
    private lateinit var adapter : FollowingAdapter

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

        //  binding.tvFollowing.text = "Following"


                lifecycleScope.launch {

                    var currentUser = arguments?.getString("login")
                    var list = currentUser?.let { viewModel.getShowUserFollowing(it) }
                    Log.d("xxxxcurrentUser2", currentUser.toString())


                    if (list != null) {
                        adapter = FollowingAdapter(list)
                        binding.recyclerview.adapter = adapter
                        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                        viewModel.currentUserFollowingMutableLiveData.observe(
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
}