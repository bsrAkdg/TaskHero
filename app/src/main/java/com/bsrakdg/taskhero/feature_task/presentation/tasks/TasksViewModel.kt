package com.bsrakdg.taskhero.feature_task.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsrakdg.taskhero.feature_task.domain.use_case.TasksUseCases
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksUseCases: TasksUseCases
) : ViewModel() {
    private val _tasksUIState = MutableStateFlow(TasksUIState())
    val taskUIState: StateFlow<TasksUIState> = _tasksUIState

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        tasksUseCases.getAllTasksUseCase()
            .onEach { tasks ->
                _tasksUIState.update { currentState ->
                    currentState.copy(tasks = tasks)
                }
            }.launchIn(viewModelScope)
    }

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
                _tasksUIState.update { it.copy(status = event.status) }

                val taskFlow = when (event.status) {
                    is ShowingListStatus.All -> tasksUseCases.getAllTasksUseCase()
                    is ShowingListStatus.Completed -> tasksUseCases.filterTasksUseCase(true)
                    is ShowingListStatus.OnGoing -> tasksUseCases.filterTasksUseCase(false)
                }

                taskFlow
                    .onEach { tasks ->
                        _tasksUIState.update { it.copy(tasks = tasks) }
                    }
                    .launchIn(viewModelScope)
            }

            is TasksEvent.UpdateTaskStatus -> {
                viewModelScope.launch {
                    tasksUseCases.insertTaskUseCase.invoke(event.task)
                }
            }
        }
    }
}