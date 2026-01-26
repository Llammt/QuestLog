package com.example.questlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TasksViewModel(val dao: TaskDao) : ViewModel() {
    val newTaskName = MutableLiveData("")
    val newTaskText = MutableLiveData("")

    val tasks = dao.getAll()

    private val _navigateToTask = MutableLiveData<Long?>()
    val navigateToTask: LiveData<Long?>
        get() = _navigateToTask

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
        }
    }

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
}