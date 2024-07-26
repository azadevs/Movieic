package android.azadevs.movieic.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by : Azamat Kalmurzayev
 * 06/07/24
 */
@Entity(tableName = "movies_table")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "movie_image") val movieImage: String?=null,
    @ColumnInfo(name = "rate") val rate: Double,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "genres") val genres: List<String>,
    @ColumnInfo(name = "movie_type") val movieType: String? = null,
    @ColumnInfo(name = "time_stamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "page") val page: Int = 1
)