package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieMedia
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.store.MovieImagesStore
import cinescout.movies.domain.store.MovieImagesStoreKey
import cinescout.movies.domain.store.MovieVideosStore
import cinescout.movies.domain.store.MovieVideosStoreKey
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetMovieMedia(
    private val movieImagesStore: MovieImagesStore,
    private val movieVideosStore: MovieVideosStore
) {

    operator fun invoke(movieId: TmdbMovieId, refresh: Boolean): Flow<Either<NetworkError, MovieMedia>> =
        combine(
            images(movieId, refresh),
            videos(movieId, refresh)
        ) { imagesEither, videosEither ->
            either {
                MovieMedia(
                    backdrops = imagesEither.bind().backdrops,
                    posters = imagesEither.bind().posters,
                    videos = videosEither.bind().videos
                )
            }
        }

    private fun images(id: TmdbMovieId, refresh: Boolean) =
        movieImagesStore.stream(StoreReadRequest.cached(MovieImagesStoreKey(id), refresh)).filterData()

    private fun videos(id: TmdbMovieId, refresh: Boolean) =
        movieVideosStore.stream(StoreReadRequest.cached(MovieVideosStoreKey(id), refresh)).filterData()
}
