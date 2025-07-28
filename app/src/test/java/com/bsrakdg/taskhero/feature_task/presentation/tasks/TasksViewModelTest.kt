package com.bsrakdg.taskhero.feature_task.presentation.tasks

import com.bsrakdg.taskhero.MainCoroutineScopeRule
import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.use_case.DeleteTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.FilterTasksUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.GetAllTasksUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.InsertTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.TasksUseCases
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineScopeRule::class)
class TasksViewModelTest {

    private lateinit var tasksUseCases: TasksUseCases
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var filterTasksUseCase: FilterTasksUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var insertTaskUseCase: InsertTaskUseCase

    private lateinit var viewModel: TasksViewModel

    private val testTasks = listOf(
        Task(id = 1, title = "Task 1", content = "Content 1", completed = false, timeStamp = 0),
        Task(id = 2, title = "Task 2", content = "Content 2", completed = true, timeStamp = 0),
    )

    @BeforeEach
    fun setup() {
        getAllTasksUseCase = mockk()
        filterTasksUseCase = mockk()
        deleteTaskUseCase = mockk(relaxed = true)
        insertTaskUseCase = mockk(relaxed = true)

        tasksUseCases = TasksUseCases(
            getAllTasksUseCase = getAllTasksUseCase,
            getTaskUseCase = mockk(),
            insertTaskUseCase = insertTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            filterTasksUseCase = filterTasksUseCase
        )

        // Default stub for getAllTasksUseCase to emit testTasks
        every { getAllTasksUseCase() } returns flowOf(testTasks)

        viewModel = TasksViewModel(tasksUseCases)
    }

    @Test
    fun `init loads all tasks`() = runTest {
        val state = viewModel.taskUIState.first()
        assertThat(state.tasks).isEqualTo(testTasks)
    }

    @Test
    fun `ToggleStatusSection event toggles isStatusBarVisible`() = runTest {
        val initialVisibility = viewModel.taskUIState.value.isStatusBarVisible

        viewModel.onEvent(TasksEvent.ToggleStatusSection)
        val toggledOnce = viewModel.taskUIState.value.isStatusBarVisible
        assertThat(toggledOnce).isEqualTo(!initialVisibility)

        viewModel.onEvent(TasksEvent.ToggleStatusSection)
        val toggledTwice = viewModel.taskUIState.value.isStatusBarVisible
        assertThat(toggledTwice).isEqualTo(initialVisibility)
    }

    @Test
    fun `ChangeShowingStatus updates status and tasks`() = runTest {
        val filteredTasks = listOf(testTasks[1]) // Only completed task

        // Stub filterTasksUseCase to emit filteredTasks when filtering completed tasks
        every { filterTasksUseCase(true) } returns flowOf(filteredTasks)
        every { filterTasksUseCase(false) } returns flowOf(testTasks.filter { !it.completed })

        // Test for Completed
        viewModel.onEvent(TasksEvent.ChangeShowingStatus(ShowingListStatus.Completed))

        advanceUntilIdle() // Ensure flow collected

        assertThat(viewModel.taskUIState.value.status).isInstanceOf(ShowingListStatus.Completed::class.java)
        assertThat(viewModel.taskUIState.value.tasks).isEqualTo(filteredTasks)

        // Test for OnGoing
        viewModel.onEvent(TasksEvent.ChangeShowingStatus(ShowingListStatus.OnGoing))
        advanceUntilIdle()

        assertThat(viewModel.taskUIState.value.status).isInstanceOf(ShowingListStatus.OnGoing::class.java)
        assertThat(viewModel.taskUIState.value.tasks).isEqualTo(testTasks.filter { !it.completed })

        // Test for All
        viewModel.onEvent(TasksEvent.ChangeShowingStatus(ShowingListStatus.All))
        advanceUntilIdle()

        assertThat(viewModel.taskUIState.value.status).isInstanceOf(ShowingListStatus.All::class.java)
        assertThat(viewModel.taskUIState.value.tasks).isEqualTo(testTasks)
    }

    @Test
    fun `UpdateTheme updates darkMode in UI state`() = runTest {
        val initialDarkMode = viewModel.taskUIState.value.darkMode
        val newDarkMode = !initialDarkMode

        viewModel.onEvent(TasksEvent.UpdateTheme(newDarkMode))

        assertThat(viewModel.taskUIState.value.darkMode).isEqualTo(newDarkMode)
    }
}
