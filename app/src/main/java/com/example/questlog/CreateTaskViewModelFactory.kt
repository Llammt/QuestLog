package com.example.questlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateTaskViewModelFactory(private val dao: TaskDao)  : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
            return CreateTaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}