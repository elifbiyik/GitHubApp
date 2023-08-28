package com.ex.github.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ex.github.ui.FavoriteRepositoryFragment
import com.ex.github.ui.FavoriteUserFragment

class ViewPagerFavoriteAdapter (fm : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteUserFragment()
            1 -> FavoriteRepositoryFragment()
            else -> FavoriteUserFragment()
        }
    }
}