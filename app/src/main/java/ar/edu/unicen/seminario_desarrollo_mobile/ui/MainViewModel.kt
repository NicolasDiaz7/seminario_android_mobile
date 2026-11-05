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
    val isLoadingNextPage: Boolean = false,
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

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var currentPage: Int = 1

    init {
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
        currentPage = 1
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = movieRepository.getPopularMovies(page = currentPage)) {
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
    }

    // Carga la página siguiente cuando el usuario llega al final de la lista de populares
    fun loadNextPage() {
        val state = _uiState.value
        // Solo paginamos si no estamos buscando, no estamos ya cargando y no hay error
        if (state.searchQuery.isNotBlank() || state.isLoading || state.isLoadingNextPage || state.error != null) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingNextPage = true)
            val nextPage = currentPage + 1

            when (val result = movieRepository.getPopularMovies(page = nextPage)) {
                is DataResult.Success -> {
                    currentPage = nextPage
                    // Sumamos las películas de la nueva página debajo de las anteriores
                    val updatedMovies = (_uiState.value.movies + result.data).distinctBy { it.id }
                    _uiState.value = _uiState.value.copy(
                        isLoadingNextPage = false,
                        movies = updatedMovies
                    )
                }
                is DataResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoadingNextPage = false)
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
        searchJob?.cancel()
        if (newQuery.isBlank()) {
            loadPopularMovies()
        } else {
            searchJob = viewModelScope.launch {
                delay(500)
                searchMovies(newQuery.trim())
            }
        }
    }

    private suspend fun searchMovies(query: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        when (val result = movieRepository.searchMovies(query)) {
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