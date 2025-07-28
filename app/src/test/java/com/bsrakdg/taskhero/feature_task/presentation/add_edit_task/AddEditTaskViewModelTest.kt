package com.bsrakdg.taskhero.feature_task.presentation.add_edit_task

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.SavedStateHandle
import com.bsrakdg.taskhero.MainCoroutineScopeRule
import com.bsrakdg.taskhero.feature_task.domain.model.Task
import com.bsrakdg.taskhero.feature_task.domain.use_case.GetTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.InsertTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.TasksUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineScopeRule::class)
class AddEditTaskViewModelTest {
    private lateinit var tasksUseCases: TasksUseCases
    private lateinit var getTaskUseCase: GetTaskUseCase
    private lateinit var insertTaskUseCase: InsertTaskUseCase

    private lateinit var viewModel: AddEditTaskViewModel

    private val savedStateHandle = SavedStateHandle()

    companion object {
        @JvmStatic
        fun focusTestCases() = listOf(
            FocusTestCase(
                eventBuilder = { focusState -> AddEditTaskEvent.ChangeContentFocus(focusState) },
                getState = { vm: AddEditTaskViewModel -> vm.taskContent.value },
                enteredEvent = { text: String -> AddEditTaskEvent.EnteredContent(text) }
            ),
            FocusTestCase(
                eventBuilder = { focusState -> AddEditTaskEvent.ChangeTitleFocus(focusState) },
                getState = { vm: AddEditTaskViewModel -> vm.taskTitle.value },
                enteredEvent = { text: String -> AddEditTaskEvent.EnteredTitle(text) }
            )
        )
    }

    data class FocusTestCase(
        val eventBuilder: (FocusState) -> AddEditTaskEvent,
        val getState: (AddEditTaskViewModel) -> TaskTextFieldState,
        val enteredEvent: (String) -> AddEditTaskEvent
    )

    @BeforeEach
    fun setup() {
        getTaskUseCase = mockk()
        insertTaskUseCase = mockk(relaxed = true)

        tasksUseCases = TasksUseCases(
            getAllTasksUseCase = mockk(),
            getTaskUseCase = getTaskUseCase,
            insertTaskUseCase = insertTaskUseCase,
            deleteTaskUseCase = mockk(),
            filterTasksUseCase = mockk()
        )

        viewModel = AddEditTaskViewModel(tasksUseCases, savedStateHandle)
    }

    @Test
    fun `initial title and content state is empty and hint is visible`() = runTest {
        val titleState = viewModel.taskTitle.value
        val contentState = viewModel.taskContent.value

        assertThat(titleState.text).isEmpty()
        assertThat(titleState.hint).isEqualTo("Enter Title")
        assertThat(titleState.isHintVisible).isTrue()

        assertThat(contentState.text).isEmpty()
        assertThat(contentState.hint).isEqualTo("Enter Content")
        assertThat(contentState.isHintVisible).isTrue()
    }

    @Test
    fun `entered title updates title state`() = runTest {
        val newTitle = "My Task Title"
        viewModel.onEvent(AddEditTaskEvent.EnteredTitle(newTitle))

        assertThat(viewModel.taskTitle.value.text).isEqualTo(newTitle)
    }

    @Test
    fun `entered content updates content state`() = runTest {
        val newContent = "My Task Content"
        viewModel.onEvent(AddEditTaskEvent.EnteredContent(newContent))

        assertThat(viewModel.taskContent.value.text).isEqualTo(newContent)
    }


    @Test
    fun `save task invokes insertTaskUseCase`() = runTest {
        val title = "Test Title"
        val content = "Test Content"

        viewModel.onEvent(AddEditTaskEvent.EnteredTitle(title))
        viewModel.onEvent(AddEditTaskEvent.EnteredContent(content))

        coEvery { insertTaskUseCase(any()) } just Runs

        viewModel.onEvent(AddEditTaskEvent.SaveTask)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            insertTaskUseCase(
                withArg {
                    assertThat(it.title).isEqualTo(title)
                    assertThat(it.content).isEqualTo(content)
                    assertThat(it.completed).isFalse()
                }
            )
        }
    }

    @Test
    fun `init loads task and updates states`() = runTest {
        val taskId = 1
        val task = Task(
            id = taskId,
            title = "Test Task",
            content = "Test Content",
            completed = false,
            timeStamp = 0L
        )

        // Prepare SavedStateHandle with taskId
        val savedStateHandle = SavedStateHandle(mapOf("taskId" to taskId))

        // Mock getTaskUseCase to return the task
        coEvery { getTaskUseCase(taskId) } returns task

        // Create ViewModel with mocked SavedStateHandle
        val viewModel = AddEditTaskViewModel(
            tasksUseCases = TasksUseCases(
                getAllTasksUseCase = mockk(),
                getTaskUseCase = getTaskUseCase,
                insertTaskUseCase = insertTaskUseCase,
                deleteTaskUseCase = mockk(),
                filterTasksUseCase = mockk()
            ),
            savedStateHandle = savedStateHandle
        )

        // Advance coroutine until all tasks are completed
        advanceUntilIdle()

        // Check that taskTitle and taskContent updated with loaded task data
        assertThat(viewModel.taskTitle.value.text).isEqualTo(task.title)
        assertThat(viewModel.taskTitle.value.isHintVisible).isFalse()

        assertThat(viewModel.taskContent.value.text).isEqualTo(task.content)
        assertThat(viewModel.taskContent.value.isHintVisible).isFalse()
    }

    @ParameterizedTest
    @MethodSource("focusTestCases")
    fun `focus events update hint visibility correctly`(testCase: FocusTestCase) = runTest {
        val focusState = mockk<FocusState>()

        // Case 1: Focused true, text empty
        every { focusState.isFocused } returns true
        viewModel.onEvent(testCase.eventBuilder(focusState))
        assertThat(testCase.getState(viewModel).isHintVisible).isFalse()

        // Case 2: Focused false, text empty
        every { focusState.isFocused } returns false
        viewModel.onEvent(testCase.eventBuilder(focusState))
        assertThat(testCase.getState(viewModel).isHintVisible).isTrue()

        // Case 3: Focused false, text not empty
        viewModel.onEvent(testCase.enteredEvent("Not Empty"))
        viewModel.onEvent(testCase.eventBuilder(focusState))
        assertThat(testCase.getState(viewModel).isHintVisible).isFalse()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
}

