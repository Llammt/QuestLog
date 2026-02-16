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

        // GIVEN — создаём задачу
        val task = Task(
            taskName = "Epic quest",
            taskDone = false
        )

        // WHEN — вставляем
        val testTaskId = dao.insert(task)

        // THEN — читаем обратно
        val loadedTask = dao.getById(testTaskId)

        assertEquals("Epic quest", loadedTask.taskName)
        assertFalse(loadedTask.taskDone)
    }
}
