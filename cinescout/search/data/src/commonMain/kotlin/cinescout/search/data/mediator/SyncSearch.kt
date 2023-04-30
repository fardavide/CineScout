package cinescout.search.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.search.data.datasource.LocalSearchDataSource
import cinescout.search.data.datasource.RemoteSearchDataSource
import org.koin.core.annotation.Factory

@Factory
internal class SyncSearch(
    private val localDataSource: LocalSearchDataSource,
    private val remoteDataSource: RemoteSearchDataSource
) {

    suspend operator fun invoke(
        type: ScreenplayTypeFilter,
        query: String,
        page: Int
    ): Either<NetworkError, Unit> = remoteDataSource.search(type, query, page)
        .map { localDataSource.insertAll(it) }
}
