package android.azadevs.movieic.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzayev
 * 22/07/24
 */
@Entity(tableName = "remote_key")
data class RemoteKeysEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "prev_key") val prevKey: Int?,
    @ColumnInfo(name = "next_key") val nextKey: Int?,
    @ColumnInfo(name = "current_page") val currentPage: Int,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "movie_type") val movieType: String? = null
)
