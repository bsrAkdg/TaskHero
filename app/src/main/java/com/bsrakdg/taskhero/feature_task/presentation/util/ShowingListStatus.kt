package com.bsrakdg.taskhero.feature_task.presentation.util

sealed class ShowingListStatus {
    class Completed : ShowingListStatus()
    class OnGoing : ShowingListStatus()
    class All : ShowingListStatus()
}