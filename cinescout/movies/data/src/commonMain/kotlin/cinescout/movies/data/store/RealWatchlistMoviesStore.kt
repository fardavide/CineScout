package cinescout.movies.data.store

import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.model.NetworkOperation
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.store.MovieDetailsKey
import cinescout.movies.domain.store.MovieDetailsStore
import cinescout.movies.domain.store.WatchlistMoviesStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [WatchlistMoviesStore::class])
class RealWatchlistMoviesStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val movieDetailsStore: MovieDetailsStore,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : WatchlistMoviesStore,
    Store5<Unit, List<Movie>> by Store5Builder
        .from<Unit, List<Movie>>(
            fetcher = EitherFetcher.buildForOperation {
                either {
                    val ratedIds = remoteMovieDataSource.getWatchlistMovies()
                        .bind()

                    if (ratedIds.isEmpty()) {
                        emit(emptyList<Movie>().right())
                        return@either
                    }

                    ratedIds.fold(emptyList<Movie>()) { acc, movieId ->
                        val details = movieDetailsStore.get(MovieDetailsKey(movieId))
                            .mapLeft(NetworkOperation::Error)
                            .bind()

                        (acc + details.movie).also { list -> emit(list.right()) }
                    }
                }.onLeft { emit(it.left()) }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localMovieDataSource.findAllWatchlistMovies() },
                writer = { _, value -> localMovieDataSource.insertWatchlist(value) }
            )
        )
        .build()
