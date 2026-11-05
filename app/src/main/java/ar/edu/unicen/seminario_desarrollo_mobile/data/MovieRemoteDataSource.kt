package ar.edu.unicen.seminario_desarrollo_mobile.data

import ar.edu.unicen.seminario_desarrollo_mobile.BuildConfig
import ar.edu.unicen.seminario_desarrollo_mobile.domain.DataResult
import ar.edu.unicen.seminario_desarrollo_mobile.domain.ErrorType
import ar.edu.unicen.seminario_desarrollo_mobile.domain.Movie
import ar.edu.unicen.seminario_desarrollo_mobile.domain.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getPopularMovies(page: Int = 1): DataResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieApi.getPopularMovies(
                    apiKey = BuildConfig.TMDB_API_KEY,
                    page = page
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val movies = body.results.map { it.toDomain() }
                    DataResult.Success(movies)
                } else {
                    DataResult.Error(ErrorType.GENERIC)
                }
            } catch (e: IOException) {
                DataResult.Error(ErrorType.NO_INTERNET)
            } catch (e: Exception) {
                DataResult.Error(ErrorType.GENERIC)
            }
        }
    }

    suspend fun getMovieDetail(movieId: Int): DataResult<MovieDetail> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieApi.getMovieDetail(
                    movieId = movieId,
                    apiKey = BuildConfig.TMDB_API_KEY
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    DataResult.Success(body.toDomain())
                } else {
                    DataResult.Error(ErrorType.GENERIC)
                }
            } catch (e: IOException) {
                DataResult.Error(ErrorType.NO_INTERNET)
            } catch (e: Exception) {
                DataResult.Error(ErrorType.GENERIC)
            }
        }
    }

    suspend fun searchMovies(query: String): DataResult<List<Movie>>{
        return withContext(Dispatchers.IO){
            try {
                val response = movieApi.searchMovies(
                    query = query,
                    apiKey = BuildConfig.TMDB_API_KEY
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val movies = body.results.map { it.toDomain() }
                    DataResult.Success(movies)
                } else {
                    DataResult.Error(ErrorType.GENERIC)
                }
            } catch (e: IOException) {
                DataResult.Error(ErrorType.NO_INTERNET)
            } catch (e: Exception) {
                DataResult.Error(ErrorType.GENERIC)
            }
        }
    }
}