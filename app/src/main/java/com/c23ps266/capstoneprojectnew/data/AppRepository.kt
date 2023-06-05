package com.c23ps266.capstoneprojectnew.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.c23ps266.capstoneprojectnew.data.local.AudioDao
import com.c23ps266.capstoneprojectnew.data.remote.RequestResult
import com.c23ps266.capstoneprojectnew.data.remote.retrofit.ApiService
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.c23ps266.capstoneprojectnew.util.FirebaseAuthHelper

class AppRepository private constructor(
    private val apiService: ApiService,
    private val firebaseAuthHelper: FirebaseAuthHelper,
    private val audioDao: AudioDao,
) {
    val prepareSignIn = firebaseAuthHelper::prepareSignIn
    fun signOut() = firebaseAuthHelper.auth.signOut()
    fun getUser() = firebaseAuthHelper.auth.currentUser

    fun getFavoriteAudios() = audioDao.getAll()
    fun getFavoriteAudio(title: String) = audioDao.get(title)
    fun isAudioFavorite(title: String) = audioDao.isExist(title)
    suspend fun addAudioToFavorite(audio: AudioModel) = audioDao.insert(audio)
    suspend fun removeAudioFromFavorite(title: String) = audioDao.delete(title)
    suspend fun removeAllFavorite() = audioDao.deleteAll()

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
            audioDao: AudioDao,
        ): AppRepository = INSTANCE ?: synchronized(this) {
            AppRepository(apiService, firebaseAuthHelper, audioDao).also { INSTANCE = it }
        }
    }
}
