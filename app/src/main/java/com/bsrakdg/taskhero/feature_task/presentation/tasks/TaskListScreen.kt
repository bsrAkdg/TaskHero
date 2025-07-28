package com.bsrakdg.taskhero.feature_task.presentation.tasks

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.presentation.tasks.components.DefaultRadioButton
import com.bsrakdg.taskhero.feature_task.presentation.tasks.components.StatusBarContent
import com.bsrakdg.taskhero.feature_task.presentation.tasks.components.TaskItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskListScreen(
    uiState: TasksUIState,
    navigate: (taskId: Int) -> Unit,
    setDarkMode: (isChecked: Boolean) -> Unit,
    darkModeState: Boolean,
    onEvent: (TasksEvent) -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val taskItemColor = MaterialTheme.colorScheme.surfaceVariant
    val titleColor = MaterialTheme.colorScheme.onSurface

    LaunchedEffect(true) {
        onEvent.invoke(TasksEvent.UpdateTheme(darkModeState))
    }

    Scaffold(
        modifier = Modifier.background(backgroundColor),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigate.invoke(-1)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Task",
                        style = MaterialTheme.typography.headlineSmall,
                        color = titleColor
                    )
                    IconButton(
                        onClick = {
                            onEvent.invoke(TasksEvent.ToggleStatusSection)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Sort"
                        )
                    }
                }

                AnimatedVisibility(
                    visible = uiState.isStatusBarVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column {
                        StatusBarContent(
                            showingListStatus = uiState.status,
                            onShowingListStatusChange = { status ->
                                onEvent.invoke(TasksEvent.ChangeShowingStatus(status = status))
                            }
                        )
                        DefaultRadioButton(
                            headerText = "Dark Mode",
                            isSelected = uiState.darkMode,
                            onSelected = {
                                setDarkMode.invoke(!uiState.darkMode)
                                onEvent.invoke(TasksEvent.UpdateTheme(!uiState.darkMode))
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(
                        uiState.tasks,
                        key = { _, task -> task.id ?: task.hashCode() }
                    ) { index, task ->
                        TaskItem(
                            task = task,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .background(
                                    color = taskItemColor,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            onDeleteClick = {
                                onEvent.invoke(TasksEvent.DeleteTask(task))
                            },
                            onItemClick = { task ->
                                task.id?.let {
                                    navigate.invoke(it)
                                }
                            },
                            onCheckBoxClick = { task, isCompleted ->
                                onEvent.invoke(TasksEvent.UpdateTaskStatus(task, isCompleted))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun TaskListScreenPreview() {
    TaskListScreen(
        uiState = TasksUIState(
            tasks = List(10) { index ->
                Task(
                    title = "title",
                    content = "content",
                    timeStamp = 1L,
                    completed = false,
                    id = index
                )
            }
        ),
        navigate = {},
        setDarkMode = {},
        darkModeState = false,
        onEvent = {}
    )
}

