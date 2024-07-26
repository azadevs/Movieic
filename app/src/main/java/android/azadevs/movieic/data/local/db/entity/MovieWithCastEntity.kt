package android.azadevs.movieic.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzayev
 * 12/07/24
 */
@Entity(tableName = "movie_cast_table")
data class MovieWithCastEntity(
    @ColumnInfo(name = "movie_id") @PrimaryKey(autoGenerate = false) val movieId: Int,
    @ColumnInfo(name = "movie_cast") val cast: List<CastEntity>,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long = System.currentTimeMillis()
)

