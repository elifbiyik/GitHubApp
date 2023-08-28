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
        var favUser = arguments?.getString("favUser") ?: ""
        // Tıklanan user
        var favRepo = arguments?.getString("favRepo") ?: ""
        // Tıklanan user

        if (favUser.isNotEmpty()) {
            binding.textView.text = favUser
            noteListPager(favUser, "User")
        } else if (favRepo.isNotEmpty()) {
            binding.textView.text = favRepo
            noteListPager(favRepo, "Repository")
        } else {
            binding.textView.text = ""
        }


        // TODO(butonu if else'lerin içine alsam ?? Firebasede Note/FavRepository ve Note/FavUser için)????
        // TODO(Repository'i ekleyince current User null dönüyor)
        binding.btnSend.setOnClickListener {
            addNote(favUser, favRepo)
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

    fun addNote(favUser: String?, favRepo: String?) {

        var currentUser = "mojombo"
        var note = binding.etNote.text.toString()

        lifecycleScope.launch {
            if (favUser?.isNotEmpty() == true) {
                var isAdded = viewModel.addNote(currentUser, favUser, note, "User")
                if (isAdded) {
                    noteListPager(favUser, "User")
                } else {
                    noteListPager(favUser, "User")
                }
            } else if (favRepo?.isNotEmpty() == true) {
                var isAdded = viewModel.addNote(currentUser, favRepo, note, "Repository")
                if (isAdded) {
                    noteListPager(favRepo, "Repository")
                } else {
                    noteListPager(favRepo, "Repository")
                }
            }
        }
    }
}