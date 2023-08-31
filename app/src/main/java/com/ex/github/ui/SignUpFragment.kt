package com.ex.github.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ex.github.R
import com.ex.github.ViewModel.SignUpViewModel
import com.ex.github.databinding.FragmentSignUpBinding
import com.ex.github.replace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            var nameSurname = binding.etNameSurname.text.toString()
            var phone = binding.etPhone.text.toString()

            lifecycleScope.launch {
                viewModel.signUp(nameSurname, phone)
            }
        }

        viewModel.userMutableLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                replace(SignInFragment())
            } else {
                Toast.makeText(context, "Try again ..", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }



}