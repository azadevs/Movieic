package android.azadevs.movieic.utils

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<T : Any>(
    private val mAreItemsTheSame: (T, T) -> Boolean,
    private val mAreContentsTheSame: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return mAreItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return mAreContentsTheSame(oldItem, newItem)
    }
}