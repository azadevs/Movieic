package android.azadevs.movieic.data.paging

import android.azadevs.movieic.data.local.db.MovieDatabase
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.data.local.db.entity.RemoteKeysEntity
import android.azadevs.movieic.data.mapper.toMovieEntity
import android.azadevs.movieic.data.remote.api.MovieApiService
import android.azadevs.movieic.utils.Constants.cacheTimeout
import android.azadevs.movieic.utils.MovieType
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import okio.IOException
import retrofit2.HttpException

/**
 * Created by : Azamat Kalmurzayev
 * 21/07/24
 */
@OptIn(ExperimentalPagingApi::class)
class TopRatedMovieSource(
    private val apiService: MovieApiService, private val database: MovieDatabase
) : RemoteMediator<Int, MovieEntity>() {

    val movieType = MovieType.TOP_RATED.name

    override suspend fun initialize(): InitializeAction {

        return if (System.currentTimeMillis() - (database.remoteKeysDao().getCreationTime(movieType)
                ?: 0) < cacheTimeout
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }
        try {
            val response = apiService.getTopRatedMovies(page)
            val movies = response.films
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys(movieType)
                    database.movieDao().clearMoviesByType(movieType)
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (movies.isEmpty()) null else page + 1
                val remoteKeys = movies.map {
                    RemoteKeysEntity(
                        movieId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        currentPage = page,
                        movieType = movieType
                    )
                }
                database.remoteKeysDao().upsertRemoteKeys(remoteKeys)
                database.movieDao().insertMovies(movies.map {
                    it.toMovieEntity().copy(movieType = movieType, page = page)
                })
            }
            return MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieId?.let { id ->
                database.remoteKeysDao().getRemoteKeysByMovieId(id, movieType)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            database.remoteKeysDao().getRemoteKeysByMovieId(movie.movieId, movieType)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {

        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            database.remoteKeysDao().getRemoteKeysByMovieId(movie.movieId, movieType)
        }
    }
}