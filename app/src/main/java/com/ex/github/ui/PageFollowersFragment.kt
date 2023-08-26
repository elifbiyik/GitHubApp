package com.ex.github.ui

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
import com.ex.github.ViewModel.PageFollowersViewModel
import com.ex.github.databinding.FragmentPageFollowersBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPageFollowersBinding.inflate(inflater, container, false)

        val currentUser = arguments?.getString("login")

            lifecycleScope.launch {
                var list = currentUser?.let { viewModel.getShowUserFollowers(it) }

                Log.d("xxxxcurrentUser", currentUser.toString())


                if (list != null) {
                    Log.d("xxxx", list.toString())

                    adapter = FollowerAdapter(list)
                    binding.recyclerview.adapter = adapter
                    binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                    viewModel.currentUserFollowersMutableLiveData.observe(
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
    /*
        companion object {
            fun newInstance(login: String): PageFollowersFragment {
                val fragment = PageFollowersFragment()
                val bundle = Bundle()
                bundle.putString("login", login)
                fragment.arguments = bundle
                return fragment
            }
        }*/
}