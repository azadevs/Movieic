# Movieic

## Overview

The Movieic is an Android application that provides detailed information about movies. Using modern Android development practices, this app is built with MVVM architecture, Coroutines for asynchronous operations, Paging3 for efficient data handling, Room for local storage, Retrofit for network requests, and Dagger Hilt for dependency injection.

## Features

- Movie Information: View detailed information about movies, including synopsis, release date, and genre.
- Cast Details: See the cast and crew involved in each movie.
- Trailers: Watch trailers for movies directly within the app.
- Bookmarked Movies: Save and manage a list of favorite movies for quick access.
- Offline Storage: Cache movie information for offline access, allowing users to view movie details without an active internet connection.

## Screenshots
<img src="https://imgur.com/G0IDvw8.png" width="250"> <img src="https://imgur.com/n339gks.png" width="250">
<img src="https://imgur.com/XifTPog.png" width="250"> <img src="https://imgur.com/UpmAovD.png" width="250">
<img src="https://imgur.com/ZE1MR1Q.png" width="250"> <img src="https://imgur.com/FgYOdGu.png" width="250">

## Technologies Used

- MVVM (Model-View-ViewModel): Architecture pattern for separating concerns and managing UI-related data.
- Coroutines: For managing asynchronous tasks and background operations.
- Paging3: For efficient and smooth paging of large datasets.
- Room: For local database management and caching.
- Retrofit: For making network requests and handling API interactions.
- Glide: For efficient image loading and caching.
- Dagger Hilt: For dependency injection and managing app components.

## Installation

### Prerequisites
Android Studio: Ensure you have Android Studio installed.
Android SDK: Make sure you have the required Android SDKs.

### Clone the Repository
```bash
git clone https://github.com/azadevs/Movieic.git
```

### Build and Run
1. Open Android Studio.
2. Select File > Open and choose the cloned repository.
3. Sync the project with Gradle files.
4. Click Run to build and launch the app on an emulator or connected device.

### Configuration
To configure the app, follow these steps:

1. API Key: Obtain an API key from [The Movie Database](https://www.themoviedb.org) and add it to your local gradle.properties file:
properties
```bash
API_KEY=your_api_key_here
```
2. Build Configuration: Ensure build.gradle files are set up with the necessary dependencies.

## Acknowledgements
- Thanks to [The Movie Database](https://www.themoviedb.org) for providing movie data.
- Resources and inspiration from [Android Developers](https://developer.android.com).




