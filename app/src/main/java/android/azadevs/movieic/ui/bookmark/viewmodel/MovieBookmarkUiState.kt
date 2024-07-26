package android.azadevs.movieic.ui.bookmark.viewmodel

import android.azadevs.movieic.data.local.db.entity.MovieEntity

sealed interface MovieBookmarkUiState {
    data class Success(val movieEntityData: List<MovieEntity>) : MovieBookmarkUiState
    data class Error(val error: String) : MovieBookmarkUiState
    data object Empty : MovieBookmarkUiState
    data object Loading : MovieBookmarkUiState
}