package android.azadevs.movieic.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzayev
 * 12/07/24
 */

@Entity(tableName = "movie_details_table")
data class MovieDetailsEntity(
    @ColumnInfo(name = "movie_id") @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "movie_title") val title: String,
    @ColumnInfo(name = "movie_image") val movieImage: String,
    @ColumnInfo(name = "movie_rate") val rate: Double,
    @ColumnInfo(name = "movie_release_date") val releaseDate: String,
    @ColumnInfo(name = "movie_genres") val genres: List<String>,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean = false,
    @ColumnInfo(name = "movie_language") val language: String,
    @ColumnInfo(name = "movie_overview") val overview: String,
    @ColumnInfo(name = "movie_runtime") val runtime: Int,
    @ColumnInfo(name = "time_stamp") val timestamp: Long = System.currentTimeMillis()
)