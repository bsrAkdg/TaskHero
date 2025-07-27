package com.bsrakdg.taskhero.feature_task.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val content: String,
    val timeStamp: Long,
    val completed: Boolean,
    @PrimaryKey val id: Int? = null
)
