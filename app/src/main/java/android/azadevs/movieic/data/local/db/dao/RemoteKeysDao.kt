package android.azadevs.movieic.data.local.db.dao

import android.azadevs.movieic.data.local.db.entity.RemoteKeysEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Created by : Azamat Kalmurzayev
 * 22/07/24
 */
@Dao
interface RemoteKeysDao {

    @Upsert
    suspend fun upsertRemoteKeys(remoteKeys: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_key WHERE movie_id=:movieId AND movie_type=:movieType")
    suspend fun getRemoteKeysByMovieId(movieId: Int, movieType: String): RemoteKeysEntity?

    @Query("Select last_updated From remote_key WHERE movie_type=:movieType Order By last_updated DESC LIMIT 1")
    suspend fun getCreationTime(movieType: String): Long?

    @Query("DELETE FROM remote_key WHERE movie_type=:movieType")
    suspend fun clearRemoteKeys(movieType: String)
}