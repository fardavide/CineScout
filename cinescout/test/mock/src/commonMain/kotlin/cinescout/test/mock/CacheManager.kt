package cinescout.test.mock

import cinescout.common.model.Rating
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.settings.domain.usecase.SetForYouHintShown
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithExtras
import cinescout.tvshows.domain.testdata.TvShowTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal object CacheManager : KoinComponent {

    fun addDislikedMovies(movies: List<Movie>) {
        runBlocking {
            insertMovies(movies)
            with(get<MovieRepository>()) {
                for (movie in movies) addToDisliked(movie.tmdbId)
            }
        }
    }

    fun addDislikedTvShows(tvShows: List<TvShow>) {
        runBlocking {
            insertTvShows(tvShows)
            with(get<TvShowRepository>()) {
                for (tvShow in tvShows) addToDisliked(tvShow.tmdbId)
            }
        }
    }

    fun addLikedMovies(movies: List<Movie>) {
        runBlocking {
            insertMovies(movies)
            with(get<MovieRepository>()) {
                for (movie in movies) addToLiked(movie.tmdbId)
            }
        }
    }

    fun addLikedTvShows(tvShows: List<TvShow>) {
        runBlocking {
            insertTvShows(tvShows)
            with(get<TvShowRepository>()) {
                for (tvShow in tvShows) addToLiked(tvShow.tmdbId)
            }
        }
    }

    fun addSuggestedMovies(movies: List<Movie>) {
        runBlocking {
            insertMovies(movies)
            get<MovieRepository>().storeSuggestedMovies(movies)
        }
    }

    fun addSuggestedTvShows(tvShows: List<TvShow>) {
        runBlocking {
            insertTvShows(tvShows)
            get<TvShowRepository>().storeSuggestedTvShows(tvShows)
        }
    }

    fun addRatedMovies(movies: Map<Movie, Rating>) {
        runBlocking {
            insertMovies(movies.keys.toList())
            with(get<MovieRepository>()) {
                for ((movie, rating) in movies) rate(movie.tmdbId, rating)
            }
        }
    }

    fun addRatedTvShows(tvShows: Map<TvShow, Rating>) {
        runBlocking {
            insertTvShows(tvShows.keys.toList())
            get<TvShowRepository>()
            with(get<TvShowRepository>()) {
                for ((tvShow, rating) in tvShows) rate(tvShow.tmdbId, rating)
            }
        }
    }

    fun addWatchlistMovies(movies: List<Movie>) {
        runBlocking {
            insertMovies(movies)
            with(get<MovieRepository>()) {
                for (movie in movies) addToWatchlist(movie.tmdbId)
            }
        }
    }

    fun addWatchlistTvShows(tvShows: List<TvShow>) {
        runBlocking {
            insertTvShows(tvShows)
            with(get<TvShowRepository>()) {
                for (tvShow in tvShows) addToWatchlist(tvShow.tmdbId)
            }
        }
    }

    fun disableForYouHint() {
        runBlocking {
            get<SetForYouHintShown>().invoke()
        }
    }

    private suspend fun insertMovies(movies: List<Movie>) {
        with(get<LocalMovieDataSource>()) {
            for (movie in movies) {
                val movieWithExtras = movie.withExtras()
                insert(movieWithExtras.movieWithDetails)
                insertCredits(movieWithExtras.credits)
                insertKeywords(movieWithExtras.keywords)
            }
        }
    }

    private suspend fun insertTvShows(tvShows: List<TvShow>) {
        with(get<LocalTvShowDataSource>()) {
            for (tvShow in tvShows) {
                val tvShowWithExtras = tvShow.withExtras()
                insert(tvShowWithExtras.tvShowWithDetails)
                insertCredits(tvShowWithExtras.credits)
                insertKeywords(tvShowWithExtras.keywords)
            }
        }
    }
}

private fun Movie.withExtras(): MovieWithExtras = when (this) {
    MovieTestData.Inception -> MovieWithExtrasTestData.Inception
    MovieTestData.TheWolfOfWallStreet -> MovieWithExtrasTestData.TheWolfOfWallStreet
    MovieTestData.War -> MovieWithExtrasTestData.War
    else -> throw UnsupportedOperationException("Movie $this is not supported")
}

private fun TvShow.withExtras(): TvShowWithExtras = when (this) {
    TvShowTestData.BreakingBad -> TvShowWithExtrasTestData.BreakingBad
    TvShowTestData.Dexter -> TvShowWithExtrasTestData.Dexter
    TvShowTestData.Grimm -> TvShowWithExtrasTestData.Grimm
    else -> throw UnsupportedOperationException("TvShow $this is not supported")
}
