package com.c23ps266.capstoneprojectnew.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
//    private lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInButton.setOnClickListener {
//            signIn()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        resultLauncher.launch(signInIntent)
//    }
//
//    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user: FirebaseUser? = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
//                }
//            }
//    }
//
//    private fun updateUI(currentUser: FirebaseUser?) {
//        if (currentUser != null){
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            finish()
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
//
//    companion object {
//        private const val TAG = "LoginActivity"
//    }
}