package cinescout.screenplay.data.remote.tmdb.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.data.remote.datasource.TmdbScreenplayRemoteDataSource
import cinescout.screenplay.data.remote.tmdb.mapper.TmdbScreenplayKeywordMapper
import cinescout.screenplay.data.remote.tmdb.mapper.TmdbScreenplayMapper
import cinescout.screenplay.data.remote.tmdb.service.TmdbScreenplayService
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class RealTmdbScreenplayRemoteDataSource(
    private val keywordsMapper: TmdbScreenplayKeywordMapper,
    private val screenplayMapper: TmdbScreenplayMapper,
    private val screenplayService: TmdbScreenplayService
) : TmdbScreenplayRemoteDataSource {

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> = when (screenplayId) {
        is TmdbScreenplayId.Movie -> screenplayService.getMovieKeywords(screenplayId)
        is TmdbScreenplayId.TvShow -> screenplayService.getTvShowKeywords(screenplayId)
    }.map(keywordsMapper::toScreenplayKeywords)

    override suspend fun getSimilar(
        screenplayId: TmdbScreenplayId,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = when (screenplayId) {
        is TmdbScreenplayId.Movie -> screenplayService.getMovieRecommendationsFor(screenplayId, page)
            .map { screenplayMapper.toMovies(it.tmdbMovies()) }
        is TmdbScreenplayId.TvShow -> screenplayService.getTvShowRecommendationsFor(screenplayId, page)
            .map { screenplayMapper.toTvShows(it.tmdbTvShows()) }
    }
}
