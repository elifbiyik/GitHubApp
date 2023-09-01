package com.ex.github.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ex.github.ImageLoad
import com.ex.github.R
import com.ex.github.ViewModel.AccountViewModel
import com.ex.github.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()

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

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        var phone = arguments?.getString("Phone").toString() //"+905453950891"
        binding.accPhone.setText(phone)

        viewModel.getPhoto(phone)

        binding.accPhone.isEnabled = false
        binding.accNameSurname.isEnabled = false

        binding.edit.setOnClickListener {
            if (binding.edit.text == "Done") {
                binding.edit.text = "Edit"
                binding.editImage.visibility = View.GONE
                Toast.makeText(context, " Update", Toast.LENGTH_SHORT).show()
            } else {
                binding.edit.text = "Done"
                binding.editImage.visibility = View.VISIBLE
            }
        }

        binding.editImage.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            builder.setTitle("Select Image")
            builder.setMessage("Choose your option")
            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


        viewModel.userProfilePhotoMutableLiveData.observe(viewLifecycleOwner, Observer { uri ->
            Glide.with(requireContext())
                .load(uri)
                .into(binding.accIm)
        })
        return binding.root
    }

    override fun onPause() {
        super.onPause()

        lifecycleScope.launch {
            var phone = binding.accPhone.text.toString()
            viewModel.updateUserProfilePhoto(phone, imageUri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    binding.accIm.setImageURI(selectedImageUri)
                    imageUri = selectedImageUri!!
                }
            }
        }
    }
}