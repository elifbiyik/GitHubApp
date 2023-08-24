package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.FavoriteUserAdapter
import com.ex.github.R
import com.ex.github.ViewModel.FavoriteUserViewModel
import com.ex.github.databinding.FragmentFavoriteUserBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_user, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            var currentUser = "mojombo"  //Login yaptıktan sonra loginden al
            var list = viewModel.showFavoriteUser(currentUser, requireContext())
            Log.d("xxxxFavListFav", list.toString())

            adapter = FavoriteUserAdapter(list){
                requireActivity(). supportFragmentManager.beginTransaction()
                    .replace(R.id.constraint, UserNote())
                    .addToBackStack(null)
                    .commit()
            }
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            viewModel.currentUserFavoriteUserMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Unsuccesfull", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return binding.root
    }
}
