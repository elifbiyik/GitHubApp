package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPageRepositoryBinding.inflate(inflater, container, false)
        bindingItem = FragmentPageRepositoryItemBinding.inflate(inflater, container, false)

        bindingItem.tvName.visibility = View.GONE

        val login = "mojombo"
        val currentUser = arguments?.getString("login")

        lifecycleScope.launch {
            var list = currentUser?.let { viewModel.getShowUserRepository(it) }
            if (list != null) {
                adapter = RepositoryAdapter(list) { it, it1 ->
                    // Yıldıza tıklandığında ;
                    isFavorite(it, login, currentUser.toString(), it1.name)
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
            }
        }
        return binding.root
    }

    fun isFavorite(imageView: ImageView, login: String, currentUser: String, name: String) {

        lifecycleScope.launch {
            var list = viewModel.getAllList(login)
            var item = Repositories(name, currentUser, null, null, null, null)
            if (list.contains(item)) {
                imageView.Color(R.color.black)
                viewModel.deleteFavoriteRepository(login, name)
            } else {
                imageView.Color(R.color.yellow)
                viewModel.addFavoriteRepository(login, currentUser, name)
            }
        }

    }
}