package com.c23ps266.capstoneprojectnew.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val REQUEST_CODE_SETTINGS = 1
    private val SPEECH_REC = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPref = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val darkMode = sharedPref?.getBoolean("DARK_MODE", false) ?: false
        setDarkMode(darkMode)
        setContentView(binding.root)

        setSearch()
        setSpeech()
        setSetting()
        setLibrary()
    }

    private fun setSearch() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.trim() != "") {
                    Toast.makeText(this@MainActivity, query.toString(), Toast.LENGTH_SHORT).show()
                    searchView.setQuery("", false)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val speech = findViewById<ImageView>(R.id.btn_voice)
                if (newText.isNullOrEmpty()) {
                    speech.visibility = ImageView.VISIBLE
                } else {
                    speech.visibility = ImageView.GONE
                }
                return false
            }


        })
    }

    private fun setSetting() {
        val setting = findViewById<ImageView>(R.id.iv_setting)
        setting.setOnClickListener {
            intent = Intent(this, SettingActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SETTINGS)
        }

    }

    private fun setLibrary() {
        val library = findViewById<ImageView>(R.id.iv_library)
        library.setOnClickListener {
            intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSpeech() {
        val speech = findViewById<ImageView>(R.id.btn_voice)
        speech.setOnClickListener {
            if (!SpeechRecognizer.isRecognitionAvailable(this)) {
                Toast.makeText(this, "Speech Recognition is not available", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
                startActivityForResult(intent, SPEECH_REC)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == SPEECH_REC) && (resultCode == RESULT_OK) && (data != null)) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val searchView = findViewById<SearchView>(R.id.searchView)
            searchView.setQuery(result?.get(0), false)
        }

        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            val darkModeChanged = data?.getBooleanExtra("DARK_MODE_CHANGED", false) ?: false
            if (darkModeChanged) {
                val darkMode = sharedPref?.getBoolean("DARK_MODE", false) ?: false
                editor = sharedPref?.edit()
                editor?.putBoolean("DARK_MODE", !darkMode)
                editor?.apply()

                setDarkMode(!darkMode)
            }
            val languageChanged = data?.getBooleanExtra("LANGUAGE_CHANGED", false) ?: false
            if (languageChanged) {
                recreate()
            }
        }
    }


    private fun setDarkMode(darkMode: Boolean) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}