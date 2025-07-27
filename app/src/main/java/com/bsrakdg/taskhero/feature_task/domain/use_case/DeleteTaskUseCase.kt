package com.bsrakdg.taskhero.feature_task.domain.use_case

import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}