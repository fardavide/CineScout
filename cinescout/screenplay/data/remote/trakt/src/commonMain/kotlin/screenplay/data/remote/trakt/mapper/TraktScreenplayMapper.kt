package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.data.mapper.ScreenplayMapper
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplaysExtendedResponse
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

@Factory
class TraktScreenplayMapper(
    private val screenplayMapper: ScreenplayMapper
) {

    fun toScreenplay(body: TraktScreenplayExtendedBody): Screenplay = when (body) {
        is TraktMovieExtendedBody -> toMovie(body)
        is TraktTvShowExtendedBody -> toTvShow(body)
    }

    fun toScreenplays(response: TraktScreenplaysExtendedResponse): List<Screenplay> =
        response.map(::toScreenplay)

    private fun toMovie(body: TraktMovieExtendedBody): Movie = screenplayMapper.toMovie(
        overview = body.overview,
        releaseDate = body.releaseDate,
        runtime = body.runtime,
        tagline = body.tagline,
        title = body.title,
        tmdbId = body.ids.tmdb,
        traktId = body.ids.trakt,
        voteAverage = body.voteAverage,
        voteCount = body.voteCount
    )

    private fun toTvShow(tvShow: TraktTvShowExtendedBody): TvShow = screenplayMapper.toTvShow(
        airedEpisodes = tvShow.airedEpisodes,
        firstAirDate = tvShow.firstAirDate,
        overview = tvShow.overview,
        runtime = tvShow.runtime,
        status = tvShow.status,
        title = tvShow.title,
        tmdbId = tvShow.ids.tmdb,
        traktId = tvShow.ids.trakt,
        voteAverage = tvShow.voteAverage,
        voteCount = tvShow.voteCount
    )
}
