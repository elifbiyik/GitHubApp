package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ex.github.Adapter.ViewPagerAdapter
import com.ex.github.Color
import com.ex.github.ImageLoad
import com.ex.github.R
import com.ex.github.ViewModel.DetailViewModel
import com.ex.github.databinding.FragmentAllCommentBinding
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

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        var name = arguments?.getString("login")
        var image = arguments?.getString("image")
        var htmlUrl = arguments?.getString("htmlUrl")
        var avatarUrl = arguments?.getString("avatarUrl")

        lifecycleScope.launch {
            name?.let {
                with(binding) {
                    var currentUser = viewModel.getShowUser(it)
                    tvName.text = currentUser.name
                    tvLogin.text = currentUser.login
                    tvUrl.text = currentUser.html_url
                    tvFollowers.text = currentUser.followers
                    tvFollowing.text = currentUser.following
                    tvRepository.text = currentUser.public_repos
                    image?.let {
                        imageUser.ImageLoad(it)
                    }
                }
            }


            adapter = ViewPagerAdapter(childFragmentManager, lifecycle, name.toString())
            viewPager = binding.viewPager
            viewPager.adapter = adapter

            val red = ContextCompat.getColor(requireContext(), R.color.red)
            val black = ContextCompat.getColor(requireContext(), R.color.black)

            binding.tvTitleFollower.setTextColor(red)
            binding.tvTitleFollowing.setTextColor(black)
            binding.tvTitleRepository.setTextColor(black)

            binding.llFollowers.setOnClickListener {
                binding.tvTitleFollower.setTextColor(red)
                binding.tvTitleFollowing.setTextColor(black)
                binding.tvTitleRepository.setTextColor(black)

                viewPager.currentItem = 0
            }

            binding.llFollowing.setOnClickListener {
                binding.tvTitleFollower.setTextColor(black)
                binding.tvTitleFollowing.setTextColor(red)
                binding.tvTitleRepository.setTextColor(black)

                viewPager.currentItem = 1
            }

            binding.llRepository.setOnClickListener {
                binding.tvTitleFollower.setTextColor(black)
                binding.tvTitleFollowing.setTextColor(black)
                binding.tvTitleRepository.setTextColor(red)

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
                Log.d("xxxListFavUsersDetail", listFavUsers.toString())

                if (listFavUsers.contains(name)) {
                    binding.ivFav.Color(R.color.red)
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
                            binding.ivFav.Color(R.color.red)
                            viewModel.addFavoriteUser(
                                currentUser,
                                name.toString(),
                                htmlUrl.toString(),
                                avatarUrl.toString(),
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
        }
        return binding.root
    }
}