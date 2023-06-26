package cinescout.screenplay.data.remote.tmdb.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.data.remote.datasource.TmdbScreenplayRemoteDataSource
import cinescout.screenplay.data.remote.tmdb.mapper.TmdbScreenplayKeywordMapper
import cinescout.screenplay.data.remote.tmdb.service.TmdbScreenplayService
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class RealTmdbScreenplayRemoteDataSource(
    private val keywordsMapper: TmdbScreenplayKeywordMapper,
    private val screenplayService: TmdbScreenplayService
) : TmdbScreenplayRemoteDataSource {

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> =
        screenplayService.getScreenplayKeywords(screenplayId).map(keywordsMapper::toScreenplayKeywords)
}
