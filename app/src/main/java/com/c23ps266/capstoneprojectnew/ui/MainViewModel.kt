package com.c23ps266.capstoneprojectnew.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.c23ps266.capstoneprojectnew.data.AppRepository
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.c23ps266.capstoneprojectnew.model.UserModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val emotionInput = MutableLiveData<String>()
    val submitStatus = emotionInput.switchMap { repository.submitEmotion(it) }
    fun submitEmotion(emotion: String) {
        emotionInput.value = emotion
    }

    fun getUserData(): UserModel? = repository.getUser()?.run { UserModel(displayName, email, photoUrl) }
    fun signOut() = repository.signOut()

    fun addToFavorite(audio: AudioModel) = viewModelScope.launch { repository.addAudioToFavorite(audio) }
    fun isFavorited(title: String) = repository.isAudioFavorite(title)
    fun removeFromFavorite(title: String) = viewModelScope.launch { repository.removeAudioFromFavorite(title) }
}
