package cinescout.progress.data.local.datasource

import androidx.paging.PagingSource
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.ScreenplayFindInProgressQueries
import cinescout.lists.data.local.mapper.DatabaseListSortingMapper
import cinescout.lists.domain.ListParams
import cinescout.progress.data.datasource.LocalInProgressDataSource
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalProgressDataSource(
    private val findInProgressQueries: ScreenplayFindInProgressQueries,
    private val listSortingMapper: DatabaseListSortingMapper,
    private val mapper: DatabaseScreenplayMapper,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val transacter: Transacter
) : LocalInProgressDataSource {

    override fun findPagedInProgress(params: ListParams): PagingSource<Int, Screenplay> {
        val databaseGenreSlug = params.genreFilter.map(GenreSlug::toDatabaseId).getOrNull()
        val sort = listSortingMapper.toDatabaseQuery(params.sorting)
        val countQuery = when (params.type) {
            ScreenplayTypeFilter.All, ScreenplayTypeFilter.TvShows ->
                findInProgressQueries.countAllByGenreId(databaseGenreSlug)
            ScreenplayTypeFilter.Movies -> findInProgressQueries.countNone()
        }
        fun source(limit: Long, offset: Long) = when (params.type) {
            ScreenplayTypeFilter.All, ScreenplayTypeFilter.TvShows -> findInProgressQueries.allPaged(
                genreSlug = databaseGenreSlug,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
            ScreenplayTypeFilter.Movies -> findInProgressQueries.nonePaged(
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = transacter,
            context = readDispatcher,
            queryProvider = ::source
        )
    }
}
