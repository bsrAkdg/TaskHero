package com.bsrakdg.taskhero.feature_task.domain.use_case

import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class FilterTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(isCompleted: Boolean): Flow<List<Task>> =
        repository.filterTasks(isCompleted = isCompleted)
}