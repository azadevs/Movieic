package android.azadevs.movieic.utils

import android.content.Context
import androidx.annotation.StringRes

/**
 * Created by : Azamat Kalmurzayev
 * 26/07/24
 */
sealed class UiText {
    data class DynamicString(
        val value: String
    ) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}
