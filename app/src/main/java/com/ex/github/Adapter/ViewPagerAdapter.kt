package com.ex.github.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ex.github.ui.PageFollowersFragment
import com.ex.github.ui.PageFollowingFragment
import com.ex.github.ui.PageRepositoryFragment


class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, val currentUser : String) :
    FragmentStateAdapter(fm, lifecycle) {
//class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {


    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = PageFollowersFragment()
                val bundle = Bundle()
                bundle.putString("login", currentUser)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = PageFollowingFragment()
                val bundle = Bundle()
                bundle.putString("login", currentUser)
                fragment.arguments = bundle
                fragment
            }
            2 -> {
                val fragment = PageRepositoryFragment()
                val bundle = Bundle()
                bundle.putString("login", currentUser)
                fragment.arguments = bundle
                fragment
            }
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
