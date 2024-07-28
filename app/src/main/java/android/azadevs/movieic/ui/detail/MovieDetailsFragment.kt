package android.azadevs.movieic.ui.detail

import android.azadevs.movieic.R
import android.azadevs.movieic.data.local.db.entity.MovieDetailsEntity
import android.azadevs.movieic.databinding.FragmentMovieDetailsBinding
import android.azadevs.movieic.ui.detail.adapter.MovieCastAdapter
import android.azadevs.movieic.ui.detail.viewmodel.MovieDetailsUiState
import android.azadevs.movieic.ui.detail.viewmodel.MovieDetailsViewModel
import android.azadevs.movieic.utils.Constants
import android.azadevs.movieic.utils.Constants.makeTimeFormat
import android.azadevs.movieic.utils.UiText
import android.azadevs.movieic.utils.addChip
import android.azadevs.movieic.utils.errorDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

/**
 * Created by : Azamat Kalmurzayev
 * 30/06/24
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val castAdapter by lazy {
        MovieCastAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMovieDetailsBinding.bind(view)

        observeMovieDetails()

        observeMovieCast()

        observeNavigationEvent()

        binding.rvCast.adapter = castAdapter

        binding.ivBookmark.setOnClickListener {
            binding.ivBookmark.isSelected = !binding.ivBookmark.isSelected
            configureBookmarkIcon()
            viewModel.upsertBookmark()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.navigateToTrailer()
        }

    }

    private fun observeNavigationEvent() {
        viewModel.navigationEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            val action =
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieTrailerFragment(
                    it
                )
            findNavController().navigate(action)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeMovieCast() {
        viewModel.movieCastState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { state ->
            when (state) {
                is MovieDetailsUiState.Error -> showError(state)
                MovieDetailsUiState.Loading -> showLoading()
                is MovieDetailsUiState.Success -> castAdapter.submitList(state.data)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun observeMovieDetails() {
        viewModel.movieDetailsState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is MovieDetailsUiState.Error -> showError(state)
                    MovieDetailsUiState.Loading -> showLoading()
                    is MovieDetailsUiState.Success -> configureUIMovieDetails(state.data)
                }

            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureUIMovieDetails(details: MovieDetailsEntity) {
        binding.apply {
            ivBookmark.isSelected = details.isBookmarked
            configureBookmarkIcon()
            frameLoading.visibility = View.INVISIBLE
            btnPlay.visibility = View.VISIBLE
            tvMovieRate.text = getString(
                R.string.text_rate, String.format(Locale.getDefault(), "%.1f", details.rate)
            )
            tvMovieTitle.text = details.title
            tvMovieLength.text = makeTimeFormat(details.runtime)
            tvMovieRating.text = details.rate.toString()
            tvMovieLanguage.text = details.language
            tvDescription.text = details.overview
            details.genres.forEach { genre ->
                chipGroup.addChip(genre, requireContext())
            }
            Glide.with(requireContext()).load("${Constants.IMAGE_URL}${details.movieImage}")
                .placeholder(R.drawable.placeholder).error(R.drawable.error_image)
                .into(ivMovieImage)
        }
    }

    private fun configureBookmarkIcon() {
        if (binding.ivBookmark.isSelected) {
            binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_border)
        }
    }

    private fun showLoading() {
        binding.frameLoading.visibility = View.VISIBLE
        binding.btnPlay.visibility = View.INVISIBLE
    }

    private fun showError(state: MovieDetailsUiState.Error) {
        binding.frameLoading.visibility = View.GONE
        binding.btnPlay.visibility = View.VISIBLE
        val error = when (state) {
            is MovieDetailsUiState.Error.HttpError -> {
                UiText.StringResource(R.string.error_http).asString(requireContext())
            }

            MovieDetailsUiState.Error.NetworkError -> {
                UiText.StringResource(R.string.error_network).asString(requireContext())
            }

            is MovieDetailsUiState.Error.UnknownError -> {
                state.error.asString(requireContext())
            }
        }
        MaterialAlertDialogBuilder(requireContext()).errorDialog(getString(R.string.error_occurred),
            error,
            onRetry = {
                viewModel.refresh()
            },
            onCancel = {
                findNavController().popBackStack()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}