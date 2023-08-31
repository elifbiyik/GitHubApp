package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        var clickedUserLogin = arguments?.getString("clickedUserLogin")
        var clickedUserHtmlUrl = arguments?.getString("clickedUserHtmlUrl")
        var clickedUserAvatarUrl = arguments?.getString("clickedUserAvatarUrl")

        lifecycleScope.launch {
            clickedUserLogin?.let {
                with(binding) {
                    var currentUser = viewModel.getShowUser(it)
                    tvName.text = currentUser.name
                    tvLogin.text = currentUser.login
                    tvUrl.text = currentUser.html_url
                    tvFollowers.text = currentUser.followers
                    tvFollowing.text = currentUser.following
                    tvRepository.text = currentUser.public_repos
                    clickedUserAvatarUrl?.let {
                        imageUser.ImageLoad(it)
                    }
                }
            }


            adapter = ViewPagerAdapter(childFragmentManager, lifecycle, clickedUserLogin.toString())
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

            lifecycleScope.launch {
                // Kullanıcının  fav olup olmadığına bakılır. (kalp rengi için)
                var loginUser = "mojombo" // Login yaptıktan sonra loginden al
                var listFavUsers = viewModel.showFavoriteUser(
                    loginUser,
                    requireContext()
                )

                if (listFavUsers.contains(clickedUserLogin)) {
                    binding.ivFav.Color(R.color.red)
                } else {
                    binding.ivFav.Color(R.color.black)
                }

                binding.ivFav.setOnClickListener {
                    Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        var listFavUsers = viewModel.showFavoriteUser(
                            loginUser,
                            requireContext()
                        )
                        if (!listFavUsers.contains(clickedUserLogin)) {
                            binding.ivFav.Color(R.color.red)
                            viewModel.addFavoriteUser(
                                loginUser,
                                clickedUserLogin.toString(),
                                clickedUserHtmlUrl.toString(),
                                clickedUserAvatarUrl.toString(),
                                requireContext()
                            )
                        } else {
                            binding.ivFav.Color(R.color.black)
                            viewModel.removeFavoriteUser(
                                loginUser,
                                clickedUserLogin.toString()
                            )
                        }
                    }
                }
            }
        }
        return binding.root
    }
}