package com.bsrakdg.taskhero.feature_task.presentation.add_edit_task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bsrakdg.taskhero.feature_task.presentation.add_edit_task.components.TransparentHintTextField

@Composable
fun AddEditTaskScreen(
    taskTitle: TaskTextFieldState,
    taskContent: TaskTextFieldState,
    onEvent: (AddEditTaskEvent) -> Unit,
    navigate: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onSurface

    Scaffold(
        containerColor = backgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddEditTaskEvent.SaveTask)
                    navigate()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Save task"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "New Task",
                style = MaterialTheme.typography.headlineMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title Field
            TransparentHintTextField(
                text = taskTitle.text,
                hint = taskTitle.hint,
                onValueChange = { onEvent(AddEditTaskEvent.EnteredTitle(it)) },
                onFocusChange = { onEvent(AddEditTaskEvent.ChangeTitleFocus(it)) },
                isHintVisible = taskTitle.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content Field
            TransparentHintTextField(
                text = taskContent.text,
                hint = taskContent.hint,
                onValueChange = { onEvent(AddEditTaskEvent.EnteredContent(it)) },
                onFocusChange = { onEvent(AddEditTaskEvent.ChangeContentFocus(it)) },
                isHintVisible = taskContent.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            )
        }
    }
}
