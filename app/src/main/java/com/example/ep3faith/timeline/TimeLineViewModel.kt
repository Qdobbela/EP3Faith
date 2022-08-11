package com.example.ep3faith.timeline

import androidx.lifecycle.ViewModel
import timber.log.Timber

class TimeLineViewModel: ViewModel() {
    init {
        Timber.i("Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }
}