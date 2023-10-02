package com.ex.github.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ex.github.R
import com.ex.github.ViewModel.SignInViewModel
import com.ex.github.databinding.FragmentSignInBinding
import com.ex.github.replace
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.btnSignup.setOnClickListener {
            replace(SignUpFragment())
        }

        binding.btnSignIn.setOnClickListener {
            var phone = binding.etPhone.text.toString()

            lifecycleScope.launch {
                var isRegister = viewModel.isRegistered(phone)

                if (phone.isEmpty()) {
                    Toast.makeText(context, " Enter phone number", Toast.LENGTH_SHORT).show()
                } else if (phone.length < 10) {
                    Toast.makeText(context, "Missing", Toast.LENGTH_SHORT).show()
                } else if (!isRegister) {
                    Toast.makeText(context, " You must register", Toast.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        viewModel.verify(requireActivity(), phone)
                        showAlertDialog(requireContext())
                        // Kullanıcının phone bilgisiyle fotoğrafına ulaşıp dB'ye kaydedicez. (Followers için)
                    }
                }
            }
        }
        return binding.root
    }

    private fun showAlertDialog(context: Context) {
        val inputEditTextField = EditText(requireActivity())
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        alertDialogBuilder.setTitle("Verify")
        alertDialogBuilder.setMessage("Verification Code")
        alertDialogBuilder.setView(inputEditTextField)

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            val etVerificationCode = inputEditTextField.text.toString()
            val verificationId = viewModel.getVerificationId()

            if (etVerificationCode.isEmpty()) {
                Toast.makeText(context, "You must enter code", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val credential =
                        PhoneAuthProvider.getCredential(
                            verificationId.toString(),
                            etVerificationCode
                        )  // Doğrulama kimliği
                    var isSuccess = viewModel.signInWithCredential(requireContext(), credential)
                    if (isSuccess) {
                        replace(HomePageFragment())
                    } else {
                        dialog.dismiss()
                    }
                }
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}