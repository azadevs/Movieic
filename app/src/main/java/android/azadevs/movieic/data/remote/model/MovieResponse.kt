package android.azadevs.movieic.data.remote.model


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val films: List<Film>,
    @SerializedName("total_pages")
    val totalPages: Int
)