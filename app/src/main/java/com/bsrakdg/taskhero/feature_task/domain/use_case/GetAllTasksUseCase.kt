package com.bsrakdg.taskhero.feature_task.domain.use_case

import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.getAllTasks()
}