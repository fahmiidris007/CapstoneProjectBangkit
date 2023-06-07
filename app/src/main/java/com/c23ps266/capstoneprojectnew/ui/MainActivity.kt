package com.c23ps266.capstoneprojectnew.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.data.local.AudioListCache
import com.c23ps266.capstoneprojectnew.databinding.ActivityMainBinding
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Periksa apakah AudioListCache sudah berisi data
        if (AudioListCache.getAudioList().isEmpty()) {
            // Jika tidak ada data, lakukan pengambilan data pertama kali
        } else {
            // Jika sudah ada data, perbarui tampilan menggunakan data yang ada di AudioListCache
            val audioData = AudioListCache.getAudioList()
            updateList(audioData)
        }
        setNavigationTabs()
    }

    private fun updateList(audioData: ArrayList<AudioModel>) {
        val fragment = supportFragmentManager.findFragmentById(R.id.bottom_fragment)
        if (fragment is HomeFragment) {
            fragment.updateList(audioData)
        }
    }

    private fun setNavigationTabs() {
        val navView: BottomNavigationView = findViewById(R.id.bottom)
        val navController = findNavController(R.id.bottom_fragment)
        navView.setupWithNavController(navController)
    }
}