package com.c23ps266.capstoneprojectnew.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c23ps266.capstoneprojectnew.data.AppRepository
import com.c23ps266.capstoneprojectnew.data.Injection

class ViewModelFactory private constructor(
    private val repository: AppRepository,
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = INSTANCE ?: synchronized(this) {
            ViewModelFactory(Injection.provideRepository(context)).also { INSTANCE = it }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // @formatter:off
            modelClass.isAssignableFrom(MainViewModel::class.java)        -> MainViewModel(repository)
            modelClass.isAssignableFrom(LoginViewModel::class.java)       -> LoginViewModel(repository)
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name )
            // @formatter:on
        } as T
    }
}
