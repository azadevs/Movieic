package android.azadevs.movieic.ui.movies.viewmodel

import android.azadevs.movieic.data.local.db.MovieDatabase
import android.azadevs.movieic.data.paging.NowPlayingMovieSource
import android.azadevs.movieic.data.paging.TopRatedMovieSource
import android.azadevs.movieic.data.remote.api.MovieApiService
import android.azadevs.movieic.utils.Constants
import android.azadevs.movieic.utils.MovieType
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * 22/07/24
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
    private val database: MovieDatabase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _moviesState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val moviesState = _moviesState.asStateFlow()

    init {
        savedStateHandle.get<String>(Constants.MOVIE_TYPE_ARG)?.let { movieType ->
            if (movieType == MovieType.NOW_PLAYING.name) {
                getNowPlayingMovies()
            } else {
                getTopRatedMovies()
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getNowPlayingMovies() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = 10,
                    initialLoadSize = INITIAL_LOAD_SIZE
                ),
                pagingSourceFactory = {
                    database.movieDao()
                        .getAllMoviesByType(MovieType.NOW_PLAYING.name)
                },
                remoteMediator = NowPlayingMovieSource(
                    movieApiService,
                    database,
                )
            ).flow.catch { exception ->
                _moviesState.value = when (exception) {
                    is IOException -> MoviesUiState.Error.NetworkError
                    is HttpException -> MoviesUiState.Error.HttpError
                    else -> MoviesUiState.Error.UnknownError(
                        exception.localizedMessage ?: "Unknown error occurred"
                    )
                }
            }.collect {
                _moviesState.value = MoviesUiState.Success(it)
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTopRatedMovies() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = 10,
                    initialLoadSize = INITIAL_LOAD_SIZE
                ),
                pagingSourceFactory = {
                    database.movieDao()
                        .getAllMoviesByType(MovieType.TOP_RATED.name)
                },
                remoteMediator = TopRatedMovieSource(
                    movieApiService,
                    database,
                )
            ).flow.catch { exception ->
                _moviesState.value = when (exception) {
                    is IOException -> MoviesUiState.Error.NetworkError
                    is HttpException -> MoviesUiState.Error.HttpError
                    else -> MoviesUiState.Error.UnknownError(
                        exception.localizedMessage ?: "Unknown error occurred"
                    )
                }
            }.collect {
                _moviesState.value = MoviesUiState.Success(it)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _moviesState.value = MoviesUiState.Loading
            savedStateHandle.get<String>(Constants.MOVIE_TYPE_ARG)?.let { movieType ->
                if (movieType == MovieType.NOW_PLAYING.name) {
                    getNowPlayingMovies()
                } else {
                    getTopRatedMovies()
                }
            }
        }

    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE = 20
    }
}