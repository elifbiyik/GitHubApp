package com.ex.github.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.ex.github.R
import com.ex.github.ViewModel.SignInViewModel
import com.ex.github.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.black)
        }

        var useruid = viewModel.getCurrentUserUID()

        if (useruid != "") {
            var sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putString("uid", useruid).apply()

            Handler(Looper.getMainLooper()).postDelayed({
                loadFragment(HomePageFragment())
            }, 2000)
        } else
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