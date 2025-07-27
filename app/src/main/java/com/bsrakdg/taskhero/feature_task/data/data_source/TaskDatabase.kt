package com.bsrakdg.taskhero.feature_task.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bsrakdg.taskhero.feature_task.domain.model.Task

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

}