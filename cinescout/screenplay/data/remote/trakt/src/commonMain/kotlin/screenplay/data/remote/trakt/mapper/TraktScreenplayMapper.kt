package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.data.mapper.ScreenplayMapper
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TvShow
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktMoviesExtendedResponse
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

@Factory
class TraktScreenplayMapper(
    private val screenplayMapper: ScreenplayMapper
) {

    fun toScreenplay(body: TraktMovieExtendedBody): Movie = screenplayMapper.toMovie(
        overview = body.overview,
        voteCount = body.voteCount,
        voteAverage = body.voteAverage,
        releaseDate = body.releaseDate,
        title = body.title,
        tmdbId = body.ids.tmdb
    )

    @JvmName("toScreenplays_movies")
    fun toScreenplays(response: TraktMoviesExtendedResponse): List<Movie> = response.map(::toScreenplay)

    fun toScreenplay(tvShow: TraktTvShowExtendedBody): TvShow = screenplayMapper.toTvShow(
        firstAirDate = tvShow.firstAirDate,
        overview = tvShow.overview,
        voteCount = tvShow.voteCount,
        voteAverage = tvShow.voteAverage,
        title = tvShow.title,
        tmdbId = tvShow.ids.tmdb
    )

    @JvmName("toScreenplays_tvShows")
    fun toScreenplays(response: List<TraktTvShowExtendedBody>): List<TvShow> = response.map(::toScreenplay)
}
