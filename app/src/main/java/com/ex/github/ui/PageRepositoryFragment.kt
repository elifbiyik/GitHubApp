package com.ex.github.ui

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.RepositoryAdapter
import com.ex.github.Color
import com.ex.github.R
import com.ex.github.Repositories
import com.ex.github.User
import com.ex.github.ViewModel.PageRepositoryViewModel
import com.ex.github.databinding.FragmentPageRepositoryBinding
import com.ex.github.databinding.FragmentPageRepositoryItemBinding
import com.google.firebase.database.core.Repo
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

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPageRepositoryBinding.inflate(inflater, container, false)

        val clickedUserLogin = arguments?.getString("clickedUserLogin").toString()
        val clickedUserisFirebase = arguments?.getBoolean("isFirebase")

        lifecycleScope.launch {
            var currentUser = viewModel.currentUser()
            var loginUser = currentUser[0]

            var list: List<Repositories>? = null

            if (clickedUserisFirebase == false) {
                list =
                    clickedUserLogin?.let { viewModel.getShowUserRepository(it, requireContext()) }
            } else {
                list = clickedUserLogin?.let { viewModel.getShowUserRepositoryFromFirebase(it) }
            }

            if (list != null) {
                var listFavoriteRepository = viewModel.getAllList(loginUser)
                adapter = RepositoryAdapter(
                    list,
                    listFavoriteRepository,
                    clickedUserLogin
                ) {
                    if (clickedUserisFirebase == false) {
                        if (it.isFavorite) {
                            viewModel.addFavoriteRepository(
                                loginUser,
                                clickedUserLogin,
                                it.name.toString()
                            )
                        } else {
                            viewModel.deleteFavoriteRepository(loginUser, it.name.toString())
                        }
                    } else {
                        Toast.makeText(context, "You can't", Toast.LENGTH_SHORT).show()
                    }
                }

                binding.recyclerview.adapter = adapter
                binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                if (view != null) {
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

        }

        return binding.root
    }
}