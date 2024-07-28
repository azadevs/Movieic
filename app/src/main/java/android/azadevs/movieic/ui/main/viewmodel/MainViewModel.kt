package android.azadevs.movieic.ui.main.viewmodel

import android.azadevs.movieic.data.repository.MovieRepository
import android.azadevs.movieic.utils.MovieType
import android.azadevs.movieic.utils.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * 26/06/24
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private var _popularMovies = MutableStateFlow<MainUIState>(MainUIState.Loading)
    val popularMovies = _popularMovies.asStateFlow()

    private var _nowPlayingMovies = MutableStateFlow<MainUIState>(MainUIState.Loading)
    val nowPlayingMovies = _nowPlayingMovies.asStateFlow()

    init {
        getTopRatedMovies()
        getNowPlayingMovies()
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            repository.getCachedMovies(MovieType.TOP_RATED)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> _popularMovies.value = when (result) {
                            Resource.Error.HttpError -> MainUIState.Error.HttpError
                            Resource.Error.NetworkError -> MainUIState.Error.NetworkError
                            is Resource.Error.UnknownError -> MainUIState.Error.UnknownError(result.error)
                        }

                        Resource.Loading -> _popularMovies.value = MainUIState.Loading
                        is Resource.Success -> _popularMovies.value =
                            MainUIState.Success(result.data)
                    }
                }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            repository.getCachedMovies(MovieType.NOW_PLAYING).collect { result ->
                when (result) {
                    is Resource.Error -> _nowPlayingMovies.value = when (result) {
                        Resource.Error.HttpError -> MainUIState.Error.HttpError
                        Resource.Error.NetworkError -> MainUIState.Error.NetworkError
                        is Resource.Error.UnknownError -> MainUIState.Error.UnknownError(result.error)
                    }

                    Resource.Loading -> _nowPlayingMovies.value = MainUIState.Loading
                    is Resource.Success -> _nowPlayingMovies.value =
                        MainUIState.Success(result.data)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _popularMovies.value = MainUIState.Loading
            _nowPlayingMovies.value = MainUIState.Loading
            getTopRatedMovies()
            getNowPlayingMovies()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.onClearJob()
    }
}

