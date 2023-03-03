package cinescout.test.mock

import arrow.core.toNonEmptyListOrNull
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithExtrasSample
import cinescout.screenplay.domain.model.Rating
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithExtras
import cinescout.tvshows.domain.sample.TvShowSample
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

    fun addSuggestedMovies(movies: List<SuggestedMovie>) {
        val nonEmptyMovies = movies.toNonEmptyListOrNull()
            ?: return
        runBlocking {
            insertMovies(nonEmptyMovies.map { it.movie })
            get<SuggestionRepository>().storeSuggestedMovies(nonEmptyMovies)
        }
    }

    fun addSuggestedTvShows(tvShows: List<SuggestedTvShow>) {
        val nonEmptyTvShows = tvShows.toNonEmptyListOrNull()
            ?: return
        runBlocking {
            insertTvShows(nonEmptyTvShows.map { it.tvShow })
            get<SuggestionRepository>().storeSuggestedTvShows(nonEmptyTvShows)
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
    MovieSample.Inception -> MovieWithExtrasSample.Inception
    MovieSample.TheWolfOfWallStreet -> MovieWithExtrasSample.TheWolfOfWallStreet
    MovieSample.War -> MovieWithExtrasSample.War
    else -> throw UnsupportedOperationException("Movie $this is not supported")
}

private fun TvShow.withExtras(): TvShowWithExtras = when (this) {
    TvShowSample.BreakingBad -> TvShowWithExtrasTestData.BreakingBad
    TvShowSample.Dexter -> TvShowWithExtrasTestData.Dexter
    TvShowSample.Grimm -> TvShowWithExtrasTestData.Grimm
    else -> throw UnsupportedOperationException("TvShow $this is not supported")
}
