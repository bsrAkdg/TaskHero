package com.bsrakdg.taskhero.feature_task.presentation.util

sealed class ShowingListStatus {
    object Completed : ShowingListStatus()
    object OnGoing : ShowingListStatus()
    object All : ShowingListStatus()

    fun toIndex(): Int = when (this) {
        Completed -> 0
        OnGoing -> 1
        All -> 2
    }

    companion object {
        fun fromIndex(index: Int): ShowingListStatus = when (index) {
            0 -> Completed
            1 -> OnGoing
            2 -> All
            else -> All
        }
    }
}