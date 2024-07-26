package android.azadevs.movieic.ui.bookmark

import android.azadevs.movieic.R
import android.azadevs.movieic.data.local.db.entity.MovieEntity
import android.azadevs.movieic.databinding.FragmentMovieBookmarksBinding
import android.azadevs.movieic.ui.bookmark.adapter.BookmarkMoviesAdapter
import android.azadevs.movieic.ui.bookmark.viewmodel.BookmarkMoviesViewModel
import android.azadevs.movieic.ui.bookmark.viewmodel.MovieBookmarkUiState
import android.azadevs.movieic.ui.trailer.viewmodel.MovieTrailersUiState
import android.azadevs.movieic.utils.UiText
import android.azadevs.movieic.utils.errorDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzayev
 * 05/07/24
 */
@AndroidEntryPoint
class BookmarksMovieFragment : Fragment(R.layout.fragment_movie_bookmarks) {

    private var _binding: FragmentMovieBookmarksBinding? = null

    private val binding get() = _binding!!

    private val viewmodel: BookmarkMoviesViewModel by viewModels()

    private lateinit var bookmarkAdapter: BookmarkMoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieBookmarksBinding.bind(view)

        configureRecyclerView()

        observeMovies()

    }

    private fun observeMovies() {
        viewmodel.bookmarkedMoviesState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    MovieBookmarkUiState.Empty -> showEmptyData()
                    is MovieBookmarkUiState.Error -> showError(state.error)
                    is MovieBookmarkUiState.Success -> showData(state.movieEntityData)
                    MovieBookmarkUiState.Loading -> {
                        showLoading()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showLoading() {
        binding.apply {
            ivNoMoviesImage.visibility = View.INVISIBLE
            tvNoMoviesLabel.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showData(movieEntityData: List<MovieEntity>) {
        binding.apply {
            ivNoMoviesImage.visibility = View.INVISIBLE
            tvNoMoviesLabel.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            bookmarkAdapter.submitList(movieEntityData)
        }
    }

    private fun showError(error: String) {
        binding.apply {
            ivNoMoviesImage.visibility = View.INVISIBLE
            tvNoMoviesLabel.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
        }
        MaterialAlertDialogBuilder(requireContext()).errorDialog(getString(R.string.error_occurred),
            error,
            onRetry = {
                viewmodel.refresh()
            },
            onCancel = {
                requireActivity().finish()
            })
    }

    private fun showEmptyData() {
        binding.apply {
            ivNoMoviesImage.visibility = View.VISIBLE
            tvNoMoviesLabel.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun configureRecyclerView() {
        bookmarkAdapter = BookmarkMoviesAdapter { movieId ->
            val action =
                BookmarksMovieFragmentDirections.actionBookmarkMoviesFragmentToMovieDetailsFragment(
                    movieId
                )
            findNavController().navigate(action)
        }
        binding.rvMovies.apply {
            adapter = bookmarkAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}