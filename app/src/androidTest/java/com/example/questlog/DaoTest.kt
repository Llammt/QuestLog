package com.example.questlog

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import kotlin.text.insert

@RunWith(AndroidJUnit4::class)
class TaskDaoInsertTest {

    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        )
            .allowMainThreadQueries() //for tests only!
            .build()

        dao = db.taskDao
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertTask_savesCorrectData() = runBlocking {

        val task = Task(
            taskName = "Epic quest",
            taskText = "Do epic stuff",
            taskDone = false
        )

        val testTaskId = dao.insert(task)

        val loadedTask = dao.getById(testTaskId)

        assertEquals("Epic quest", loadedTask!!.taskName)
        assertEquals("Do epic stuff", loadedTask.taskText)
        assertFalse(loadedTask.taskDone)
    }

    @Test
    fun editTask_savesCorrectData() = runBlocking {

        val originalTask = Task(
            taskName = "Epic quest",
            taskText = "Do epic stuff",
            taskDone = false
        )

        val testTaskId = dao.insert(originalTask)

        val taskToUpdate = dao.getById(testTaskId)

        taskToUpdate!!.taskName = "Another epic quest"
        taskToUpdate.taskText = "Do another epic stuff"
        taskToUpdate.taskDone = true

        dao.update(taskToUpdate)

        val updatedTask = dao.getById(testTaskId)

        assertEquals("Another epic quest", updatedTask!!.taskName)
        assertEquals("Do another epic stuff", updatedTask.taskText)
        assertTrue(updatedTask.taskDone)
    }

    @Test
    fun deleteTask_deleteDataCorrectly() = runBlocking {

        val task = Task(
            taskName = "Epic quest",
            taskText = "Do epic stuff",
            taskDone = false
        )

        val testTaskId = dao.insert(task)

        val loadedTask = dao.getById(testTaskId)
        dao.delete(loadedTask!!)

        // Assert
        val deletedTask = dao.getById(testTaskId)
        assertNull(deletedTask)
    }

    @Test
    fun deleteTaskById_deleteDataCorrectly() = runBlocking {

        val task = Task(
            taskName = "Epic quest",
            taskText = "Do epic stuff",
            taskDone = false
        )

        val testTaskId = dao.insert(task)

        val loadedTask = dao.getById(testTaskId)
        dao.deleteById(loadedTask!!.taskId)

        // Assert
        val deletedTask = dao.getById(testTaskId)
        assertNull(deletedTask)
    }
}
