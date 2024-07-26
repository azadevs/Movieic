package android.azadevs.movieic.ui.detail.adapter

import android.azadevs.movieic.R
import android.azadevs.movieic.data.local.db.entity.CastEntity
import android.azadevs.movieic.databinding.ItemMovieCastBinding
import android.azadevs.movieic.utils.Constants
import android.azadevs.movieic.utils.DiffUtilCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by : Azamat Kalmurzayev
 * 02/07/24
 */
class MovieCastAdapter :
    ListAdapter<CastEntity, MovieCastAdapter.CastViewHolder>(callback) {
    inner class CastViewHolder(private val binding: ItemMovieCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(cast: CastEntity) {
            binding.tvCastName.text = cast.name
            Glide.with(binding.root.context).load("${Constants.IMAGE_URL}${cast.image}")
                .placeholder(R.drawable.placeholder).into(binding.ivCastImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            ItemMovieCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val callback = DiffUtilCallback<CastEntity>(
            { oldItem, newItem ->
                oldItem.castId == newItem.castId
            },
            { oldItem, newItem ->
                oldItem == newItem
            })
    }
}

