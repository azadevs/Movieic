package android.azadevs.movieic.data.local.db.dao

import android.azadevs.movieic.data.local.db.entity.MovieDetailsEntity
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzayev
 * 06/07/24
 */
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies_table WHERE movie_type=:movieType")
    suspend fun getAllMovies(movieType: String): List<MovieEntity>

    @Query("SELECT * FROM movie_details_table WHERE is_bookmarked=1")
    fun getBookmarkedMovies(): Flow<List<MovieDetailsEntity>>

    @Query("DELETE FROM movies_table WHERE movie_type=:type")
    suspend fun clearMoviesByType(type: String)

    @Query("SELECT * FROM movies_table WHERE movie_type=:movieType")
    fun getAllMoviesByType(movieType: String): PagingSource<Int, MovieEntity>


}