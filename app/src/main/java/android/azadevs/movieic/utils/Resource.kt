package android.azadevs.movieic.utils

/**
 * Created by : Azamat Kalmurzayev
 * 28/06/24
 */
sealed class Resource<out T> {

    sealed class Error : Resource<Nothing>() {
        data object NetworkError : Error()

        data object HttpError : Error()

        data class UnknownError(val error: String) : Error()
    }

    data class Success<T>(val data: T) : Resource<T>()

    data object Loading : Resource<Nothing>()

}