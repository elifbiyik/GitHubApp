package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.FollowerAdapter
import com.ex.github.User
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

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPageFollowersBinding.inflate(inflater, container, false)

        val clickedUserLogin = arguments?.getString("clickedUserLogin")
        val clickedUserisFirebase = arguments?.getBoolean("isFirebase")

            lifecycleScope.launch {
                var list: List<User>? = null

                if(clickedUserisFirebase == false) {
                    list = clickedUserLogin?.let { viewModel.getShowUserFollowersApi(it, requireContext()) }
                } else {
                    list = clickedUserLogin?.let { viewModel.getShowUserFollowersFromFirebase(it) }
                    Log.d("****List", list.toString())
                }

                if (list != null) {
                    adapter = FollowerAdapter(list) {
                        if (clickedUserisFirebase == false) {
                            var fragment = DetailFragment()
                            var bundle = Bundle().apply {
                                putString("clickedUserLogin", it.login)
                                putString("clickedUserHtmlUrl", it.phoneNumber)
                                putString("clickedUserAvatarUrl", it.avatar_url)
                                putString("isFirebase", it.isFirebase.toString())
                            }
                            fragment.arguments = bundle
                            replace(fragment)
                        } else {
                            Toast.makeText(context, "You can't", Toast.LENGTH_SHORT).show()
                        }
                    }
                    binding.recyclerview.adapter = adapter
                    binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

           //         if (view != null) {
                        viewModel.currentUserFollowersMutableLiveData.observe(
                            viewLifecycleOwner,
                            Observer {
                                if (it.isNotEmpty()) {
                                    adapter.list = it
                                    adapter.notifyDataSetChanged()
                                }
                            })
   //                 }

   //                 if (view != null) {
                        viewModel.currentUserFollowersFromfirebaseMutableLiveData.observe(
                            viewLifecycleOwner,
                            Observer {
                                if (it.isNotEmpty()) {
                                    adapter.list = it
                                    adapter.notifyDataSetChanged()
                                }
                            })
          //          }
                }
            }
        return binding.root
    }
}