package ar.edu.unicen.seminario_desarrollo_mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario_desarrollo_mobile.data.MovieRepository
import ar.edu.unicen.seminario_desarrollo_mobile.domain.DataResult
import ar.edu.unicen.seminario_desarrollo_mobile.domain.ErrorType
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Representa el estado actual de la Pantalla de Detalle
data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val error: ErrorType? = null
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = movieRepository.getMovieDetail(movieId)) {
                is DataResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movie = result.data,
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
}