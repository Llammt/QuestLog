package com.example.questlog

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao : TaskDao

    companion object {
        @Volatile
        private var INSTANCE : TaskDatabase? = null

        fun getInstance(context: Context) : TaskDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "tasks_database")
                        .fallbackToDestructiveMigration(true)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}