package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.FollowingAdapter
import com.ex.github.User
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
        val clickedUserisFirebase = arguments?.getBoolean("isFirebase")


        lifecycleScope.launch {
            var list: List<User>? = null

            if (clickedUserisFirebase == false) {
                list = clickedUserLogin?.let { viewModel.getShowUserFollowing(it, requireContext()) }
            } else {
                list = clickedUserLogin?.let { viewModel.getShowUserFollowingFromFirebase(it) }
            }

            if (list != null) {
                adapter = FollowingAdapter(list, clickedUserisFirebase!!) {
                    if (clickedUserisFirebase == false) {   // Firebaseden gelen kullanıcıların takip ettiği kişilere tıklamaz.
                        var fragment = DetailFragment()
                        var bundle = Bundle().apply {
                            putString("clickedUserLogin", it.login)
                            putString("clickedUserHtmlUrl", it.html_url)
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

                if (view != null) {
                    viewModel.currentUserFollowingMutableLiveData.observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it.isNotEmpty()) {
                                adapter.list = it
                                adapter.notifyDataSetChanged()
                            }
                        })
                }

                if (view != null) {
                    viewModel.currentUserFollowingFromFirebaseMutableLiveData.observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it.isNotEmpty()) {
                                adapter.list = it
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
            }
        }
        return binding.root
    }
}