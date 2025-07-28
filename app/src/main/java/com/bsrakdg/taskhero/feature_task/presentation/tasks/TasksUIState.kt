package com.bsrakdg.taskhero.feature_task.presentation.tasks

import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus.All

data class TasksUIState(
    val tasks: List<Task> = listOf(),
    val status: ShowingListStatus = All,
    val isStatusBarVisible: Boolean = false,
    val darkMode: Boolean = false
)
