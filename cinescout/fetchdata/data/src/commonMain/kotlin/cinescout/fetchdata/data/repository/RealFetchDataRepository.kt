package cinescout.fetchdata.data.repository

import cinescout.GetCurrentDateTime
import cinescout.fetchdata.data.datasource.FetchDataDataSource
import cinescout.fetchdata.domain.model.Bookmark
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ContentIds
import cinescout.screenplay.domain.model.id.TmdbContentId
import cinescout.screenplay.domain.model.id.TraktContentId
import cinescout.utils.kotlin.shortName
import cinescout.utils.kotlin.toTimeSpan
import org.koin.core.annotation.Factory
import kotlin.time.Duration

@Factory
internal class RealFetchDataRepository(
    private val dataSource: FetchDataDataSource,
    private val getCurrentDateTime: GetCurrentDateTime
) : FetchDataRepository {

    override suspend fun clear() {
        dataSource.clear()
    }

    override suspend fun get(key: Any, expiration: Duration): FetchData? =
        dataSource.get(verifyKeyType(key))?.takeIf { it.isExpired(expiration).not() }

    override suspend fun set(key: Any, bookmark: Bookmark) {
        dataSource.set(verifyKeyType(key), bookmark, getCurrentDateTime())
    }

    private fun FetchData.isExpired(expiration: Duration) =
        this@RealFetchDataRepository.getCurrentDateTime() - dateTime > expiration.toTimeSpan()

    private fun verifyKeyType(key: Any) = when (key) {
        is ContentIds,
        is ScreenplayType,
        is ScreenplayTypeFilter,
        is TmdbContentId,
        is TraktContentId,
        is Unit -> throw IllegalArgumentException(
            "Key cannot be of type ${key::class.shortName}, to avoid collisions with other entities"
        )
        else -> key
    }
}
