package android.azadevs.movieic.ui.bookmark.viewmodel

import android.azadevs.movieic.data.mapper.toMovieEntity
import android.azadevs.movieic.data.repository.MovieRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * 06/07/24
 */
@HiltViewModel
class BookmarkMoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    init {
        getBookmarkedMovies()
    }

    private var _bookmarkedMoviesState =
        MutableStateFlow<MovieBookmarkUiState>(MovieBookmarkUiState.Loading)
    val bookmarkedMoviesState get() = _bookmarkedMoviesState.asStateFlow()

    private fun getBookmarkedMovies() {
        viewModelScope.launch {
            repository.getBookmarkedMovies()
                .catch {
                    _bookmarkedMoviesState.value =
                        MovieBookmarkUiState.Error(it.localizedMessage ?: "Unknown error")
                }.collect { result ->
                    if (result.isEmpty()) {
                        _bookmarkedMoviesState.value = MovieBookmarkUiState.Empty
                    } else {
                        _bookmarkedMoviesState.value = MovieBookmarkUiState.Success(result.map {
                            it.toMovieEntity()
                        })
                    }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _bookmarkedMoviesState.value = MovieBookmarkUiState.Loading
            repository.getBookmarkedMovies()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.onClearJob()
    }
}

