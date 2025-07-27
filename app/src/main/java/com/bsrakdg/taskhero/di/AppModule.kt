package com.bsrakdg.taskhero.di

import android.app.Application
import androidx.room.Room
import com.bsrakdg.taskhero.feature_task.data.data_source.TaskDatabase
import com.bsrakdg.taskhero.feature_task.data.repository.TaskRepositoryImpl
import com.bsrakdg.taskhero.feature_task.domain.repository.TaskRepository
import com.bsrakdg.taskhero.feature_task.domain.use_case.DeleteTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.FilterTasksUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.GetAllTasksUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.GetTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.InsertTaskUseCase
import com.bsrakdg.taskhero.feature_task.domain.use_case.TasksUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(database: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(dao = database.taskDao)
    }

    @Provides
    @Singleton
    fun provideTaskUseCases(repository: TaskRepository): TasksUseCases {
        return TasksUseCases(
            getAllTasksUseCase = GetAllTasksUseCase(repository),
            deleteTaskUseCase = DeleteTaskUseCase(repository),
            insertTaskUseCase = InsertTaskUseCase(repository),
            filterTasksUseCase = FilterTasksUseCase(repository),
            getTaskUseCase = GetTaskUseCase(repository)
        )
    }
}