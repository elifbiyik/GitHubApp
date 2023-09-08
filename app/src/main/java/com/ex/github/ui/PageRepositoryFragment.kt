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
import com.ex.github.ViewModel.PageRepositoryViewModel
import com.ex.github.databinding.FragmentPageRepositoryBinding
import com.ex.github.databinding.FragmentPageRepositoryItemBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val clickedUserLogin = arguments?.getString("clickedUserLogin").toString()

        lifecycleScope.launch {
            var currentUser = viewModel.currentUser()
            var loginUser = currentUser[0]
            var list = clickedUserLogin?.let { viewModel.getShowUserRepository(it) }

            if (list != null) {
                var listFavoriteRepository = viewModel.getAllList(loginUser)
                adapter = RepositoryAdapter(
                    list,
                    listFavoriteRepository,
                    clickedUserLogin,
                    viewModel
                ){
                    if(it.isFavorite) {
                        viewModel.addFavoriteRepository(loginUser, clickedUserLogin, it.name)
                    }else {
                        viewModel.deleteFavoriteRepository(loginUser, it.name)
                    }
                }

                binding.recyclerview.adapter = adapter
                binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

                viewModel.currentUserRepositoryMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            adapter.list = it
                            adapter.notifyDataSetChanged()
                        }
                    })

               /* viewModel.currentUserFavoriteRepositoryMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            adapter.listFavoriteRepository = it
                            adapter.notifyDataSetChanged()
                        }
                    })*/
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPageRepositoryBinding.inflate(inflater, container, false)



        return binding.root
    }

    fun isFavorite(
        listFavoriteRepository: ArrayList<Repositories>,
        imageView: ImageView,
        loginUser: String,
        clickedUserLogin: String,
        repositoryName: String
    ) {
        lifecycleScope.launch {
            var item = Repositories(repositoryName, clickedUserLogin, null, null, null, null)
            //      var listFavoriteRepository = viewModel.getAllList(loginUser)

            if (listFavoriteRepository.contains(item)) {
                imageView.Color(R.color.black)
                viewModel.deleteFavoriteRepository(loginUser, repositoryName)
            } else {
                imageView.Color(R.color.yellow)
                viewModel.addFavoriteRepository(loginUser, clickedUserLogin, repositoryName)
            }
        }
    }
}