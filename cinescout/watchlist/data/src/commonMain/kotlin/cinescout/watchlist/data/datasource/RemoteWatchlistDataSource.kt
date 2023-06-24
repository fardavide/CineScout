package cinescout.watchlist.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.filterByType
import cinescout.screenplay.domain.model.ids
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import kotlinx.coroutines.flow.MutableStateFlow

interface RemoteWatchlistDataSource {

    suspend fun getAllWatchlistIds(type: ScreenplayTypeFilter): Either<NetworkOperation, List<ScreenplayIds>>

    suspend fun getAllWatchlist(type: ScreenplayTypeFilter): Either<NetworkOperation, List<Screenplay>>

    suspend fun getWatchlist(
        type: ScreenplayTypeFilter,
        page: Int
    ): Either<NetworkOperation, List<Screenplay>>

    suspend fun postAddToWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit>
}

class FakeRemoteWatchlistDataSource(
    private val isConnected: Boolean,
    private val pageSize: Int,
    watchlist: List<Screenplay>
) : RemoteWatchlistDataSource {

    private val mutableWatchlist = MutableStateFlow(watchlist)
    private val mutableWatchlistIds = MutableStateFlow(watchlist.ids())

    override suspend fun getAllWatchlistIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkOperation, List<ScreenplayIds>> = when (isConnected) {
        true -> mutableWatchlistIds.value.right()
        false -> NetworkOperation.Skipped.left()
    }

    override suspend fun getAllWatchlist(
        type: ScreenplayTypeFilter
    ): Either<NetworkOperation, List<Screenplay>> = when (isConnected) {
        true -> mutableWatchlist.value.filterByType(type).right()
        false -> NetworkOperation.Skipped.left()
    }

    override suspend fun getWatchlist(
        type: ScreenplayTypeFilter,
        page: Int
    ): Either<NetworkOperation, List<Screenplay>> = when (isConnected) {
        true ->
            mutableWatchlist.value
                .filterByType(type)
                .chunked(pageSize)
                .getOrNull(page.index0())
                ?.right()
                ?: notFound()

        false -> NetworkOperation.Skipped.left()
    }

    override suspend fun postAddToWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        when (isConnected) {
            true -> {
                val ids = with(ScreenplayIdsSample) { id.toIds() }
                mutableWatchlistIds.emit((mutableWatchlistIds.value + ids).distinct())
                Unit.right()
            }

            false -> NetworkOperation.Skipped.left()
        }

    override suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        when (isConnected) {
            true -> {
                val ids = with(ScreenplayIdsSample) { id.toIds() }
                mutableWatchlistIds.emit(mutableWatchlistIds.value - ids)
                Unit.right()
            }

            false -> NetworkOperation.Skipped.left()
        }

    private fun notFound() = NetworkOperation.Error(NetworkError.NotFound).left()
    private fun Int.index0() = this - 1
}
