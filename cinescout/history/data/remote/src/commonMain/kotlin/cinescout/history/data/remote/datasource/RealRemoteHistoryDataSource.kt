package cinescout.history.data.remote.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.history.data.datasource.RemoteHistoryDataSource
import cinescout.history.data.remote.mapper.TraktHistoryMapper
import cinescout.history.data.remote.service.ScreenplayHistoryService
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.TvShowHistory
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ContentIds
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TvShowIds
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktContentMetadataMapper

@Factory
internal class RealRemoteHistoryDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val historyMapper: TraktHistoryMapper,
    private val historyService: ScreenplayHistoryService,
    private val metadataMapper: TraktContentMetadataMapper
) : RemoteHistoryDataSource {

    override suspend fun addToHistory(contentIds: ContentIds): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            historyService.postAddToHistory(metadataMapper.toMultiRequest(contentIds))
        }

    override suspend fun deleteHistory(screenplayId: ScreenplayIds): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            historyService.postRemoveFromHistory(metadataMapper.toMultiRequest(screenplayId))
        }

    override suspend fun getAllHistories(
        type: ScreenplayTypeFilter
    ): Either<NetworkOperation, List<ScreenplayHistory>> = callWithTraktAccount {
        historyService.getAllHistoryIds(type).map { response ->
            historyMapper.toHistories(response)
        }
    }

    override suspend fun getHistory(
        screenplayIds: ScreenplayIds
    ): Either<NetworkOperation, ScreenplayHistory> = callWithTraktAccount {
        historyService.getHistoryIds(screenplayIds).map { response ->
            historyMapper.toHistories(response).singleOrEmpty(screenplayIds)
        }
    }

    private fun List<ScreenplayHistory>.singleOrEmpty(screenplayIds: ScreenplayIds): ScreenplayHistory {
        check(size <= 1) { "Expected single history, but was $this" }
        return firstOrNull() ?: emptyHistoryFor(screenplayIds)
    }

    private fun emptyHistoryFor(screenplayIds: ScreenplayIds): ScreenplayHistory = when (screenplayIds) {
        is MovieIds -> MovieHistory.empty(screenplayIds)
        is TvShowIds -> TvShowHistory.empty(screenplayIds)
    }
}
