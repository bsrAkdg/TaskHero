package com.bsrakdg.taskhero.feature_task.presentation.tasks.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bsrakdg.taskhero.feature_task.presentation.util.ShowingListStatus

@Composable
fun StatusBarSection(
    modifier: Modifier = Modifier,
    showingListStatus: ShowingListStatus = ShowingListStatus.All(),
    onShowingListStatusChange: (ShowingListStatus) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(
            headerText = "ALL",
            isSelected = showingListStatus is ShowingListStatus.All,
            onSelected = {
                onShowingListStatusChange.invoke(ShowingListStatus.All())
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        DefaultRadioButton(
            headerText = "ONGOING",
            isSelected = showingListStatus is ShowingListStatus.OnGoing,
            onSelected = {
                onShowingListStatusChange.invoke(ShowingListStatus.OnGoing())
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        DefaultRadioButton(
            headerText = "Title",
            isSelected = showingListStatus is ShowingListStatus.Completed,
            onSelected = {
                onShowingListStatusChange.invoke(ShowingListStatus.Completed())
            }
        )
    }
}

@Composable
fun DefaultRadioButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    headerText: String,
    onSelected: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Unspecified,
                unselectedColor = Color.Blue
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = headerText, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
@Preview
private fun RadioButtonPreview() {
    DefaultRadioButton(
        isSelected = false,
        headerText = "headerText"
    ) {}
}