package com.bsrakdg.taskhero.feature_task.domain.use_case

import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>>  = repository.getAllTasks()
}