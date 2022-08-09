package com.example.ep3faith.profile

import androidx.lifecycle.ViewModel
import timber.log.Timber

class ProfileViewModel: ViewModel() {
    init {
        Timber.i("Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }
}