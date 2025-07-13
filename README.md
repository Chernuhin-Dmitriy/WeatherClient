# Weather Client Android App
An application for viewing the weather forecast with the ability to search for cities and save your favorite locations.

<div align="center">
<img width="333" height="886" alt="image" src="https://github.com/user-attachments/assets/5e077040-14df-4c1e-85a0-334a95023084" />
<img width="329" height="889" alt="image" src="https://github.com/user-attachments/assets/b0523e7f-cce0-401b-bacd-82a111577452" />

</div>

## Architecture
### The application is built on a Clean Architecture with a division into layers:
- Presentation Layer - UI components (Jetpack Compose)
- Domain Layer - business logic, use cases, repositories (interfaces)
- Data Layer - data sources (API, database), repository implementations
### Patterns:
- MVVM - for managing the UI state
- Repository Pattern - for abstraction of data sources
- Use Cases - for encapsulating business logic
- Dependency Injection - for dependency management
###Libraries
UI and Navigation
- Jetpack Compose is a modern declarative UI framework
- Compose Navigation - navigation between screens
- Material 3 - Google design system
### Architecture and DI
- Hilt - dependency injection from Google
- ViewModel - managing the UI state based on the lifecycle
- LiveData/StateFlow - reactive programming
### Network
- Coil - uploading images (weather icons)
- Retrofit - HTTP client for REST API
- OkHttp is a low-level HTTP client
- Gson - JSON serialization/deserialization
### The database
- Room - ORM for SQLite
- SQLite - Local database
### Asynchrony
- Kotlin Coroutines - asynchronous programming
- Flow - reactive data flows


<div align="center">
  
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)

<!-- Архитектура -->
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-FF9800?style=for-the-badge&logo=android&logoColor=white)]()
[![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-4CAF50?style=for-the-badge&logo=android&logoColor=white)]()

<!-- База данных и сеть -->
[![Room](https://img.shields.io/badge/Room-Database-4CAF50?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/androidx/releases/room)
[![Retrofit](https://img.shields.io/badge/Retrofit-Network-FF5722?style=for-the-badge&logo=square&logoColor=white)](https://square.github.io/retrofit)
[![Coil](https://img.shields.io/badge/Coil-Images-9C27B0?style=for-the-badge&logo=android&logoColor=white)](https://coil-kt.github.io/coil)
</div>
