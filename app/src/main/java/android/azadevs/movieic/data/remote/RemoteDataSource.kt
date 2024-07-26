package android.azadevs.movieic.data.remote

import android.azadevs.movieic.data.remote.api.MovieApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzayev
 * 10/07/24
 */
@Singleton
class RemoteDataSource @Inject constructor(
    private val movieService: MovieApiService
) {

    suspend fun getTopRatedMovies() = movieService.getTopRatedMovies()

    suspend fun getNowPlayingMovies() = movieService.getNowPlayingMovies()

    suspend fun getMovieDetailsById(movieId: Int) = movieService.getMovieDetailsById(movieId)

    suspend fun getMovieVideosById(movieId: Int) = movieService.getMovieVideosById(movieId)

    suspend fun getMovieCastById(movieId: Int) = movieService.getMovieCastById(movieId)

}