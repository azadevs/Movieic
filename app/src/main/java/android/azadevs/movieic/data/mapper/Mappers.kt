package android.azadevs.movieic.data.mapper

import android.azadevs.movieic.data.local.db.entity.CastEntity
import android.azadevs.movieic.data.local.db.entity.MovieDetailsEntity
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.data.local.db.entity.MovieWithCastEntity
import android.azadevs.movieic.data.remote.model.Cast
import android.azadevs.movieic.data.remote.model.Film
import android.azadevs.movieic.data.remote.model.MovieCastResponse
import android.azadevs.movieic.data.remote.model.MovieDetailsResponse
import android.azadevs.movieic.utils.getCurrentMovieGenres

/**
 * Created by : Azamat Kalmurzayev
 * 11/07/24
 */

fun Film.toMovieEntity() = MovieEntity(
    movieId = id,
    title = title,
    movieImage = backdropPath,
    rate = voteAverage,
    releaseDate = releaseDate,
    genres = getCurrentMovieGenres(genreIds)
)

fun MovieDetailsResponse.toMovieDetails() = MovieDetailsEntity(
    id = this.id,
    title = this.title,
    movieImage = this.backdropPath,
    rate = this.voteAverage,
    releaseDate = this.releaseDate,
    genres = this.genres.map {
        it.name
    },
    runtime = this.runtime,
    overview = this.overview,
    language = this.languages.map {
        it.englishName
    }.lastOrNull() ?: "Unknown"
)

fun MovieDetailsEntity.toMovieEntity() = MovieEntity(
    movieId = id,
    title = title,
    movieImage = movieImage,
    rate = rate,
    releaseDate = releaseDate,
    genres = genres
)

fun MovieCastResponse.toMovieWithCastEntity() = MovieWithCastEntity(
    movieId = id,
    cast = cast.map {
        it.toCastEntity()
    },
    timeStamp = System.currentTimeMillis()
)

fun Cast.toCastEntity() = CastEntity(
    castId = castId,
    name = name,
    image = profilePath
)