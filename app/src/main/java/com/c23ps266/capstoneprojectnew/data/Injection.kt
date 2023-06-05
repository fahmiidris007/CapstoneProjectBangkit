package com.c23ps266.capstoneprojectnew.data

import android.content.Context
import com.c23ps266.capstoneprojectnew.data.local.AudioDatabase
import com.c23ps266.capstoneprojectnew.data.remote.retrofit.ApiConfig
import com.c23ps266.capstoneprojectnew.util.FirebaseAuthHelper

object Injection {
    fun provideRepository(context: Context) : AppRepository {
        val apiService = ApiConfig.getApiService()
        val firebaseAuthHelper = FirebaseAuthHelper.getInstance();
        val dao = AudioDatabase.getInstance(context).audioDao()
        return AppRepository.getInstance(apiService, firebaseAuthHelper, dao)
    }
}
