package com.ex.github.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ex.github.R
import com.ex.github.ViewModel.SignInViewModel
import com.ex.github.databinding.FragmentSignInBinding
import com.ex.github.replace
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()


  //  private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    //lateinit var verificationId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.xx.setOnClickListener {
            replace(HomePageFragment())
        }

        binding.btnSignup.setOnClickListener {
            replace(SignUpFragment())
        }

        binding.btnSignIn.setOnClickListener {
            var phone = binding.etPhone.text.toString()
            lifecycleScope.launch {
                Log.d("Telefon numarası", phone)

                viewModel.verify(requireActivity(), phone)
                showAlertDialog(requireContext(), phone)
            }
        }
        return binding.root
    }


    private fun showAlertDialog(context: Context, phone: String) {

        val inputEditTextField = EditText(requireActivity())

        val alertDialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        alertDialogBuilder.setTitle("Verify")
        alertDialogBuilder.setMessage("Verification Code")
        alertDialogBuilder.setView(inputEditTextField)

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            val verificationCode = inputEditTextField.text.toString()


           /* val verificationId = viewModel.signIn()
            if (verificationId != null) {
                val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
                viewModel.signInWithCredential(credential)
            } else {
            }*/



        }


        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}