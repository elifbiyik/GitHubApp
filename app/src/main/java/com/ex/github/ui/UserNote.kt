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
import com.ex.github.Adapter.ViewPagerCommentAdapter
import com.ex.github.R
import com.ex.github.ViewModel.DetailViewModel
import com.ex.github.ViewModel.UserNoteViewModel
import com.ex.github.databinding.FragmentUserNoteBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserNote : Fragment() {

    private lateinit var binding: FragmentUserNoteBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerCommentAdapter
    private val viewModel: UserNoteViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_note, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        var favUser = arguments?.getString("favUser").toString()
        // Tıklanan user
        Log.d("xxxcommentToUser", favUser)


        binding.textView.text = favUser

        commentListPager(favUser)

        //      if(binding.etComment.text.isEmpty()) {
        //        binding.btnSend.isEnabled = false
        //  }else{
        binding.btnSend.isEnabled = true

        binding.btnSend.setOnClickListener {
            var currentUser = "mojombo"
            var comment = binding.etComment.text.toString()
            lifecycleScope.launch {
                var isAdded = viewModel.addComment(currentUser, favUser, comment)

                if (isAdded) {
                    commentListPager(favUser)
                } else {
                    commentListPager(favUser)
                }
            }
            //         }

        }






        return binding.root
    }

    fun commentListPager(favUser: String) {
        adapter = ViewPagerCommentAdapter(childFragmentManager, lifecycle, favUser)
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        }

}