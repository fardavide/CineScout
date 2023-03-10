package cinescout.movies.data.store

import cinescout.error.NetworkError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedMovieIdsStore : Store5<Unit, List<MovieIdWithPersonalRating>>

@Single(binds = [RatedMovieIdsStore::class])
class RealRatedMovieIdsStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : RatedMovieIdsStore,
    Store5<Unit, List<MovieIdWithPersonalRating>> by Store5Builder
        .from<Unit, List<MovieIdWithPersonalRating>>(
            fetcher = EitherFetcher.ofOperation { remoteMovieDataSource.getRatedMovies() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localMovieDataSource.findAllRatedMovies().ids() },
                writer = { _, value -> localMovieDataSource.insertRatingIds(value) }
            )
        )
        .build()

private fun Flow<List<MovieWithPersonalRating>>.ids(): Flow<List<MovieIdWithPersonalRating>> =
    map { movies -> movies.map { MovieIdWithPersonalRating(it.movie.tmdbId, it.personalRating) } }

class FakeRatedMovieIdsStore(
    private val ratedMovies: List<MovieWithPersonalRating>? = null,
    private val ratedMovieIds: List<MovieIdWithPersonalRating>? =
        ratedMovies?.map { MovieIdWithPersonalRating(it.movie.tmdbId, it.personalRating) }
) : RatedMovieIdsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<MovieIdWithPersonalRating>> =
        ratedMovieIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
