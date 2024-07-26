package android.azadevs.movieic.utils

import java.util.Locale
import java.util.concurrent.TimeUnit

object Constants {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

    val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    const val MOVIE_ID_ARG = "movieId"

    const val MOVIE_TYPE_ARG = "movieType"

    fun makeTimeFormat(minute: Int): String {
        val hour = minute / 60
        val min = minute % 60
        return String.format(Locale.getDefault(), "%02dh %02dmin", hour, min)
    }
}