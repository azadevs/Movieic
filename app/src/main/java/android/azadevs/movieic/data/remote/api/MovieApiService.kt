package android.azadevs.movieic.data.remote.api

import android.azadevs.movieic.BuildConfig
import android.azadevs.movieic.data.remote.model.MovieCastResponse
import android.azadevs.movieic.data.remote.model.MovieDetailsResponse
import android.azadevs.movieic.data.remote.model.MovieResponse
import android.azadevs.movieic.data.remote.model.MovieVideosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by : Azamat Kalmurzayev
 * 26/06/24
 */
interface MovieApiService {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") key: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") key: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String = BuildConfig.API_KEY
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideosById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String = BuildConfig.API_KEY
    ): MovieVideosResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCastById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String = BuildConfig.API_KEY
    ): MovieCastResponse
}