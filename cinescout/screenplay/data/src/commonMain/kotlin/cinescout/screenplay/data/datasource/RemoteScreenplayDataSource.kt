package cinescout.screenplay.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemoteScreenplayDataSource {

    suspend fun getRecommendedMovies(): Either<NetworkOperation, List<TmdbScreenplayId.Movie>>

    suspend fun getRecommendedTvShows(): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>>
}

class FakeRemoteScreenplayDataSource(
    private val hasNetwork: Boolean = true,
    private val recommendedMovies: List<TmdbScreenplayId.Movie>? = null,
    private val recommendedTvShows: List<TmdbScreenplayId.TvShow>? = null
) : RemoteScreenplayDataSource {

    override suspend fun getRecommendedMovies(): Either<NetworkOperation, List<TmdbScreenplayId.Movie>> =
        if (hasNetwork) recommendedMovies?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()
        else NetworkOperation.Error(NetworkError.NoNetwork).left()

    override suspend fun getRecommendedTvShows(): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>> =
        if (hasNetwork) recommendedTvShows?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()
        else NetworkOperation.Error(NetworkError.NoNetwork).left()
}
