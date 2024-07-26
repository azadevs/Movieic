package android.azadevs.movieic.data.local

import android.azadevs.movieic.data.local.db.dao.MovieCastDao
import android.azadevs.movieic.data.local.db.dao.MovieDao
import android.azadevs.movieic.data.local.db.dao.MovieDetailsDao
import android.azadevs.movieic.data.local.db.entity.MovieDetailsEntity
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.data.local.db.entity.MovieWithCastEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzayev
 * 10/07/24
 */
@Singleton
class LocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val castDao: MovieCastDao,
    private val detailsDao: MovieDetailsDao
) {

    // Movie dao functions

    suspend fun insertMovies(movies: List<MovieEntity>) = movieDao.insertMovies(movies)

    fun getBookmarkedMovies() = movieDao.getBookmarkedMovies()

    suspend fun getAllMovies(movieType: String) = movieDao.getAllMovies(movieType)

    // Movie details dao functions

    suspend fun upsertMovieDetails(movieDetails: MovieDetailsEntity) =
        detailsDao.upsertMovieDetails(movieDetails)

    suspend fun getMovieDetailsById(movieId: Int) = detailsDao.getMovieDetailsById(movieId)

    suspend fun isExistMovieDetails(movieId: Int) = detailsDao.isExistMovieDetails(movieId)

    suspend fun clearMoviesByType(type: String) {
        movieDao.clearMoviesByType(type)
    }

    // Cast dao functions

    suspend fun upsertCast(cast: MovieWithCastEntity) = castDao.upsertCast(cast)

    suspend fun getMovieCastById(movieId: Int) = castDao.getMovieCastById(movieId)

    suspend fun isExistMovieCast(movieId: Int) = castDao.isExistMovieCast(movieId)


}