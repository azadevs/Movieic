package android.azadevs.movieic.di

import android.azadevs.movieic.data.local.db.MovieDatabase
import android.azadevs.movieic.data.local.db.dao.MovieCastDao
import android.azadevs.movieic.data.local.db.dao.MovieDao
import android.azadevs.movieic.data.local.db.dao.MovieDetailsDao
import android.azadevs.movieic.data.local.db.dao.RemoteKeysDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzayev
 * 06/07/24
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieDetailsDao(movieDatabase: MovieDatabase): MovieDetailsDao {
        return movieDatabase.detailsDao()
    }

    @Provides
    @Singleton
    fun provideMovieCastDao(movieDatabase: MovieDatabase): MovieCastDao {
        return movieDatabase.castDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(movieDatabase: MovieDatabase): RemoteKeysDao {
        return movieDatabase.remoteKeysDao()
    }

}