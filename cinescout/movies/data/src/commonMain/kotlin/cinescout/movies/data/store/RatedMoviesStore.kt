package cinescout.movies.data.store

import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedMoviesStore : Store5<Unit, List<MovieWithPersonalRating>>

@Single(binds = [RatedMoviesStore::class])
class RealRatedMoviesStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val movieDetailsStore: MovieDetailsStore,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : RatedMoviesStore,
    Store5<Unit, List<MovieWithPersonalRating>> by Store5Builder
        .from<Unit, List<MovieWithPersonalRating>>(
            fetcher = EitherFetcher.buildForOperation {
                either {
                    val ratedIds = remoteMovieDataSource.getRatedMovies()
                        .bind()

                    if (ratedIds.isEmpty()) {
                        emit(emptyList<MovieWithPersonalRating>().right())
                        return@either
                    }

                    ratedIds.fold(emptyList<MovieWithPersonalRating>()) { acc, (movieId, personalRating) ->
                        val details = movieDetailsStore.get(MovieDetailsKey(movieId))
                            .mapLeft(NetworkOperation::Error)
                            .bind()
                        val movieWithPersonalRating = MovieWithPersonalRating(details.movie, personalRating)

                        (acc + movieWithPersonalRating).also { list -> emit(list.right()) }
                    }
                }.onLeft { emit(it.left()) }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localMovieDataSource.findAllRatedMovies() },
                writer = { _, value -> localMovieDataSource.insertRatings(value) }
            )
        )
        .build()

class FakeRatedMoviesStore(
    private val ratedMovies: List<MovieWithPersonalRating>? = null
) : RatedMoviesStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<MovieWithPersonalRating>> =
        ratedMovies?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
