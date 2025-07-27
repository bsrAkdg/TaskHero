package com.bsrakdg.taskhero.feature_task.presentation.util

sealed class Screen(val route: String) {
    object TaskListScreen : Screen("task_list")
    object EditTaskScreen : Screen("edit_task")
}