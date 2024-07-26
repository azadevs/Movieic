package android.azadevs.movieic.utils

import android.icu.text.DateIntervalFormat.FormattedDateInterval

/**
 * Created by : Azamat Kalmurzayev
 * 28/06/24
 */

val genresMap = mutableMapOf(
    28 to "Action",
    12 to "Adventure",
    16 to "Animation",
    35 to "Comedy",
    80 to "Crime",
    99 to "Documentary",
    18 to "Drama",
    10751 to "Family",
    14 to "Fantasy",
    36 to "History",
    27 to "Horror",
    10402 to "Music",
    9648 to "Mystery",
    10749 to "Romance",
    878 to "Science Fiction",
    10770 to "Tv Movie",
    53 to "Thriller",
    10752 to "War",
    37 to "Western"
)

fun getCurrentMovieGenres(genreIds: List<Int>): List<String> {
    val genres = mutableListOf<String>()
    for (i in genreIds) {
        genres.add(genresMap[i] ?: "Unknown")
    }
    return genres
}