package cinescout.movies.data.store

import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistMoviesStore : Store5<Unit, List<Movie>>

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

class FakeWatchlistMoviesStore(
    private val movies: List<Movie>? = null
) : WatchlistMoviesStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<Movie>> =
        movies?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
