package com.ex.github.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ex.github.R
import com.ex.github.databinding.FragmentUserNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserNote : Fragment() {

    private lateinit var binding: FragmentUserNoteBinding



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



        return binding.root
    }
}