package cinescout.screenplay.data.remote.tmdb.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.data.remote.datasource.TmdbScreenplayRemoteDataSource
import cinescout.screenplay.data.remote.tmdb.mapper.TmdbScreenplayKeywordMapper
import cinescout.screenplay.data.remote.tmdb.service.TmdbScreenplayService
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
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

    override suspend fun getScreenplayGenres(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayGenres> = when (screenplayId) {
        is TmdbMovieId -> screenplayService.getMovieGenres(screenplayId)
            .map { list -> ScreenplayGenres(genres = list.map { Genre(it.id, it.name) }, screenplayId) }
        is TmdbTvShowId -> screenplayService.getTvShowGenres(screenplayId)
            .map { list -> ScreenplayGenres(genres = list.map { Genre(it.id, it.name) }, screenplayId) }
    }

}
