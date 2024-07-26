package android.azadevs.movieic.data.local.db.dao

import android.azadevs.movieic.data.local.db.entity.MovieWithCastEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Created by : Azamat Kalmurzayev
 * 22/07/24
 */
@Dao
interface MovieCastDao {

    @Upsert
    suspend fun upsertCast(cast: MovieWithCastEntity)

    @Query("SELECT EXISTS(SELECT * FROM movie_cast_table WHERE movie_id=:movieId)")
    suspend fun isExistMovieCast(movieId: Int): Boolean

    @Query("SELECT * FROM movie_cast_table WHERE movie_id=:movieId")
    suspend fun getMovieCastById(movieId: Int): MovieWithCastEntity

}