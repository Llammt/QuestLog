package com.example.questlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TasksViewModel(val dao: TaskDao) : ViewModel() {
    private val _navigateToTask = MutableStateFlow<Long?>(null)
    val navigateToTask: StateFlow<Long?> = _navigateToTask

    private val _newTaskName = MutableStateFlow("")
    val newTaskName: StateFlow<String> = _newTaskName

    private val _newTaskText = MutableStateFlow("")
    val newTaskText: StateFlow<String> = _newTaskText

    private val _navigateToCreateTask = MutableStateFlow(false)
    val navigateToCreateTask: StateFlow<Boolean> = _navigateToCreateTask

    val tasks = dao.getAll()

    fun deleteTask(taskId : Long) {
        viewModelScope.launch {
            dao.deleteById(taskId)
        }
    }

    fun onTaskClicked(taskId: Long) {
        _navigateToTask.value = taskId
    }

    fun onTaskNavigated() {
        _navigateToTask.value = null
    }

    fun onCreateTaskClicked() {
        _navigateToCreateTask.value = true
    }

    fun onNavigated() {
        _navigateToCreateTask.value = false
    }
}
