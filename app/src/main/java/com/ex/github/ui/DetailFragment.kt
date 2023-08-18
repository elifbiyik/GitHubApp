package com.ex.github.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
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
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        var name = arguments?.getString("login")
        var image = arguments?.getString("image")
        //    var url = arguments?.getString("url")

        with(binding) {
            tvName.text = name
            //     tvUrl.text = url
            image?.let { imageUser.ImageLoad(it) }
        }

        lifecycleScope.launch {
            name?.let {
                var currentUser = viewModel.getShowUser(it)
                Log.d("xxx", currentUser.toString())
                binding.user = currentUser
            }
        }



        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
   //     adapter = ViewPagerAdapter(childFragmentManager)
        viewPager = binding.viewPager
        viewPager.adapter = adapter







/*        binding.llFollowers.setOnClickListener {
            name?.let { it -> replace(PageFollowersFragment(), it) }
        }
        binding.llFollowing.setOnClickListener {
            name?.let { it -> replace(PageFollowingFragment(), it) }
        }
        binding.llRepository.setOnClickListener {
            name?.let { it -> replace(PageRepositoryFragment(), it) }
        }*/


        return binding.root
    }

    fun replace(fragment: Fragment, name : String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.constraint, fragment)
            .addToBackStack(null)
            .commit()

        var bundle = Bundle()
        bundle.putString("login", name)
        fragment.arguments = bundle
    }
}