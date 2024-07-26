package android.azadevs.movieic.ui.movies

import android.azadevs.movieic.R
import android.azadevs.movieic.databinding.FragmentMoviesBinding
import android.azadevs.movieic.ui.movies.adapter.LinearLayoutManagerWrapper
import android.azadevs.movieic.ui.movies.adapter.MoviesPagingAdapter
import android.azadevs.movieic.ui.movies.viewmodel.MoviesUiState
import android.azadevs.movieic.ui.movies.viewmodel.MoviesViewModel
import android.azadevs.movieic.utils.UiText
import android.azadevs.movieic.utils.errorDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzayev
 * 07/07/24
 */
@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private var _binding: FragmentMoviesBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    private val moviesAdapter by lazy {
        MoviesPagingAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMoviesBinding.bind(view)

        configureRecyclerView()

        observerMoviesFlow()
    }

    private fun observerMoviesFlow() {
        viewModel.moviesState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { state ->
            when (state) {
                is MoviesUiState.Error -> {
                    showError(state)
                }

                MoviesUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is MoviesUiState.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    moviesAdapter.submitData(state.data)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showError(state: MoviesUiState.Error) {
        binding.progressBar.visibility = View.INVISIBLE
        val error = when (state) {
            is MoviesUiState.Error.HttpError -> {
                UiText.StringResource(R.string.error_http).asString(requireContext())
            }

            MoviesUiState.Error.NetworkError -> {
                UiText.StringResource(R.string.error_network).asString(requireContext())
            }

            is MoviesUiState.Error.UnknownError -> {
                state.error
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

    private fun configureRecyclerView() {
        binding.rvMovies.adapter = moviesAdapter
        binding.rvMovies.layoutManager = LinearLayoutManagerWrapper(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}