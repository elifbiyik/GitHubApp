package com.ex.github.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ex.github.ui.AllCommentFragment
import com.ex.github.ui.MyCommentFragment
import com.ex.github.ui.PageFollowersFragment

class ViewPagerCommentAdapter(fm : FragmentManager, lifecycle: Lifecycle,private var favUser : String) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = MyCommentFragment()
                val bundle = Bundle()
                bundle.putString("favUser", favUser)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = AllCommentFragment()
                val bundle = Bundle()
                bundle.putString("favUser", favUser)
                fragment.arguments = bundle
                fragment
            }
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}