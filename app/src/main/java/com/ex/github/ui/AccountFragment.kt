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
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ex.github.Adapter.ViewPagerAccountAdapter
import com.ex.github.R
import com.ex.github.ViewModel.AccountViewModel
import com.ex.github.databinding.FragmentAccountBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAccountAdapter

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

        noteListPager()

        lifecycleScope.launch {
            var list = viewModel.currentUser()
            var nameSurname = list[0]
            var phone = list[1]
            binding.accNameSurname.setText(nameSurname)
            binding.accPhone.setText(phone)

            binding.accPhone.isEnabled = false
            binding.accNameSurname.isEnabled = false

            viewModel.getPhoto("+90${phone}")
            
            var size = viewModel.showFavoriteUser(nameSurname)
            binding.tvFollowing.text = size.toString()
        }

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

        binding.signOut.setOnClickListener {
            viewModel.signOut()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.constraint, SignInFragment()).addToBackStack(null).commit()
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            var phone = binding.accPhone.text.toString()
            viewModel.updateUserProfilePhoto("+90${phone}", imageUri)
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

    fun noteListPager() {
        adapter = ViewPagerAccountAdapter(childFragmentManager, lifecycle)
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "My Note"
                1 -> tab.text = "All Note"
                else -> tab.text = "Undefined"
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}