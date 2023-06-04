package com.c23ps266.capstoneprojectnew.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.c23ps266.capstoneprojectnew.data.remote.RequestResult
import com.c23ps266.capstoneprojectnew.databinding.FragmentHomeBinding
import com.c23ps266.capstoneprojectnew.util.TextClassifierHelper
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val REQUEST_CODE_SETTINGS = 1
    private val SPEECH_REC = 101
    private lateinit var textClassifierHelper: TextClassifierHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val darkMode = sharedPref?.getBoolean("DARK_MODE", false) ?: false
        setDarkMode(darkMode)

        val modelDetail = TextClassifierHelper.ModelDetail(
            modelFileName = "tflite_model_v3.2.tflite",
            labelJsonFileName = "tflite_model_labels.json",
            inputMaxLen = 32
        )
        textClassifierHelper = TextClassifierHelper(requireContext(), modelDetail) {
            setSearch()
            setSpeech()
        }

        setDisplayName()

        binding.btnPlay.setOnClickListener {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSearch() {
        viewModel.submitStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                RequestResult.Loading    -> {}

                is RequestResult.Error   -> Toast.makeText(
                    requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT
                ).show()

                is RequestResult.Success -> {
                    val audioLinks = result.data
                    val audio = audioLinks.random()
                    val intent = Intent(requireContext(), PlayerActivity::class.java)
                    intent.putExtra(PlayerActivity.EXTRA_AUDIO_URL, audio)
                    startActivity(intent)
                }
            }
        }

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.trim() != "") {
                    searchView.setQuery("", false)
                    textClassifierHelper.classify(query.toString()) { result ->
                        val emotion = result.maxBy { it.score }
                        viewModel.submitEmotion(emotion.label)
                        Log.d(TAG, "emotion: ${emotion.label} | ${emotion.score}")
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val speech = binding.btnVoice
                if (newText.isNullOrEmpty()) {
                    speech.visibility = ImageView.VISIBLE
                } else {
                    speech.visibility = ImageView.GONE
                }
                return false
            }
        })
    }

    private fun setSpeech() {
        val speech = binding.btnVoice
        speech.setOnClickListener {
            if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
                Toast.makeText(
                    requireContext(),
                    "Speech Recognition is not available",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
                startActivityForResult(intent, SPEECH_REC)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == SPEECH_REC) && (resultCode == Activity.RESULT_OK) && (data != null)) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val searchView = binding.searchView
            searchView.setQuery(result?.get(0), false)
        }

        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == Activity.RESULT_OK) {
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
                requireActivity().recreate()
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

    private fun setDisplayName() {
        binding.tvUsername.text = viewModel.getUserData()?.name
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}
