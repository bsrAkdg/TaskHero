package com.bsrakdg.taskhero.feature_task.domain.use_case

import com.bsrakdg.taskhero.feature_task.domain.model.InvalidTaskException
import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository
import kotlin.jvm.Throws

class InsertTaskUseCase(
    private val repository: TaskRepository
) {
    @Throws(InvalidTaskException::class)
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            throw InvalidTaskException("title can not be empty")
        }
        if (task.content.isBlank()) {
            throw InvalidTaskException("content can not be empty")
        }
        repository.insertTask(task)
    }
}