package com.bsrakdg.taskhero.feature_task.data.repository

import com.bsrakdg.taskhero.feature_task.data.data_source.TaskDao
import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    override suspend fun getTaskById(id: Int): Task? = dao.getTaskById(id)

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
       dao.deleteTask(task)
    }
}