package android.azadevs.movieic.ui.main.adapter

import android.azadevs.movieic.R
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.databinding.ItemMovieVerticalBinding
import android.azadevs.movieic.utils.Constants
import android.azadevs.movieic.utils.configureGenresChipGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale

/**
 * Created by : Azamat Kalmurzayev
 * 27/06/24
 */

class TopRatedMovieAdapter(
    val onItemClick: (Int) -> Unit
) :
    ListAdapter<MovieEntity, TopRatedMovieAdapter.PopularViewHolder>(movieDiffUtilCallback) {

    inner class PopularViewHolder(private val binding: ItemMovieVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(movieEntity: MovieEntity) {
            binding.apply {
                tvMovieTitle.text = movieEntity.title
                tvMovieRate.text =
                    root.resources.getString(
                        R.string.text_rate,
                        String.format(Locale.getDefault(), "%.1f", movieEntity.rate)
                    )
                Glide.with(root).load("${Constants.IMAGE_URL}${movieEntity.movieImage}")
                    .error(R.drawable.error_image).placeholder(R.drawable.placeholder).into(ivMovie)
                configureGenresChipGroup(movieEntity.genres, chipGroup, root.context)
                tvReleaseDate.text = movieEntity.releaseDate
                root.setOnClickListener {
                    onItemClick.invoke(movieEntity.movieId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            ItemMovieVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}