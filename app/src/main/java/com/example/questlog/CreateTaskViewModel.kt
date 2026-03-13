package com.example.questlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateTaskViewModel(val dao: TaskDao) : ViewModel() {
    val newTaskName = MutableStateFlow("")
    val newTaskText = MutableStateFlow("")
    private val _navigateToList = MutableStateFlow<Boolean>(false)
    val navigateToList: MutableStateFlow<Boolean> = _navigateToList

    fun addTask() {
        viewModelScope.launch {
            val name = newTaskName.value
            val text = newTaskText.value

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
