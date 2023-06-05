package com.c23ps266.capstoneprojectnew.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.c23ps266.capstoneprojectnew.data.remote.RequestResult
import com.c23ps266.capstoneprojectnew.data.remote.retrofit.ApiService
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.c23ps266.capstoneprojectnew.util.FirebaseAuthHelper
import retrofit2.HttpException

class AppRepository private constructor(
    private val apiService: ApiService,
    private val firebaseAuthHelper: FirebaseAuthHelper,
) {
    val prepareSignIn = firebaseAuthHelper::prepareSignIn
    val signOut = firebaseAuthHelper.auth::signOut
    fun getUser() = firebaseAuthHelper.auth.currentUser

    fun submitEmotion(emotion: String): LiveData<RequestResult<List<AudioModel>>> = liveData {
        emit(RequestResult.Loading)

        try {
            val response = apiService.submitEmotion(emotion)
            val data = response.map { AudioModel(it.title, it.duration, it.link) }
            if (data.isEmpty())
                throw IllegalStateException("Invalid emotion")
            emitSource(MutableLiveData(RequestResult.Success(data)))
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            emit(RequestResult.Error(e.message.toString()))
        }
    }

    companion object {
        const val TAG = "AppRepository"

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
