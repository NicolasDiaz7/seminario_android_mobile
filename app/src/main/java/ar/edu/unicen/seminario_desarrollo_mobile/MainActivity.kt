package ar.edu.unicen.seminario_desarrollo_mobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import ar.edu.unicen.seminario_desarrollo_mobile.ui.MainViewModel
import ar.edu.unicen.seminario_desarrollo_mobile.ui.PopularMoviesScreen
import ar.edu.unicen.seminario_desarrollo_mobile.ui.theme.SeminarioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeminarioTheme {
                PopularMoviesScreen(
                    viewModel = viewModel,
                    onMovieClick = { movieId ->
                        val intent = Intent(this, MovieDetailActivity::class.java)
                        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieId)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}