package com.c23ps266.capstoneprojectnew.data

import com.c23ps266.capstoneprojectnew.data.remote.retrofit.ApiConfig
import com.c23ps266.capstoneprojectnew.util.FirebaseAuthHelper

object Injection {
    fun provideRepository() : AppRepository {
        val apiService = ApiConfig.getApiService()
        val firebaseAuthHelper = FirebaseAuthHelper.getInstance();
        return AppRepository(apiService, firebaseAuthHelper)
    }
}
