package ar.edu.unicen.seminario_desarrollo_mobile.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie

@Entity(tableName = "wishlist_movies")
data class WishlistMovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "poster_url")
    val posterUrl: String?
) {
    fun toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            posterUrl = posterUrl
        )
    }
}