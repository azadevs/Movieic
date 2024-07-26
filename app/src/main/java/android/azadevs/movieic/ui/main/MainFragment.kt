package android.azadevs.movieic.ui.main

import android.azadevs.movieic.R
import android.azadevs.movieic.databinding.FragmentMainBinding
import android.azadevs.movieic.ui.main.adapter.NowPlayingMovieAdapter
import android.azadevs.movieic.ui.main.adapter.TopRatedMovieAdapter
import android.azadevs.movieic.ui.main.viewmodel.MainUIState
import android.azadevs.movieic.ui.main.viewmodel.MainViewModel
import android.azadevs.movieic.utils.MovieType
import android.azadevs.movieic.utils.UiText
import android.azadevs.movieic.utils.errorDialog
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private lateinit var nowPlayingMovieAdapter: NowPlayingMovieAdapter

    private lateinit var topRatedMovieAdapter: TopRatedMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        configurePopularAdapter()

        configureNowPlayingAdapter()

        observeNowPlayingMovies()

        observeTopRatedMovies()

        binding.btnSeeMoreNowPlaying.setOnClickListener {
            val action =
                MainFragmentDirections.actionMoviesFragmentToSeeMoreMoviesFragment(MovieType.NOW_PLAYING.name)
            findNavController().navigate(action)
        }

        binding.btnSeeMoreTopRated.setOnClickListener {
            val action =
                MainFragmentDirections.actionMoviesFragmentToSeeMoreMoviesFragment(MovieType.TOP_RATED.name)
            findNavController().navigate(action)
        }

    }

    private fun observeNowPlayingMovies() {
        viewModel.nowPlayingMovies
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is MainUIState.Error -> {
                        showError(state, binding.progressBarNowPlaying)
                    }

                    MainUIState.Loading -> {
                        binding.progressBarNowPlaying.visibility = View.VISIBLE
                    }

                    is MainUIState.Success -> {
                        nowPlayingMovieAdapter.submitList(state.data)
                        binding.progressBarNowPlaying.visibility = View.INVISIBLE
                    }
                }
            }.launchIn(
                viewLifecycleOwner.lifecycleScope
            )
    }

    private fun observeTopRatedMovies() {
        viewModel.popularMovies
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is MainUIState.Error -> {
                        showError(state, binding.progressBarTopRated)
                    }

                    MainUIState.Loading -> {
                        binding.progressBarTopRated.visibility = View.VISIBLE
                    }

                    is MainUIState.Success -> {
                        topRatedMovieAdapter.submitList(state.data)
                        binding.progressBarTopRated.visibility = View.INVISIBLE
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configurePopularAdapter() {
        topRatedMovieAdapter = TopRatedMovieAdapter { id ->
            val action =
                MainFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
            findNavController().navigate(action)
        }
        binding.apply {
            rvTopRated.setHasFixedSize(true)
            rvTopRated.adapter = topRatedMovieAdapter
            rvTopRated.isNestedScrollingEnabled = false
        }
    }

    private fun configureNowPlayingAdapter() {
        nowPlayingMovieAdapter = NowPlayingMovieAdapter { id ->
            val action = MainFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
            findNavController().navigate(action)
        }
        binding.rvNowPlaying.setHasFixedSize(true)
        binding.rvNowPlaying.adapter = nowPlayingMovieAdapter
    }

    private fun showError(state: MainUIState.Error, progressBar: ProgressBar) {
        progressBar.visibility = View.INVISIBLE
        val error = when (state) {
            is MainUIState.Error.HttpError -> {
                UiText.StringResource(R.string.error_http).asString(requireContext())
            }

            MainUIState.Error.NetworkError -> {
                UiText.StringResource(R.string.error_network).asString(requireContext())
            }

            is MainUIState.Error.UnknownError -> {
                state.error
            }
        }
        MaterialAlertDialogBuilder(requireContext()).errorDialog(getString(R.string.error_occurred),
            error,
            onRetry = {
                viewModel.refresh()
            },
            onCancel = {
                requireActivity().finish()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}