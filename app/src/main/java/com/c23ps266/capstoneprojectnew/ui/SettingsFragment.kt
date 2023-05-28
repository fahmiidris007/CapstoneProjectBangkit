package com.c23ps266.capstoneprojectnew.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var modeSwitch: SwitchCompat
    private var darkMode: Boolean = false
    private var editor: SharedPreferences.Editor? = null
    private var sharedPref: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDarkMode()
        setLanguage()
    }

    private fun setLanguage() {
        binding.selectLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }
        val currentLocale = resources.configuration.locale
        val currentLanguage = currentLocale.language
        if (currentLanguage == "in") {
            binding.language.text = getString(R.string.language)
            binding.english.text = getString(R.string.indonesia)
        } else {
            binding.language.text = getString(R.string.language)
            binding.english.text = getString(R.string.english)
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf(getString(R.string.english), getString(R.string.indonesia))

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.select_language))
            .setItems(languages) { _, which ->
                val selectedLanguage = if (which == 0) "en" else "in"
                setAppLanguage(selectedLanguage)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun setAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("LANGUAGE_CHANGED", true)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setDarkMode() {
        modeSwitch = binding.modeSwitch
        sharedPref = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE)
        darkMode = sharedPref?.getBoolean("DARK_MODE", false)!!
        if (darkMode) {
            modeSwitch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            modeSwitch.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        modeSwitch.setOnCheckedChangeListener { _, state ->
            if (darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedPref?.edit()
                editor?.putBoolean("DARK_MODE", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPref?.edit()
                editor?.putBoolean("DARK_MODE", true)
            }
            editor?.apply()
            val intent = Intent()
            intent.putExtra("DARK_MODE_CHANGED", true)
            requireActivity().setResult(Activity.RESULT_OK, intent)
        }
    }

}