package com.bsrakdg.taskhero.feature_task.presentation.add_edit_task

import androidx.compose.ui.focus.FocusState

sealed class AddEditTaskEvent {
    data class EnteredTitle(val title: String) : AddEditTaskEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditTaskEvent()
    data class EnteredContent(val title: String) : AddEditTaskEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditTaskEvent()
    object SaveTask : AddEditTaskEvent()
}


