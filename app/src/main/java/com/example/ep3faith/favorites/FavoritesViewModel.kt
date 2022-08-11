package com.example.ep3faith.favorites

import androidx.lifecycle.ViewModel
import timber.log.Timber

class FavoritesViewModel: ViewModel() {
    init {
        Timber.i("Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }
}