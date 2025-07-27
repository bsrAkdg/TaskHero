package com.bsrakdg.taskhero.feature_task.domain.use_case

data class TasksUseCases(
    val getAllTasksUseCase: GetAllTasksUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val insertTaskUseCase: InsertTaskUseCase,
    val filterTasksUseCase: FilterTasksUseCase
)
