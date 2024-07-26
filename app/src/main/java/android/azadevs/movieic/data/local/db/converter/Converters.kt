package android.azadevs.movieic.data.local.db.converter

import android.azadevs.movieic.data.local.db.entity.CastEntity
import android.azadevs.movieic.data.remote.model.Cast
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by : Azamat Kalmurzayev
 * 07/07/24
 */
class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",")
    }


    @TypeConverter
    fun fromCastList(value: List<CastEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCastList(value: String): List<CastEntity> {
        val type = object : TypeToken<List<CastEntity>>() {}.type
        return gson.fromJson(value, type)
    }

    companion object {
        val gson = Gson()
    }
}