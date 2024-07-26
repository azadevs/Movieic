package android.azadevs.movieic.ui.trailer.viewmodel

import android.azadevs.movieic.data.remote.model.Video

/**
 * Created by : Azamat Kalmurzayev
 * 17/07/24
 */
sealed interface MovieTrailersUiState {

    data object Loading : MovieTrailersUiState

    data class Success(val data: List<Video>) : MovieTrailersUiState

    sealed interface Error : MovieTrailersUiState {
        data class UnknownError(val error: String) : Error
        data object NetworkError : Error
        data object HttpError : Error
    }

}