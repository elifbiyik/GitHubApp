package com.ex.github.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.ex.github.R
import com.ex.github.ViewModel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var useruid = viewModel.getCurrentUserUID()

        if (useruid != "") {
            var sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putString("uid", useruid).apply()

            // Splash ekran
            Handler(Looper.getMainLooper()).postDelayed({
                loadFragment(HomePageFragment())
            }, 2000)
        } else
        // Splash ekran
            Handler(Looper.getMainLooper()).postDelayed({
                loadFragment(SignInFragment())
            }, 2000)

    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.constraint, fragment)
            .addToBackStack(null)
            .commit()
    }
}