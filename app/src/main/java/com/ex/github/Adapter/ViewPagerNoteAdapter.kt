package com.ex.github.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ex.github.ui.AllNoteFragment
import com.ex.github.ui.MyNoteFragment

class ViewPagerNoteAdapter(fm : FragmentManager, lifecycle: Lifecycle, private var favorite : String, private var isUserOrRepository : String) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = MyNoteFragment()
                val bundle = Bundle()
                bundle.putString("favorite", favorite)
                bundle.putString("isUserOrRepository", isUserOrRepository)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = AllNoteFragment()
                val bundle = Bundle()
                bundle.putString("favorite", favorite)
                bundle.putString("isUserOrRepository", isUserOrRepository)
                fragment.arguments = bundle
                fragment
            }
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}