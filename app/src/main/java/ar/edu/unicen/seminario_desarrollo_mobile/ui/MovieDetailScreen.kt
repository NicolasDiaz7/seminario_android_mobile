package ar.edu.unicen.seminario_desarrollo_mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.edu.unicen.seminario_desarrollo_mobile.R
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import coil.compose.AsyncImage
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel,
    onBackClick: () -> Unit,
    onShareClick: (MovieDetail) -> Unit,
    onOpenHomepageClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.movie_detail_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingView()
                }
                uiState.error != null -> {
                    ErrorView(
                        errorType = uiState.error!!,
                        onRetry = { viewModel.loadMovieDetail(movieId) }
                    )
                }
                uiState.movie != null -> {
                    val movie = uiState.movie!!
                    MovieDetailContent(
                        movie = movie,
                        isInWishlist = uiState.isInWishlist,
                        onWishlistToggle = { viewModel.toggleWishlist() },
                        onShareClick = { onShareClick(movie) },
                        onOpenHomepageClick = { url -> onOpenHomepageClick(url) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    isInWishlist: Boolean,
    onWishlistToggle: () -> Unit,
    onShareClick: () -> Unit,
    onOpenHomepageClick: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (isLandscape) {
            // DISEÑO HORIZONTAL: 2 Columnas arriba + Rating y Botones abajo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Columna 1: Imagen + Título
                Column(
                    modifier = Modifier.weight(0.4f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Columna 2: Géneros + Sinopsis
                Column(
                    modifier = Modifier.weight(0.6f)
                ) {
                    MovieGenresAndSynopsis(movie = movie)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Abajo: Valoración y Botones
            MovieRatingAndButtons(
                movie = movie,
                isInWishlist = isInWishlist,
                onWishlistToggle = onWishlistToggle,
                onShareClick = onShareClick,
                onOpenHomepageClick = onOpenHomepageClick
            )
        } else {
            // DISEÑO VERTICAL (el que ya teníamos)
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(340.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            MovieRatingAndButtons(
                movie = movie,
                isInWishlist = isInWishlist,
                onWishlistToggle = onWishlistToggle,
                onShareClick = onShareClick,
                onOpenHomepageClick = onOpenHomepageClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            MovieGenresAndSynopsis(movie = movie)
        }
    }
}

@Composable
private fun MovieRatingAndButtons(
    movie: MovieDetail,
    isInWishlist: Boolean,
    onWishlistToggle: () -> Unit,
    onShareClick: () -> Unit,
    onOpenHomepageClick: (String) -> Unit
) {
    // Rating (con ícono de estrella dorada)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(R.string.rating_label, movie.rating),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Botón para Agregar / Quitar de la Lista de Deseados
    if (isInWishlist) {
        OutlinedButton(
            onClick = onWishlistToggle,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.remove_from_favorites))
        }
    } else {
        Button(
            onClick = onWishlistToggle,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.add_to_favorites))
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Botones de Compartir y Homepage
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onShareClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = stringResource(R.string.share_movie))
        }

        if (movie.homepage != null) {
            OutlinedButton(
                onClick = { onOpenHomepageClick(movie.homepage) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = stringResource(R.string.open_homepage))
            }
        }
    }
}

@Composable
private fun MovieGenresAndSynopsis(movie: MovieDetail) {
    if (movie.genres.isNotEmpty()) {
        Text(
            text = stringResource(R.string.genres_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.genres.joinToString(", "),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    Text(
        text = stringResource(R.string.synopsis_title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = movie.overview.ifBlank { stringResource(R.string.no_synopsis_available) },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}