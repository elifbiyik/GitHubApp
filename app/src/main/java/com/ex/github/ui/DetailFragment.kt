package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
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


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        var clickedUserLogin = arguments?.getString("clickedUserLogin")
        var clickedUserHtmlUrl = arguments?.getString("clickedUserHtmlUrl")
        var clickedUserAvatarUrl = arguments?.getString("clickedUserAvatarUrl")
        var clickedUserisFirebase = arguments?.getString("isFirebase").toBoolean()
        var clickedUserPhoneNumber = arguments?.getString("clickedUserNumber")

        var clickedRepoName = arguments?.getString("clickedRepoName")
        var clickedRepoIsWhose = arguments?.getString("clickedRepoIsWhose")

        lifecycleScope.launch {
            if (!clickedUserLogin.isNullOrEmpty()) {
                if (clickedUserisFirebase == false) {
                    with(binding) {
                        val currentUser =
                        viewModel.getShowUserFromApi(clickedUserLogin, requireContext())
                        tvName.text = currentUser.name
                        tvLogin.text = currentUser.login
                        tvUrl.text = currentUser.html_url
                        tvFollowers.text = currentUser.followers
                        tvFollowing.text = currentUser.following
                        tvRepository.text = currentUser.public_repos
                        clickedUserAvatarUrl?.let { imageUser.ImageLoad(it) }
                    }
                }
            }
            if (!clickedUserPhoneNumber.isNullOrEmpty()) {
                var followersSize = clickedUserLogin?.let {
                    viewModel.getShowUserFollowersSize(it)
                }.toString()

                var followingSize = clickedUserLogin?.let {
                    viewModel.getShowUserFollowingSize(it)
                }.toString()

                var repositorySize = clickedUserLogin?.let {
                    viewModel.getShowUserRepositorySize(it)
                }.toString()

                with(binding) {
                    if (clickedUserisFirebase == true) {
                        val currentUser = viewModel.getShowUserFromFirebase(clickedUserPhoneNumber)
                        tvName.text = currentUser.login
                        tvLogin.text = currentUser.phoneNumber
                        tvFollowers.text = followersSize
                        tvFollowing.text = followingSize
                        tvRepository.text = repositorySize
                        clickedUserAvatarUrl?.let { imageUser.ImageLoad(it) }
                    }
                }
            }

            clickedRepoName?.let {
                with(binding) {
                    tvName.text = clickedRepoName
                    tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25.toFloat())
                    tvLogin.text = clickedRepoIsWhose
                    tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.toFloat())
                    ivFav.visibility = View.GONE
                    cardView.visibility = View.GONE
                    viewPager.visibility = View.GONE
                    clickedUserAvatarUrl?.let { imageUser.ImageLoad(it) }
                }
            }

            lifecycleScope.launch {
                var currentUser = viewModel.currentUser()
                var loginUser = currentUser[0]
                var loginPhone = currentUser[1]

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
                    lifecycleScope.launch {
                        var listFavUsers = viewModel.showFavoriteUser(
                            loginUser,
                            requireContext()
                        )
                        if (!listFavUsers.contains(clickedUserLogin)) {
                            binding.ivFav.Color(R.color.red)
                                viewModel.addFavoriteUser(
                                    loginUser,
                                    loginPhone,
                                    clickedUserLogin.toString(),
                                    clickedUserHtmlUrl.toString(),
                                    clickedUserAvatarUrl.toString(),
                                    clickedUserisFirebase,
                                    clickedUserPhoneNumber.toString(),
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

        adapter = ViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            clickedUserLogin.toString(),
            clickedUserisFirebase
        )
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        val red = ContextCompat.getColor(requireContext(), R.color.red)
        val black = ContextCompat.getColor(requireContext(), R.color.black)

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    // Sayfa kaydırıldığında
                    when (position) {
                        0 -> {
                            binding.tvTitleFollower.setTextColor(red)
                            binding.tvTitleFollowing.setTextColor(black)
                            binding.tvTitleRepository.setTextColor(black)
                        }

                        1 -> {
                            binding.tvTitleFollower.setTextColor(black)
                            binding.tvTitleFollowing.setTextColor(red)
                            binding.tvTitleRepository.setTextColor(black)
                        }

                        2 -> {
                            binding.tvTitleFollower.setTextColor(black)
                            binding.tvTitleFollowing.setTextColor(black)
                            binding.tvTitleRepository.setTextColor(red)
                        }
                    }
                }
            })

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
        return binding.root
    }
}