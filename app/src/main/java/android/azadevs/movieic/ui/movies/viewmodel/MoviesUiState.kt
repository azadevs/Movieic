package android.azadevs.movieic.ui.movies.viewmodel

import android.azadevs.movieic.data.local.db.entity.MovieEntity
import androidx.paging.PagingData

/**
 * Created by : Azamat Kalmurzayev
 * 25/07/24
 */
sealed interface MoviesUiState {

    data object Loading : MoviesUiState

    sealed interface Error : MoviesUiState {

        data object HttpError : Error

        data object NetworkError : Error

        data class UnknownError(val error: String) : Error

    }

    data class Success(val data: PagingData<MovieEntity>) : MoviesUiState
}