# TaskHero
TaskHero is a modern Android task management application that allows users to add, edit, and delete tasks efficiently. It leverages Kotlin, Jetpack Compose, Room, and Clean Architecture with the MVVM pattern to provide a clean, maintainable, and scalable codebase.

Features

Add new tasks with title and content
Edit existing tasks
Delete tasks
Filter tasks by status (Completed, Ongoing, All)
Real-time UI updates using Kotlin Flows and Jetpack Compose
Persistent storage with Room database
Dark and Light theme support
Clean Architecture and MVVM for a modular, testable codebase

Architecture & Technologies

Language: Kotlin
UI: Jetpack Compose
Data Persistence: Room
Architecture: Clean Architecture with MVVM pattern
Asynchronous: Kotlin Coroutines and Flows
Dependency Injection: Hilt
Testing:
Unit Testing: JUnit 5, MockK, Google Truth
Coroutine testing with kotlinx-coroutines-test

Build and Run

Open the project in Android Studio
Sync Gradle
Run on an emulator or physical device

Project Structure

data: Repository implementations, Room database entities, and DAOs
domain: Use cases and business logic interfaces
presentation: ViewModels and Compose UI components
di: Dependency injection modules
utils: Utility classes and helpers
test: Unit tests for ViewModels and use cases

Testing

The project uses JUnit 5, MockK, and Google Truth libraries for unit testing.