package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.store.MovieDetailsKey
import cinescout.movies.domain.store.MovieDetailsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [MovieDetailsStore::class])
internal class RealMovieDetailsStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieDetailsStore,
    Store5<MovieDetailsKey, MovieWithDetails> by Store5Builder
        .from<MovieDetailsKey, MovieWithDetails>(
            fetcher = EitherFetcher.of { key -> remoteMovieDataSource.getMovieDetails(key.movieId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localMovieDataSource.findMovieWithDetails(key.movieId) },
                writer = { _, value -> localMovieDataSource.insert(value) }
            )
        )
        .build()
