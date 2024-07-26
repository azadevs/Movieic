package android.azadevs.movieic.ui.main.adapter

import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.utils.DiffUtilCallback

/**
 * Created by : Azamat Kalmurzayev
 * 21/07/24
 */

val movieDiffUtilCallback = DiffUtilCallback<MovieEntity>({ oldItem, newItem ->
    oldItem.movieId == newItem.movieId
}, { oldItem, newItem ->
    oldItem == newItem
})