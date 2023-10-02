package com.ex.github.ui

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
import com.ex.github.Adapter.FavoriteUserAdapter
import com.ex.github.ViewModel.FavoriteUserViewModel
import com.ex.github.databinding.FragmentFavoriteUserBinding
import com.ex.github.replace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteUserFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteUserBinding
    private val viewModel: FavoriteUserViewModel by viewModels()
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE

            var currentUser = viewModel.currentUser()
            var loginUser = currentUser[0]
            var list = viewModel.showFavoriteUser(loginUser)

            Log.d("xFavUser", list.toString())

            adapter = FavoriteUserAdapter(list) {
                var clickedFavUser = it.favLogin.toString()
                var fragment = UserNoteFragment()
                var bundle = Bundle()
                bundle.putString("clickedFavUser", clickedFavUser)
                fragment.arguments = bundle

                replace(fragment)
            }

            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            if (view != null) {
                viewModel.currentUserFavoriteUserMutableLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it.isNotEmpty()) {
                            adapter.list = it
                            adapter.notifyDataSetChanged()
                            binding.progressBar.visibility = View.GONE
                        } else {
                            Toast.makeText(requireContext(), "Unsuccesfull", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }
        return binding.root
    }
}
