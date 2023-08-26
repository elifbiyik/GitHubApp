package com.ex.github.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.github.Adapter.MyCommentAdapter
import com.ex.github.ViewModel.MyCommentViewModel
import com.ex.github.databinding.FragmentMyCommentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyCommentFragment : Fragment() {

    private lateinit var binding: FragmentMyCommentBinding
    private val viewModel: MyCommentViewModel by viewModels()
    private lateinit var adapter: MyCommentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyCommentBinding.inflate(inflater, container, false)


        val favUser = arguments?.getString("favUser").toString()


        lifecycleScope.launch {
            var currentUser = "mojombo"
            var list = viewModel.getMyComment(currentUser, favUser)
            Log.d("xxxxFirebaseListCommentFrag", list.toString())

            adapter = MyCommentAdapter(list)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())


            viewModel.currentUserMyCommentMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "...", Toast.LENGTH_SHORT).show()
                }
            })

        }

        return binding.root
    }
}