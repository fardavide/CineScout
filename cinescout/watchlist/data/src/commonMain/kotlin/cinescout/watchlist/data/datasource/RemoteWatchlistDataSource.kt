package cinescout.watchlist.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.lists.domain.ListSorting
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.filterByType
import cinescout.screenplay.domain.model.ids
import kotlinx.coroutines.flow.MutableStateFlow

interface RemoteWatchlistDataSource {

    suspend fun getAllWatchlistIds(type: ScreenplayType): Either<NetworkOperation, List<TmdbScreenplayId>>

    suspend fun getWatchlist(
        sorting: ListSorting,
        type: ScreenplayType,
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
        type: ScreenplayType
    ): Either<NetworkOperation, List<TmdbScreenplayId>> = when (isConnected) {
        true -> mutableWatchlistIds.value.right()
        false -> NetworkOperation.Skipped.left()
    }

    override suspend fun getWatchlist(
        sorting: ListSorting,
        type: ScreenplayType,
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
                mutableWatchlistIds.emit((mutableWatchlistIds.value + id).distinct())
                Unit.right()
            }

            false -> NetworkOperation.Skipped.left()
        }

    override suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        when (isConnected) {
            true -> {
                mutableWatchlistIds.emit(mutableWatchlistIds.value - id)
                Unit.right()
            }

            false -> NetworkOperation.Skipped.left()
        }

    private fun notFound() = NetworkOperation.Error(NetworkError.NotFound).left()
    private fun Int.index0() = this - 1
}
