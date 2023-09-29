package com.desafiolatam.desafio2_m6.orm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): List<TaskEntity>

    @Insert
    fun insertTask(vararg taskEntity: TaskEntity)

    @Query("DELETE FROM tasks_table")
    fun deleteAllTasks()

    @Update
    fun updateTask(taskEntity: TaskEntity)
}