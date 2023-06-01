package com.c23ps266.capstoneprojectnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.c23ps266.capstoneprojectnew.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        setLogin()
    }

    private fun setLogin() {
        val signInFunction = viewModel.signIn(this) { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val errorMessage = task.exception?.message
                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.signInButton.setOnClickListener {
            signInFunction.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}