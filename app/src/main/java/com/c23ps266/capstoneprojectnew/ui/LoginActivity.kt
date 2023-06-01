package com.c23ps266.capstoneprojectnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c23ps266.capstoneprojectnew.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLogin()
    }

    private fun setLogin() {
        val signInFunction = viewModel.signIn(this) { task ->
            if (task.isSuccessful) {
                startMainActivity()
            } else {
                val errorMessage = task.exception?.message
                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.signInButton.setOnClickListener {
            signInFunction.invoke()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.isLoggedIn()) {
            startMainActivity()
        }
    }
}