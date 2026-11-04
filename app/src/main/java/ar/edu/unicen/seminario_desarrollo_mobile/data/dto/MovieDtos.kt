package ar.edu.unicen.seminario_desarrollo_mobile.data.dto

import ar.edu.unicen.seminario_desarrollo_mobile.BuildConfig
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import com.google.gson.annotations.SerializedName

data class MovieListResponseDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieDto>
)

// Cada pelicula dentro de la lista de populares
data class MovieDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?
) {
    fun toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            posterUrl = posterPath?.let { BuildConfig.TMDB_IMAGE_BASE_URL + it }
        )
    }
}

// Respuesta de /movie/{movie_id} para la pantalla de detalle
data class MovieDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("genres")
    val genres: List<GenreDto>?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("homepage")
    val homepage: String?
) {
    fun toDomain(): MovieDetail {
        return MovieDetail(
            id = id,
            title = title,
            posterUrl = posterPath?.let { BuildConfig.TMDB_IMAGE_BASE_URL + it },
            overview = overview ?: "",
            genres = genres?.map { it.name } ?: emptyList(),
            rating = voteAverage ?: 0.0,
            homepage = homepage?.takeIf {it.isNotBlank()}
        )
    }
}

data class GenreDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)