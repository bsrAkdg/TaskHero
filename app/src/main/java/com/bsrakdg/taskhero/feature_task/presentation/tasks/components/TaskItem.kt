package com.bsrakdg.taskhero.feature_task.presentation.tasks.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bsrakdg.taskhero.feature_task.domain.model.Task

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onDeleteClick: (task: Task) -> Unit,
    onCheckBoxClick: (task: Task) -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth().aspectRatio(1.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = {
                onDeleteClick.invoke(task)
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete task",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Row (modifier = Modifier.align(Alignment.BottomStart)){
            Checkbox(
                checked = task.completed,
                onCheckedChange = {
                    onCheckBoxClick.invoke(task)
                }
            )
        }
    }
}

@Composable
@Preview
fun TaskItemPreview() {
    TaskItem(
        task = Task(
            title = "title",
            content = "content",
            timeStamp = 1L,
            completed = false,
            id = 1
        ),
        onDeleteClick = {}
    ) {}
}