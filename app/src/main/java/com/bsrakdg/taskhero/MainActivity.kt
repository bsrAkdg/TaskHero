package com.bsrakdg.taskhero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bsrakdg.taskhero.feature_task.presentation.add_edit_task.AddEditTaskScreen
import com.bsrakdg.taskhero.feature_task.presentation.add_edit_task.AddEditTaskViewModel
import com.bsrakdg.taskhero.feature_task.presentation.tasks.TaskListScreen
import com.bsrakdg.taskhero.feature_task.presentation.tasks.TasksViewModel
import com.bsrakdg.taskhero.feature_task.presentation.util.Screen
import com.bsrakdg.taskhero.ui.theme.TaskHeroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskHeroTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TaskListScreen.route
                    ) {
                        composable(route = Screen.TaskListScreen.route) {
                            val viewModel = hiltViewModel<TasksViewModel>()
                            val uiState by viewModel.taskUIState.collectAsStateWithLifecycle()
                            TaskListScreen(
                                uiState = uiState,
                                onEvent = viewModel::onEvent,
                                navigate = { taskId ->
                                    navController.navigate(
                                        Screen.EditTaskScreen.route + "?taskId=${taskId}"
                                    )
                                }
                            )
                        }

                        composable(
                            route = Screen.EditTaskScreen.route + "?taskId={taskId}",
                            arguments = listOf(
                                navArgument(
                                    name = "taskId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val viewModel = hiltViewModel<AddEditTaskViewModel>()
                            val taskTitle = viewModel.taskTitle.collectAsStateWithLifecycle()
                            val taskContent = viewModel.taskContent.collectAsStateWithLifecycle()
                            AddEditTaskScreen(
                                taskTitle = taskTitle.value,
                                taskContent = taskContent.value,
                                navigate = {
                                    navController.navigateUp()
                                },
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}