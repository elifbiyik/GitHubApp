package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ex.github.Adapter.ViewPagerNoteAdapter
import com.ex.github.ViewModel.UserNoteViewModel
import com.ex.github.databinding.FragmentUserNoteBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserNoteFragment : Fragment() {

    private lateinit var binding: FragmentUserNoteBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerNoteAdapter
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

        binding = FragmentUserNoteBinding.inflate(inflater, container, false)

        // Eğer ?: "" yazılmazsa ; favUser != null' is always 'true' oluyor. Repository için binding.textView.text = null oluyor
        var clickedFavUser = arguments?.getString("clickedFavUser") ?: ""
        // Tıklanan user
        var clickedFavRepo = arguments?.getString("clickedFavRepo") ?: ""
        // Tıklanan user

        if (clickedFavUser.isNotEmpty()) {
            binding.textView.text = clickedFavUser
            noteListPager(clickedFavUser, "User")
        } else if (clickedFavRepo.isNotEmpty()) {
            binding.textView.text = clickedFavRepo
            noteListPager(clickedFavRepo, "Repository")
        } else {
            binding.textView.text = ""
        }


        // TODO(butonu if else'lerin içine alsam ?? Firebasede Note/FavRepository ve Note/FavUser için)????
        // TODO(Repository'i ekleyince current User null dönüyor)
        binding.btnSend.setOnClickListener {
            addNote(clickedFavUser, clickedFavRepo)
        }
        return binding.root
    }

    fun noteListPager(favorite: String, isUserOrRepository : String) {
        adapter = ViewPagerNoteAdapter(childFragmentManager, lifecycle, favorite, isUserOrRepository)
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

    fun addNote(clickedFavUser: String?, clickedFavRepo: String?) {

        var loginUser = "mojombo"
        var note = binding.etNote.text.toString()

        lifecycleScope.launch {
            if (clickedFavUser?.isNotEmpty() == true) {
                var isAdded = viewModel.addNote(loginUser, clickedFavUser, note, "User")
                if (isAdded) {
                    noteListPager(clickedFavUser, "User")
                } else {
                    noteListPager(clickedFavUser, "User")
                }
            } else if (clickedFavRepo?.isNotEmpty() == true) {
                var isAdded = viewModel.addNote(loginUser, clickedFavRepo, note, "Repository")
                if (isAdded) {
                    noteListPager(clickedFavRepo, "Repository")
                } else {
                    noteListPager(clickedFavRepo, "Repository")
                }
            }
        }
    }
}