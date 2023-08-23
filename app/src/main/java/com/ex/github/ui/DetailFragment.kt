package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ex.github.Adapter.ViewPagerAdapter
import com.ex.github.Color
import com.ex.github.ImageLoad
import com.ex.github.R
import com.ex.github.ViewModel.DetailViewModel
import com.ex.github.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        var name = arguments?.getString("login")
        var image = arguments?.getString("image")
        var htmlUrl = arguments?.getString("htmlUrl")


        with(binding) {
            tvName.text = name
            image?.let { imageUser.ImageLoad(it) }
        }

        lifecycleScope.launch {
            name?.let {
                var currentUser = viewModel.getShowUser(it)
                binding.user = currentUser
            }
        }

        adapter = ViewPagerAdapter(childFragmentManager, lifecycle, name.toString())
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.background)
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)

        binding.tvTitleFollower.setTextColor(backgroundColor)
        binding.tvTitleFollowing.setTextColor(blackColor)
        binding.tvTitleRepository.setTextColor(blackColor)

        binding.llFollowers.setOnClickListener {
            binding.tvTitleFollower.setTextColor(backgroundColor)
            binding.tvTitleFollowing.setTextColor(blackColor)
            binding.tvTitleRepository.setTextColor(blackColor)

            viewPager.currentItem = 0
        }

        binding.llFollowing.setOnClickListener {
            binding.tvTitleFollower.setTextColor(blackColor)
            binding.tvTitleFollowing.setTextColor(backgroundColor)
            binding.tvTitleRepository.setTextColor(blackColor)

            viewPager.currentItem = 1
        }

        binding.llRepository.setOnClickListener {
            binding.tvTitleFollower.setTextColor(blackColor)
            binding.tvTitleFollowing.setTextColor(blackColor)
            binding.tvTitleRepository.setTextColor(backgroundColor)

            viewPager.currentItem = 2
        }

        //     binding.llGists.setOnClickListener {
        //        viewPager.currentItem = 3
//        }


        lifecycleScope.launch {
            var currentUser = "mojombo" // Login yaptıktan sonra loginden al
            var listFavUsers = viewModel.showFavoriteUser(
                currentUser,
                requireContext()
            )
            if (listFavUsers.contains(name)) {
                binding.ivFav.Color(R.color.background)
            } else {
                binding.ivFav.Color(R.color.black)
            }

            binding.ivFav.setOnClickListener {
                lifecycleScope.launch {
                    var currentUser = "mojombo" // Login yaptıktan sonra loginden al
                    var listFavUsers = viewModel.showFavoriteUser(
                        currentUser,
                        requireContext()
                    )
                    if (!listFavUsers.contains(name)) {
                        binding.ivFav.Color(R.color.background)
                        viewModel.addFavoriteUser(
                            currentUser,
                            name.toString(),
                            htmlUrl.toString(),
                            requireContext()
                        )
                    } else {
                        binding.ivFav.Color(R.color.black)
                        viewModel.removeFavoriteUser(
                            currentUser,
                            name.toString()
                        )
                    }
                }
            }
        }

        return binding.root
    }
}