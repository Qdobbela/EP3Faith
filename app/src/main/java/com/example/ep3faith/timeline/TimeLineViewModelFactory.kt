package com.example.ep3faith.timeline

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.profile.ProfileViewModel

class TimeLineViewModelFactory(
    private val dataSource: FaithDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TimeLineViewModel::class.java)){
            return TimeLineViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}