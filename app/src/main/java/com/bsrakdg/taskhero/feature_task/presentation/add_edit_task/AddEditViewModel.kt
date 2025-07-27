package com.bsrakdg.taskhero.feature_task.presentation.add_edit_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsrakdg.taskhero.feature_task.domain.model.InvalidTaskException
import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.use_case.TasksUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val tasksUseCases: TasksUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _taskTitle = MutableStateFlow(
        TaskTextFieldState(
            hint = "Enter Title"
        )
    )
    val taskTitle: StateFlow<TaskTextFieldState> = _taskTitle

    private val _taskContent = MutableStateFlow(
        TaskTextFieldState(
            hint = "Enter Content"
        )
    )
    val taskContent: StateFlow<TaskTextFieldState> = _taskContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentTaskId: Int? = null

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            if (taskId != -1) {
                viewModelScope.launch {
                    tasksUseCases.getTaskUseCase(taskId)?.also { task ->
                        currentTaskId = taskId
                        _taskTitle.update {
                            it.copy(
                                text = task.title,
                                isHintVisible = false
                            )
                        }
                        _taskContent.update {
                            it.copy(
                                text = task.content,
                                isHintVisible = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditTaskEvent) {
        when (event) {
            is AddEditTaskEvent.ChangeContentFocus -> {
                _taskContent.update {
                    it.copy(
                        isHintVisible = !event.focusState.isFocused
                                && taskContent.value.text.isBlank()
                    )
                }
            }

            is AddEditTaskEvent.ChangeTitleFocus -> {
                _taskTitle.update {
                    it.copy(
                        isHintVisible = !event.focusState.isFocused
                                && taskTitle.value.text.isBlank()
                    )
                }
            }

            is AddEditTaskEvent.EnteredContent -> {
                _taskContent.update {
                    it.copy(event.title)
                }
            }

            is AddEditTaskEvent.EnteredTitle -> {
                _taskTitle.update {
                    it.copy(event.title)
                }
            }

            is AddEditTaskEvent.SaveTask -> {
                viewModelScope.launch {
                    try {
                        tasksUseCases.insertTaskUseCase(
                            Task(
                                title = taskTitle.value.text,
                                content = taskContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                completed = false,
                                id = currentTaskId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidTaskException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Could not save task"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}