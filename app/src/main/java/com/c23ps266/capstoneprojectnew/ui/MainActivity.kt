package com.c23ps266.capstoneprojectnew.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationTabs()


    }

    private fun setNavigationTabs() {
        val navView: BottomNavigationView = findViewById(R.id.bottom)
        val navController = findNavController(R.id.bottom_fragment)
        navView.setupWithNavController(navController)
    }
}