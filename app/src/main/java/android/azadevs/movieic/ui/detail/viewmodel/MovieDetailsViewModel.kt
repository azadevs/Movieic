package android.azadevs.movieic.ui.detail.viewmodel

import android.azadevs.movieic.data.local.db.entity.CastEntity
import android.azadevs.movieic.data.local.db.entity.MovieDetailsEntity
import android.azadevs.movieic.data.repository.MovieDetailsRepository
import android.azadevs.movieic.utils.Constants
import android.azadevs.movieic.utils.Resource
import android.azadevs.movieic.utils.UiText
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * 01/07/24
 */
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieDetailsRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _movieDetailsState =
        MutableStateFlow<MovieDetailsUiState<MovieDetailsEntity>>(MovieDetailsUiState.Loading)
    val movieDetailsState get() = _movieDetailsState.asStateFlow()


    private var _movieCastState =
        MutableStateFlow<MovieDetailsUiState<List<CastEntity>>>(MovieDetailsUiState.Loading)
    val movieCastState get() = _movieCastState.asStateFlow()

    private var _navigationEvent = Channel<Int>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val movieId = savedStateHandle.get<Int>(Constants.MOVIE_ID_ARG)

    init {
        movieId?.let { movieId ->
            getMovieDetails(movieId)
            getMovieCast(movieId)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _movieDetailsState.value = MovieDetailsUiState.Loading
            _movieCastState.value = MovieDetailsUiState.Loading
            movieId?.let { movieId ->
                getMovieDetails(movieId)
                getMovieCast(movieId)
            }
        }
    }

    private fun getMovieCast(movieId: Int) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) { repository.getMovieCastById(movieId) }
            _movieCastState.value = when (response) {
                is Resource.Error -> {
                    when (response) {
                        Resource.Error.HttpError -> MovieDetailsUiState.Error.HttpError
                        Resource.Error.NetworkError -> MovieDetailsUiState.Error.NetworkError
                        is Resource.Error.UnknownError -> MovieDetailsUiState.Error.UnknownError(
                            UiText.DynamicString(response.error)
                        )
                    }
                }

                is Resource.Success -> MovieDetailsUiState.Success(response.data.cast)
                Resource.Loading -> MovieDetailsUiState.Loading
            }
        }
    }

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) { repository.getMovieDetailsById(movieId) }
            when (response) {
                is Resource.Error -> {
                    when (response) {
                        Resource.Error.HttpError -> MovieDetailsUiState.Error.HttpError
                        Resource.Error.NetworkError -> MovieDetailsUiState.Error.NetworkError
                        is Resource.Error.UnknownError -> MovieDetailsUiState.Error.UnknownError(
                            UiText.DynamicString(response.error)
                        )
                    }
                }

                Resource.Loading -> _movieDetailsState.value = MovieDetailsUiState.Loading
                is Resource.Success -> _movieDetailsState.value =
                    MovieDetailsUiState.Success(response.data)
            }
        }
    }


    fun upsertBookmark() {
        viewModelScope.launch {
            val data = (_movieDetailsState.value as MovieDetailsUiState.Success).data
            repository.upsertMovieDetails(
                data.copy(isBookmarked = !data.isBookmarked)
            )
        }
    }


    fun navigateToTrailer() {
        viewModelScope.launch {
            try {
                _navigationEvent.send(movieId!!)
            } catch (_: Exception) {

            }
        }
    }

}

