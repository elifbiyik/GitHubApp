package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ex.github.Adapter.ViewPagerAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        var name = arguments?.getString("login")
        var image = arguments?.getString("image")


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

        binding.llFollowers.setOnClickListener {
            viewPager.currentItem = 0
        }

        binding.llFollowing.setOnClickListener {
            viewPager.currentItem = 1
        }

        binding.llRepository.setOnClickListener {
            viewPager.currentItem = 2
        }

   //     binding.llGists.setOnClickListener {
    //        viewPager.currentItem = 3
//        }


        return binding.root
    }
}