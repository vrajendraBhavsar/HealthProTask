package com.example.healthprotask.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.healthprotask.R
import com.example.healthprotask.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
        val navController = navHostFragment?.navController
//        val navController = findNavController(binding.container.id)
        val config =
            navController?.graph?.let { AppBarConfiguration(navGraph = it) } //responsible for managing navigation button

        navController?.let {
            config?.let { it1 ->
                binding.toolbar.setupWithNavController(it, it1)
            }
        }
    }
}