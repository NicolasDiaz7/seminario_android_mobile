package ar.edu.unicen.seminario_desarrollo_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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

        // Recuperamos el ID de la película que nos mandó MainActivity en el Intent
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        // Solo pedimos los datos la primera vez que se crea la Activity
        // (si savedInstanceState != null significa que solo giró la pantalla y el ViewModel ya tiene los datos)
        if (savedInstanceState == null && movieId != -1) {
            viewModel.loadMovieDetail(movieId)
        }

        setContent {
            SeminarioTheme {
                MovieDetailScreen(
                    movieId = movieId,
                    viewModel = viewModel,
                    onBackClick = { finish() } // Cierra esta Activity y vuelve a la anterior en el Back Stack
                )
            }
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}