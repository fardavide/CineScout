package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.store.MovieCreditsKey
import cinescout.movies.domain.store.MovieCreditsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [MovieCreditsStore::class])
internal class RealMovieCreditsStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieCreditsStore,
    Store5<MovieCreditsKey, MovieCredits> by Store5Builder
        .from<MovieCreditsKey, MovieCredits>(
            fetcher = EitherFetcher.of { key -> remoteMovieDataSource.getMovieCredits(key.movieId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localMovieDataSource.findMovieCredits(key.movieId) },
                writer = { _, value -> localMovieDataSource.insertCredits(value) }
            )
        )
        .build()
