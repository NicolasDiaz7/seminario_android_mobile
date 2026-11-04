package ar.edu.unicen.seminario_desarrollo_mobile.domain

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val overview: String,
    val genres: List<String>,
    val rating: Double,
    val homepage: String?
)