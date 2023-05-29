package com.c23ps266.capstoneprojectnew.data

import com.c23ps266.capstoneprojectnew.data.remote.retrofit.ApiService
import com.c23ps266.capstoneprojectnew.util.FirebaseAuthHelper

class AppRepository constructor(
    private val apiService: ApiService,
    private val firebaseAuthHelper: FirebaseAuthHelper,
) {
    val submitEmotion = apiService::submitEmotion

    val prepareSignIn = firebaseAuthHelper::prepareSignIn
    val signOut = firebaseAuthHelper.auth::signOut
    fun getUser() = firebaseAuthHelper.auth.currentUser

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(
            apiService: ApiService,
            firebaseAuthHelper: FirebaseAuthHelper,
        ): AppRepository = INSTANCE ?: synchronized(this) {
            AppRepository(apiService, firebaseAuthHelper).also { INSTANCE = it }
        }
    }
}
