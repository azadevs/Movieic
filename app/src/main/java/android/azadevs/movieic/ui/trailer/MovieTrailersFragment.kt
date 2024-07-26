package android.azadevs.movieic.ui.trailer

import android.azadevs.movieic.R
import android.azadevs.movieic.databinding.FragmentMovieTrailersBinding
import android.azadevs.movieic.ui.trailer.adapter.MovieTrailersAdapter
import android.azadevs.movieic.ui.trailer.viewmodel.MovieTrailersUiState
import android.azadevs.movieic.ui.trailer.viewmodel.MovieTrailersViewModel
import android.azadevs.movieic.utils.UiText
import android.azadevs.movieic.utils.errorDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * Created by : Azamat Kalmurzayev
 * 14/07/24
 */
@AndroidEntryPoint
class MovieTrailersFragment : Fragment(R.layout.fragment_movie_trailers) {

    private var _binding: FragmentMovieTrailersBinding? = null

    private val binding get() = _binding!!

    private lateinit var trailersAdapter: MovieTrailersAdapter

    private val viewmodel: MovieTrailersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieTrailersBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        configureTrailerAdapter()

        viewLifecycleOwner.lifecycle.addObserver(binding.youTubePlayer)

        observerMovieVideosFlow()

    }

    private fun observerMovieVideosFlow() {
        viewmodel.movieVideosState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is MovieTrailersUiState.Error -> {
                        showError(state)
                    }

                    MovieTrailersUiState.Loading -> showLoading()
                    is MovieTrailersUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        trailersAdapter.submitList(state.data)
                        playVideo(state.data[0].key)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(state: MovieTrailersUiState.Error) {
        binding.progressBar.visibility = View.INVISIBLE
        val error = when (state) {
            is MovieTrailersUiState.Error.HttpError -> {
                UiText.StringResource(R.string.error_http).asString(requireContext())
            }

            MovieTrailersUiState.Error.NetworkError -> {
                UiText.StringResource(R.string.error_network).asString(requireContext())
            }

            is MovieTrailersUiState.Error.UnknownError -> {
                state.error
            }
        }
        MaterialAlertDialogBuilder(requireContext()).errorDialog(getString(R.string.error_occurred),
            error,
            onRetry = {
                viewmodel.refresh()
            },
            onCancel = {
                findNavController().popBackStack()
            })
    }

    private fun playVideo(videoKey: String) {
        binding.youTubePlayer.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadOrCueVideo(viewLifecycleOwner.lifecycle, videoKey, 0f)
            }
        })
    }

    private fun configureTrailerAdapter() {
        trailersAdapter = MovieTrailersAdapter { key ->
            playVideo(key)
        }
        binding.rvTrailer.adapter = trailersAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}