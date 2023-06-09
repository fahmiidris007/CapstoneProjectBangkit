package com.c23ps266.capstoneprojectnew.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.FragmentSettingsBinding
import com.c23ps266.capstoneprojectnew.viewmodel.MainViewModel
import com.c23ps266.capstoneprojectnew.viewmodel.ViewModelFactory
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(requireActivity()) }
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
        setLogout()
        setDisplayUser()
    }

    private fun setLanguage() {
        binding.selectLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }
        val currentLocale = resources.configuration.locale
        val currentLanguage = currentLocale.language
        if (currentLanguage == LANGUAGE_INDONESIAN) {
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
        builder.setTitle(getString(R.string.select_language)).setItems(languages) { _, which ->
            val selectedLanguage = if (which == 0) LANGUAGE_ENGLISH else LANGUAGE_INDONESIAN
            setAppLanguage(selectedLanguage)
        }.setNegativeButton(getString(R.string.cancel), null).show()
    }

    private fun setAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(EXTRA_LANGUAGE_CHANGED, true)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setDarkMode() {
        modeSwitch = binding.modeSwitch
        sharedPref = requireActivity().getSharedPreferences(PREF_DARK_MODE_NAME, Context.MODE_PRIVATE)
        darkMode = sharedPref?.getBoolean(PREF_DARK_MODE_KEY, false)!!
        if (darkMode) {
            modeSwitch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            modeSwitch.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        modeSwitch.setOnCheckedChangeListener { _, _ ->
            if (darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedPref?.edit()
                editor?.putBoolean(PREF_DARK_MODE_KEY, false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPref?.edit()
                editor?.putBoolean(PREF_DARK_MODE_KEY, true)
            }
            editor?.apply()
            val intent = Intent()
            intent.putExtra(EXTRA_DARK_MODE_CHANGED, true)
            requireActivity().setResult(Activity.RESULT_OK, intent)
        }
    }

    private fun setDisplayUser() = viewModel.getUserData()?.run {
        photoUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .placeholder(binding.profilePicture.drawable)
                .circleCrop()
                .into(binding.profilePicture)
        }
        Log.d(TAG, photoUrl.toString())
        binding.username.text = name
        binding.email.text = email
    }

    private fun setLogout() {
        binding.logout.setOnClickListener {
            viewModel.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
        const val PREF_DARK_MODE_NAME = "MODE"
        const val PREF_DARK_MODE_KEY = "DARK_MODE"
        const val EXTRA_DARK_MODE_CHANGED = "DARK_MODE_CHANGED"
        const val EXTRA_LANGUAGE_CHANGED = "LANGUAGE_CHANGED"
        private const val LANGUAGE_INDONESIAN = "in"
        private const val LANGUAGE_ENGLISH = "en"
    }
}
