package com.example.ep3faith.ui.addPost

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.database.FaithDatabaseDAO


class EditPostViewModelFactory(
    private val dataSource: FaithDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditPostViewModel::class.java)){
            return EditPostViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}