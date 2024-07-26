package android.azadevs.movieic.data.local.db

import android.azadevs.movieic.data.local.db.converter.Converters
import android.azadevs.movieic.data.local.db.dao.MovieCastDao
import android.azadevs.movieic.data.local.db.dao.MovieDao
import android.azadevs.movieic.data.local.db.dao.MovieDetailsDao
import android.azadevs.movieic.data.local.db.dao.RemoteKeysDao
import android.azadevs.movieic.data.local.db.entity.MovieWithCastEntity
import android.azadevs.movieic.data.local.db.entity.MovieDetailsEntity
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.data.local.db.entity.RemoteKeysEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by : Azamat Kalmurzayev
 * 06/07/24
 */
@Database(
    entities = [MovieEntity::class, MovieDetailsEntity::class, MovieWithCastEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun castDao(): MovieCastDao

    abstract fun detailsDao(): MovieDetailsDao

    abstract fun remoteKeysDao(): RemoteKeysDao

}