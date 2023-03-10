package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface MovieDetailsStore : Store5<MovieDetailsKey, MovieWithDetails>

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

@JvmInline
value class MovieDetailsKey(val movieId: TmdbMovieId)

class FakeMovieDetailsStore(private val moviesDetails: List<MovieWithDetails>) :
    MovieDetailsStore {

    override fun stream(request: StoreReadRequest<MovieDetailsKey>): StoreFlow<MovieWithDetails> =
        storeFlowOf(moviesDetails.first { it.movie.tmdbId == request.key.movieId })
}
