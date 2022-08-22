package com.example.ep3faith.ui.inbox

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.ui.inbox.InboxViewModel

class InboxViewModelFactory(
    private val dataSource: FaithDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InboxViewModel::class.java)){
            return InboxViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}