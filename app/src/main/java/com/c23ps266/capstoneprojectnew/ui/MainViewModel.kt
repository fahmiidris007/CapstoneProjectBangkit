package com.c23ps266.capstoneprojectnew.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.c23ps266.capstoneprojectnew.data.AppRepository
import com.c23ps266.capstoneprojectnew.model.UserModel

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
}
