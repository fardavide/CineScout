package cinescout.search.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.search.data.datasource.RemoteSearchDataSource
import cinescout.search.data.remote.mapper.SearchResponseMapper
import cinescout.search.data.remote.service.SearchService
import cinescout.utils.kotlin.sum
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteSearchDataSource(
    private val mapper: SearchResponseMapper,
    private val searchService: SearchService
) : RemoteSearchDataSource {

    override suspend fun search(
        type: ScreenplayType,
        query: String,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = when (type) {
        ScreenplayType.All -> sum(
            searchService.searchMovie(query, page).map(mapper::toScreenplays),
            searchService.searchTvShow(query, page).map(mapper::toScreenplays)
        )
        ScreenplayType.Movies -> searchService.searchMovie(query, page).map(mapper::toScreenplays)
        ScreenplayType.TvShows -> searchService.searchTvShow(query, page).map(mapper::toScreenplays)
    }
}
