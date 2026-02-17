package com.example.questlog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task) : Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks_table WHERE taskId = :taskId")
    suspend fun deleteById(taskId: Long)

    @Query("SELECT * FROM tasks_table WHERE taskId = :taskId")
    fun get(taskId: Long): LiveData<Task>

    @Query("SELECT * FROM tasks_table ORDER BY taskId DESC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE taskId = :id")
    suspend fun getById(id: Long): Task?

}