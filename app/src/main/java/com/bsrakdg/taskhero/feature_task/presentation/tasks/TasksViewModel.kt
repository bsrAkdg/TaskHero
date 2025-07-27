package com.bsrakdg.taskhero.feature_task.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsrakdg.taskhero.feature_task.domain.use_case.TasksUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksUseCases: TasksUseCases
) : ViewModel() {
    private val _tasksUIState = MutableStateFlow(TasksUIState())
    val state: StateFlow<TasksUIState> = _tasksUIState

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.DeleteTask -> {
                viewModelScope.launch {
                    tasksUseCases.deleteTaskUseCase(
                        event.task
                    )
                }
            }

            is TasksEvent.ToggleStatusSection -> {
                _tasksUIState.update { currentState ->
                    currentState.copy(
                        isStatusBarVisible = !currentState.isStatusBarVisible
                    )
                }
            }

            is TasksEvent.ChangeShowingStatus -> {

            }
        }
    }
}