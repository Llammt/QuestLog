package com.example.questlog

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {
    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = db.taskDao
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun recyclerView_showsCavesCorrectData() = runBlocking {
        @RunWith(AndroidJUnit4::class)
        class RecyclerViewTest {
            private lateinit var db: TaskDatabase
            private lateinit var dao: TaskDao

            @Before
            fun createDb() {
                db = Room.inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    TaskDatabase::class.java
                )
                    .allowMainThreadQueries()
                    .build()

                dao = db.taskDao
            }

            @After
            fun closeDb() {
                db.close()
            }

            @Test
            fun recyclerView_showsCavesCorrectData() = runBlocking {
                dao.insert(Task(taskName = "Epic quest", taskDone = false))
                dao.insert(Task(taskName = "Another epic quest", taskDone = false))
                dao.insert(Task(taskName = "Not such an epic quest", taskDone = false))

                launchFragmentInContainer<TasksFragment>()
                onView(withText("Epic quest")).check(matches(isDisplayed()))
                onView(withText("Another epic quest")).check(matches(isDisplayed()))
                onView(withText("Not such an epic quest")).check(matches(isDisplayed()))
            }
        }
    }
}