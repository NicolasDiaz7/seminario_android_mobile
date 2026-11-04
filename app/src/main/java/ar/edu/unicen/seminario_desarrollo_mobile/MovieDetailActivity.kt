package ar.edu.unicen.seminario_desarrollo_mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import ar.edu.unicen.seminario_desarrollo_mobile.ui.MovieDetailScreen
import ar.edu.unicen.seminario_desarrollo_mobile.ui.MovieDetailViewModel
import ar.edu.unicen.seminario_desarrollo_mobile.ui.theme.SeminarioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : ComponentActivity() {

    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        if (savedInstanceState == null && movieId != -1) {
            viewModel.loadMovieDetail(movieId)
        }

        setContent {
            SeminarioTheme {
                MovieDetailScreen(
                    movieId = movieId,
                    viewModel = viewModel,
                    onBackClick = { finish() },
                    onShareClick = { movie -> shareMovie(movie) },
                    onOpenHomepageClick = { url -> openHomepage(url) }
                )
            }
        }
    }

    private fun shareMovie(movie: MovieDetail) {
        val synopsis = movie.overview.ifBlank { getString(R.string.no_synopsis_available) }
        val shareText = getString(R.string.share_movie_message, movie.title, synopsis)

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        val chooserIntent = Intent.createChooser(sendIntent, getString(R.string.share_movie))
        startActivity(chooserIntent)
    }

    private fun openHomepage(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}