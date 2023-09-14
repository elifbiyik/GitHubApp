package com.ex.github.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_GALLERY = 1

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

        binding.uploadProfilePhoto.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            builder.setTitle("Select Image")
            builder.setMessage("Choose your option")

            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        binding.btnSignUp.setOnClickListener {
            var phone = binding.etPhone.text.toString()
            var nameSurname = binding.etNameSurname.text.toString()
            lifecycleScope.launch {
                viewModel.signUp(nameSurname, phone)
            }
        }

        if (view != null) {
            viewModel.userMutableLiveData.observe(viewLifecycleOwner, Observer {
                if (it) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    replace(SignInFragment())
                } else {
                    Toast.makeText(context, "Try again ..", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
           var phone = binding.etPhone.text.toString()
            if(imageUri != null) {
                viewModel.uploadProfilePhoto(phone, imageUri)
            } else {
                val drawableResId = R.drawable.ic_account_circle_24
                val uri = Uri.parse("android.resource://com.ex.github/$drawableResId")
                viewModel.uploadProfilePhoto(phone, uri)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    binding.uploadProfilePhoto.text = selectedImageUri.toString()
                    imageUri = selectedImageUri!!
                }
            }
        }
    }
}



