package com.c23ps266.capstoneprojectnew.util

import android.app.Activity
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.c23ps266.capstoneprojectnew.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthHelper private constructor() {
    val auth: FirebaseAuth = Firebase.auth

    /**
     * This function prepares the sequence of signing in a user using firebase. Call this function
     * before the activity passed in reached RESUME state. The returned function can be invoked
     * anywhere (e.g. button click listener) to launch the actual sign in dialog.
     * @return an invocable function that launches the firebase google sign in dialog
     * @throws IllegalStateException when attempting to call this function while the activity
     * state is RESUMED
     */
    fun prepareSignIn(
        activity: AppCompatActivity,
        onAuthResult: FirebaseAuthHelper.(task: Task<AuthResult>) -> Unit,
    ): () -> Unit = activity.run {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val resultLauncherKey = "signin"
        val resultLauncher = activityResultRegistry.register(
            resultLauncherKey, activity, StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                    val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(activity) { this@FirebaseAuthHelper.onAuthResult(it) }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

        return {
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
    }

    companion object {
        const val TAG = "FirebaseAuthHelper"

        @Volatile
        private var INSTANCE: FirebaseAuthHelper? = null

        fun getInstance(): FirebaseAuthHelper = INSTANCE ?: synchronized(this) {
            FirebaseAuthHelper().also { INSTANCE = it }
        }
    }
}
