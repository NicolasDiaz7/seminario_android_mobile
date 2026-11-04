package ar.edu.unicen.seminario_desarrollo_mobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: WishlistMovieEntity)

    @Query("DELETE FROM wishlist_movies WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)

    // Devolver Flow permite que Room nos avise automáticamente cada vez que se agrega o borra una pelicula
    @Query("SELECT * FROM wishlist_movies")
    fun getAllWishlistMovies(): Flow<List<WishlistMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_movies WHERE id = :movieId)")
    suspend fun isMovieInWishlist(movieId: Int): Boolean
}