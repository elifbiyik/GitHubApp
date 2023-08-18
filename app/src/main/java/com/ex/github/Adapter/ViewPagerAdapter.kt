package com.ex.github.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ex.github.ui.PageFollowersFragment
import com.ex.github.ui.PageFollowingFragment
import com.ex.github.ui.PageGistsFragment
import com.ex.github.ui.PageRepositoryFragment

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val COUNT = 4

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PageFollowersFragment()
            1 -> PageFollowingFragment()
            2 -> PageRepositoryFragment()
            3 -> PageGistsFragment()
            else -> PageFollowersFragment()
        }
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab " + (position + 1)
    }
}
