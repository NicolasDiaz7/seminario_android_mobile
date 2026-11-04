package ar.edu.unicen.seminario_desarrollo_mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario_desarrollo_mobile.data.MovieRepository
import ar.edu.unicen.seminario_desarrollo_mobile.domain.DataResult
import ar.edu.unicen.seminario_desarrollo_mobile.domain.ErrorType
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Representa la "foto" del estado actual de la Pantalla Principal
data class MoviesUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: ErrorType? = null,
    val searchQuery: String = "",
    val wishlistMovies: List<Movie> = emptyList(),
    val selectedTabIndex: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    // _uiState es privado y modificable (solo el ViewModel puede cambiarlo)
    private val _uiState = MutableStateFlow(MoviesUiState())

    // uiState es público pero de solo lectura (la pantalla Compose lo observa desde acá)
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Apenas se crea el ViewModel, cargamos las películas populares automáticamente
        loadPopularMovies()
        observeWishlist()
    }
    private fun observeWishlist() {
        viewModelScope.launch {
            movieRepository.getWishlistMovies().collect { wishlist ->
                _uiState.value = _uiState.value.copy(wishlistMovies = wishlist)
            }
        }
    }
    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }

    fun loadPopularMovies() {
        viewModelScope.launch {
            // 1. Activamos el indicador de carga y limpiamos errores previos
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            // 2. Llamamos al repositorio (se suspende aca mientras va a TMDB en el hilo IO)
            when (val result = movieRepository.getPopularMovies()) {
                is DataResult.Success -> {
                    // 3a. Si salió bien: apagamos la carga y guardamos las películas
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movies = result.data,
                        error = null
                    )
                }
                is DataResult.Error -> {
                    // 3b. Si falló: apagamos la carga y guardamos qué tipo de error fue
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.errorType
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (newQuery.isBlank()) {
                loadPopularMovies()
            } else {
                searchJob = viewModelScope.launch {
                    delay(500)
                    searchMovies(newQuery.trim())
                }
            }
        }
    }

    private suspend fun searchMovies(query: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        when (val result = movieRepository.searchMovies(query)){
            is DataResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = result.data,
                    error = null
                )
            }
            is DataResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.errorType
                )
            }
        }
    }

    fun retry() {
        val currentQuery = _uiState.value.searchQuery
        if (currentQuery.isBlank()) {
            loadPopularMovies()
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                searchMovies(currentQuery.trim())
            }
        }
    }

}