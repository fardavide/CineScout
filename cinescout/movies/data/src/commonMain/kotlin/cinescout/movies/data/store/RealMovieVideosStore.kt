package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.store.MovieVideosStore
import cinescout.movies.domain.store.MovieVideosStoreKey
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Single(binds = [MovieVideosStore::class])
class RealMovieVideosStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieVideosStore,
    Store5<MovieVideosStoreKey, MovieVideos> by Store5Builder
        .from<MovieVideosStoreKey, MovieVideos>(
            fetcher = EitherFetcher.of { key -> remoteMovieDataSource.getMovieVideos(key.movieId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localMovieDataSource.findMovieVideos(key.movieId) },
                writer = { _, value -> localMovieDataSource.insertVideos(value) }
            )
        )
        .build()

class FakeMovieVideosStore : MovieVideosStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<MovieVideosStoreKey>): StoreFlow<MovieVideos> {
        TODO("Not yet implemented")
    }
}
