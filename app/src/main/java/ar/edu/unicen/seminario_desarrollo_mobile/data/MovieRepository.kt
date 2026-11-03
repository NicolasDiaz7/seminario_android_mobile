package ar.edu.unicen.seminario_desarrollo_mobile.data

import ar.edu.unicen.seminario_desarrollo_mobile.domain.DataResult
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource
) {

    suspend fun getPopularMovies(): DataResult<List<Movie>> {
        return remoteDataSource.getPopularMovies()
    }

    suspend fun getMovieDetail(movieId: Int): DataResult<MovieDetail> {
        return remoteDataSource.getMovieDetail(movieId)
    }

    suspend fun searchMovies(query: String): DataResult<List<Movie>>{
        return remoteDataSource.searchMovies(query)
    }
}