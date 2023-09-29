package com.desafiolatam.desafio2_m6.orm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(TaskEntity::class)], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun getTaskDao(): TasksDao
}

