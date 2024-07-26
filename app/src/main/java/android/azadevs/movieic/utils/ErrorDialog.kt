package android.azadevs.movieic.utils

import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by : Azamat Kalmurzayev
 * 25/07/24
 */

fun MaterialAlertDialogBuilder.errorDialog(
    title: String,
    message: String,
    onCancel: () -> Unit,
    onRetry: () -> Unit
) {
    setCancelable(false)
    setTitle(title)
    setMessage(message)
    setNegativeButton("Cancel") { dialog, _ ->
        onCancel()
        dialog.cancel()
    }
    setPositiveButton("Retry") { dialog, _ ->
        onRetry()
        dialog.dismiss()
    }.show()
}