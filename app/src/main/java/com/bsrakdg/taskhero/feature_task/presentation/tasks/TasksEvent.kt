package com.bsrakdg.taskhero.feature_task.presentation.tasks

import com.bsrakdg.taskhero.feature_task.domain.model.Task

sealed class TasksEvent {
    data class DeleteTask(val task: Task) : TasksEvent()
    object ToggleStatusSection : TasksEvent()

    object ChangeShowingStatus : TasksEvent()
}