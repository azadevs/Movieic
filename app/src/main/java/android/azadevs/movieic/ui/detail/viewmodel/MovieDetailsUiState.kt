package android.azadevs.movieic.ui.detail.viewmodel

import android.azadevs.movieic.utils.UiText

sealed class MovieDetailsUiState<out T> {

    data class Success<T>(val data: T) : MovieDetailsUiState<T>()

    sealed class Error : MovieDetailsUiState<Nothing>() {
        data object NetworkError : Error()

        data object HttpError : Error()

        data class UnknownError(val error: UiText) : Error()
    }

    data object Loading : MovieDetailsUiState<Nothing>()
}