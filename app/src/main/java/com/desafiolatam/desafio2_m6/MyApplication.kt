package com.desafiolatam.desafio2_m6

import android.app.Application
import androidx.room.Room
import com.desafiolatam.desafio2_m6.orm.TaskDatabase

class MyApplication: Application() {
    companion object {
        var taskDatabase: TaskDatabase? = null
    }
    override fun onCreate () {
        super .onCreate()
        taskDatabase = Room
            .databaseBuilder( this ,
                TaskDatabase:: class . java ,
                "task-master-db").build ()
    }

}