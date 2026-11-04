package ar.edu.unicen.seminario_desarrollo_mobile.di

import android.content.Context
import androidx.room.Room
import ar.edu.unicen.seminario_desarrollo_mobile.BuildConfig
import ar.edu.unicen.seminario_desarrollo_mobile.data.MovieApi
import ar.edu.unicen.seminario_desarrollo_mobile.data.local.MovieDatabase
import ar.edu.unicen.seminario_desarrollo_mobile.data.local.WishlistMovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWishlistMovieDao(database: MovieDatabase): WishlistMovieDao {
        return database.wishlistMovieDao()
    }
}