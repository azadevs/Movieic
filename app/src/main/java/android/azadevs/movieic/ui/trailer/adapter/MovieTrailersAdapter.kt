package android.azadevs.movieic.ui.trailer.adapter

import android.azadevs.movieic.data.remote.model.Video
import android.azadevs.movieic.databinding.ItemMovieTrailerBinding
import android.azadevs.movieic.utils.DiffUtilCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by : Azamat Kalmurzayev
 * 17/07/24
 */
class MovieTrailersAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<Video, MovieTrailersAdapter.MovieTrailerViewHolder>(callback) {

    inner class MovieTrailerViewHolder(private val binding: ItemMovieTrailerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Video) {
            binding.apply {
                tvVideoTitle.text = data.name
                tvSiteName.text = data.site
                root.setOnClickListener {
                    onItemClick.invoke(data.key)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTrailerViewHolder {
        return MovieTrailerViewHolder(
            ItemMovieTrailerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieTrailerViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val callback = DiffUtilCallback<Video>(
            { oldItem, newItem ->
                oldItem.id == newItem.id
            }, { oldItem, newItem ->
                oldItem == newItem
            })
    }

}

