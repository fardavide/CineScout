package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.store.MovieImagesStore
import cinescout.movies.domain.store.MovieImagesStoreKey
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Single(binds = [MovieImagesStore::class])
class RealMovieImagesStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieImagesStore,
    Store5<MovieImagesStoreKey, MovieImages> by Store5Builder
        .from<MovieImagesStoreKey, MovieImages>(
            fetcher = EitherFetcher.of { key -> remoteMovieDataSource.getMovieImages(key.movieId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localMovieDataSource.findMovieImages(key.movieId) },
                writer = { _, value -> localMovieDataSource.insertImages(value) }
            )
        )
        .build()

class FakeMovieImagesStore : MovieImagesStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<MovieImagesStoreKey>): StoreFlow<MovieImages> {
        TODO("Not yet implemented")
    }
}
