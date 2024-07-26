package android.azadevs.movieic.ui.trailer.viewmodel

import android.azadevs.movieic.data.repository.MovieDetailsRepository
import android.azadevs.movieic.utils.Constants.MOVIE_ID_ARG
import android.azadevs.movieic.utils.Resource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * 17/07/24
 */
@HiltViewModel
class MovieTrailersViewModel @Inject constructor(
    private val repository: MovieDetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _movieVideosState =
        MutableStateFlow<MovieTrailersUiState>(MovieTrailersUiState.Loading)

    val movieVideosState get() = _movieVideosState.asStateFlow()

    init {
        refresh()
    }

    private fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            val response = repository.getMovieVideos(movieId)
            _movieVideosState.value = when (response) {
                is Resource.Error -> {
                    when (response) {
                        Resource.Error.HttpError -> MovieTrailersUiState.Error.HttpError
                        Resource.Error.NetworkError -> MovieTrailersUiState.Error.NetworkError
                        is Resource.Error.UnknownError -> MovieTrailersUiState.Error.UnknownError(
                            response.error
                        )
                    }
                }

                is Resource.Success -> MovieTrailersUiState.Success(response.data.videos)
                Resource.Loading -> {
                    MovieTrailersUiState.Loading
                }
            }
        }
    }

    fun refresh() {
        savedStateHandle.get<Int>(MOVIE_ID_ARG)?.let {
            getMovieVideos(it)
        }
    }
}

