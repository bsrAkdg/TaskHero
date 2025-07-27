package com.bsrakdg.taskhero.feature_task.presentation.tasks

import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus

sealed class TasksEvent {
    data class DeleteTask(val task: Task) : TasksEvent()
    object ToggleStatusSection : TasksEvent()
    data class ChangeShowingStatus(val status: ShowingListStatus) : TasksEvent()
}