package ar.edu.unicen.seminario_desarrollo_mobile.data

import ar.edu.unicen.seminario_desarrollo_mobile.domain.DataResult
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource
) {

    suspend fun getPopularMovies(page: Int = 1): DataResult<List<Movie>> {
        return remoteDataSource.getPopularMovies(page)
    }

    suspend fun getMovieDetail(movieId: Int): DataResult<MovieDetail> {
        return remoteDataSource.getMovieDetail(movieId)
    }

    suspend fun searchMovies(query: String): DataResult<List<Movie>> {
        return remoteDataSource.searchMovies(query)
    }

    fun getWishlistMovies(): Flow<List<Movie>> {
        return localDataSource.getWishlistMovies()
    }

    suspend fun isMovieInWishlist(movieId: Int): Boolean {
        return localDataSource.isMovieInWishlist(movieId)
    }

    suspend fun addToWishlist(movie: MovieDetail) {
        localDataSource.addToWishlist(movie)
    }

    suspend fun removeFromWishlist(movieId: Int) {
        localDataSource.removeFromWishlist(movieId)
    }
}