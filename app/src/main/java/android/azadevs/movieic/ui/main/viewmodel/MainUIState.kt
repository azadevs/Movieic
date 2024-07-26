package android.azadevs.movieic.ui.main.viewmodel

import android.azadevs.movieic.data.local.db.entity.MovieEntity

sealed interface MainUIState {

    data object Loading : MainUIState

    data class Success(val data: List<MovieEntity>) : MainUIState

    sealed interface Error : MainUIState {
        data object NetworkError : Error
        data object HttpError : Error
        data class UnknownError(val error: String) : Error
    }

}