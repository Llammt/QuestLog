package com.example.questlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CreateTaskViewModel(val dao: TaskDao) : ViewModel() {
    val newTaskName = MutableLiveData("")
    val newTaskText = MutableLiveData("")
    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    fun addTask() {
        viewModelScope.launch {
            val name = newTaskName.value ?: ""
            val text = newTaskText.value ?: ""

            if (name.isNotBlank()) {
                val task = Task(
                    taskName = name,
                    taskText = text
                )
                dao.insert(task)

                newTaskName.value = ""
                newTaskText.value = ""
            }
            _navigateToList.value = true
        }
    }

    fun onNavigatedToList() {
        _navigateToList.value = false
    }
}