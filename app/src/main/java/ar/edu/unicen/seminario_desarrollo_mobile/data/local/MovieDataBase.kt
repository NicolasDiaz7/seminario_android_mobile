package ar.edu.unicen.seminario_desarrollo_mobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WishlistMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun wishlistMovieDao(): WishlistMovieDao
}