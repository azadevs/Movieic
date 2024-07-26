package android.azadevs.movieic.data.local.db.entity

import androidx.room.ColumnInfo

data class CastEntity(
    @ColumnInfo(name = "cast_id") val castId: Int,
    @ColumnInfo(name = "cast_name") val name: String,
    @ColumnInfo(name = "cast_image") val image: String?=null
)