package com.bsrakdg.taskhero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bsrakdg.taskhero.feature_task.presentation.add_edit_task.AddEditTaskScreen
import com.bsrakdg.taskhero.feature_task.presentation.add_edit_task.AddEditTaskViewModel
import com.bsrakdg.taskhero.feature_task.presentation.tasks.TaskListScreen
import com.bsrakdg.taskhero.feature_task.presentation.tasks.TasksEvent
import com.bsrakdg.taskhero.feature_task.presentation.tasks.TasksViewModel
import com.bsrakdg.taskhero.feature_task.presentation.util.Screen
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus
import com.bsrakdg.taskhero.feature_task.presentation.util.ThemePreferenceManager
import com.bsrakdg.taskhero.ui.theme.TaskHeroTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val darkModeState by ThemePreferenceManager
                .getDarkModeFlow(applicationContext)
                .collectAsState(initial = false)

            val currentFilter by ThemePreferenceManager
                .getCurrentFilter(applicationContext)
                .collectAsState(initial = 0)

            TaskHeroTheme(darkTheme = darkModeState) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.TaskListScreen.route
                    ) {
                        composable(route = Screen.TaskListScreen.route) {
                            val viewModel = hiltViewModel<TasksViewModel>()
                            val uiState by viewModel.taskUIState.collectAsStateWithLifecycle()
                            viewModel.onEvent(TasksEvent.UpdateTheme(darkModeState))
                            viewModel.onEvent(TasksEvent.ChangeShowingStatus(ShowingListStatus.fromIndex(currentFilter)))
                            TaskListScreen(
                                uiState = uiState,
                                onEvent = viewModel::onEvent,
                                darkModeState = darkModeState,
                                setDarkMode = { isDark ->
                                    lifecycleScope.launch {
                                        ThemePreferenceManager.setDarkMode(
                                            context = applicationContext,
                                            isDarkMode = isDark
                                        )
                                    }
                                },
                                setCurrentFilter = { currentFilter ->
                                    lifecycleScope.launch {
                                        ThemePreferenceManager.setCurrentFilter(
                                            context = applicationContext,
                                            currentFilter = currentFilter
                                        )
                                    }
                                },
                                navigate = { taskId ->
                                    navController.navigate(
                                        Screen.EditTaskScreen.route + "?taskId=$taskId"
                                    )
                                }
                            )
                        }

                        composable(
                            route = Screen.EditTaskScreen.route + "?taskId={taskId}",
                            arguments = listOf(
                                navArgument("taskId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val viewModel = hiltViewModel<AddEditTaskViewModel>()
                            val taskTitle by viewModel.taskTitle.collectAsStateWithLifecycle()
                            val taskContent by viewModel.taskContent.collectAsStateWithLifecycle()

                            AddEditTaskScreen(
                                taskTitle = taskTitle,
                                taskContent = taskContent,
                                navigate = { navController.navigateUp() },
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}
