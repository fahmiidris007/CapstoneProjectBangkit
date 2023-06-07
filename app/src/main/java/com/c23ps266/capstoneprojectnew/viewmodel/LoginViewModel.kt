package com.c23ps266.capstoneprojectnew.viewmodel

import androidx.lifecycle.ViewModel
import com.c23ps266.capstoneprojectnew.data.AppRepository

class LoginViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    val signIn = repository.prepareSignIn
    fun isLoggedIn() = repository.getUser() != null
}
