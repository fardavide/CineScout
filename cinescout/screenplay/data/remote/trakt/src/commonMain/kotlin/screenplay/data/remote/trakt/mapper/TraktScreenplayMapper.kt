package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.data.mapper.ScreenplayMapper
import cinescout.screenplay.domain.model.MovieWithGenreSlugs
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.TvShowWithGenreSlugs
import cinescout.utils.kotlin.nonEmptyUnsafe
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplaysExtendedResponse
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

@Factory
class TraktScreenplayMapper(
    private val screenplayMapper: ScreenplayMapper
) {

    fun toScreenplayWithGenreSlugs(body: TraktScreenplayExtendedBody): ScreenplayWithGenreSlugs =
        when (body) {
            is TraktMovieExtendedBody -> toMovie(body)
            is TraktTvShowExtendedBody -> toTvShow(body)
        }

    fun toScreenplaysWithGenreSlugs(
        response: TraktScreenplaysExtendedResponse
    ): List<ScreenplayWithGenreSlugs> = response.map(::toScreenplayWithGenreSlugs)

    private fun toMovie(body: TraktMovieExtendedBody) = MovieWithGenreSlugs(
        screenplay = screenplayMapper.toMovie(
            overview = body.overview,
            releaseDate = body.releaseDate,
            runtime = body.runtime,
            tagline = body.tagline,
            title = body.title,
            tmdbId = body.ids.tmdb,
            traktId = body.ids.trakt,
            voteAverage = body.voteAverage,
            voteCount = body.voteCount
        ),
        genreSlugs = body.genreSlugs.nonEmptyUnsafe()
    )

    private fun toTvShow(tvShow: TraktTvShowExtendedBody) = TvShowWithGenreSlugs(
        screenplay = screenplayMapper.toTvShow(
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
        ),
        genreSlugs = tvShow.genreSlugs.nonEmptyUnsafe()
    )
}
