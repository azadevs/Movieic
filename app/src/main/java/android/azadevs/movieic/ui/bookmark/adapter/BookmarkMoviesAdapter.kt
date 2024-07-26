package android.azadevs.movieic.ui.bookmark.adapter

import android.azadevs.movieic.R
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.databinding.ItemMovieVerticalBinding
import android.azadevs.movieic.utils.Constants
import android.azadevs.movieic.utils.DiffUtilCallback
import android.azadevs.movieic.utils.addChip
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale

/**
 * Created by : Azamat Kalmurzayev
 * 06/07/24
 */
class BookmarkMoviesAdapter(
    private val onItemClick: (movieId: Int) -> Unit,
) :
    ListAdapter<MovieEntity, BookmarkMoviesAdapter.BookmarkMoviesViewHolder>(callback) {
    inner class BookmarkMoviesViewHolder(
        private val binding: ItemMovieVerticalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(movieEntity: MovieEntity) {
            binding.apply {
                tvMovieTitle.text = movieEntity.title
                Glide.with(root).load("${Constants.IMAGE_URL}${movieEntity.movieImage}")
                    .placeholder(R.drawable.placeholder).error(R.drawable.error_image).into(ivMovie)
                tvMovieRate.text = binding.root.context.getString(
                    R.string.text_rate, String.format(Locale.getDefault(), "%.1f", movieEntity.rate)
                )
                tvReleaseDate.text = movieEntity.releaseDate
                movieEntity.genres.forEach { genre ->
                    chipGroup.addChip(genre, binding.root.context)
                }
                root.setOnClickListener {
                    onItemClick.invoke(movieEntity.movieId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkMoviesViewHolder {
        return BookmarkMoviesViewHolder(
            ItemMovieVerticalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BookmarkMoviesViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val callback = DiffUtilCallback<MovieEntity>({ oldItem, newItem ->
            oldItem.movieId == newItem.movieId
        }, { oldItem, newItem ->
            oldItem == newItem
        })
    }
}

