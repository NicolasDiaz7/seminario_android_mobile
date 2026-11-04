package ar.edu.unicen.seminario_desarrollo_mobile.data

import ar.edu.unicen.seminario_desarrollo_mobile.data.local.WishlistMovieDao
import ar.edu.unicen.seminario_desarrollo_mobile.data.local.WishlistMovieEntity
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    private val wishlistMovieDao: WishlistMovieDao
) {

    fun getWishlistMovies(): Flow<List<Movie>> {
        return wishlistMovieDao.getAllWishlistMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun isMovieInWishlist(movieId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            wishlistMovieDao.isMovieInWishlist(movieId)
        }
    }

    suspend fun addToWishlist(movie: MovieDetail) {
        withContext(Dispatchers.IO) {
            val entity = WishlistMovieEntity(
                id = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl
            )
            wishlistMovieDao.insert(entity)
        }
    }

    suspend fun removeFromWishlist(movieId: Int) {
        withContext(Dispatchers.IO) {
            wishlistMovieDao.deleteById(movieId)
        }
    }
}