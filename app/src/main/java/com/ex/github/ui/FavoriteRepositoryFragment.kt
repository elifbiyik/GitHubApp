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
import com.ex.github.Adapter.FavoriteRepositoryAdapter
import com.ex.github.R
import com.ex.github.ViewModel.FavoriteRepositoryViewModel
import com.ex.github.databinding.FragmentFavoriteRepositoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoriteRepositoryFragment : Fragment() {

    private lateinit var binding : FragmentFavoriteRepositoryBinding
    private val viewModel: FavoriteRepositoryViewModel by viewModels()
    private lateinit var adapter : FavoriteRepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteRepositoryBinding.inflate(inflater, container, false)

        var loginUser = "mojombo"
        lifecycleScope.launch {
            var list = viewModel.getAllList(loginUser)

            adapter = FavoriteRepositoryAdapter(list){
                var clickedFavRepo = it.name
                var fragment = UserNoteFragment()
                var bundle = Bundle()
                bundle.putString("clickedFavRepo", clickedFavRepo)
                fragment.arguments = bundle

                requireActivity(). supportFragmentManager.beginTransaction()
                    .replace(R.id.constraint, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            viewModel.currentFavoriteRepositoryMutableLiveData.observe(viewLifecycleOwner, Observer {
                adapter.list = it
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            })

        }

        return binding.root
    }
}