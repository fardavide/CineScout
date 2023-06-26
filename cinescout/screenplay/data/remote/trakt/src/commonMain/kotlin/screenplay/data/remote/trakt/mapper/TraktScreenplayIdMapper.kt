package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.screenplay.domain.model.id.TvShowIds
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.OptTraktMovieIds
import screenplay.data.remote.trakt.model.OptTraktTvShowIds
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplayIds
import screenplay.data.remote.trakt.model.TraktTvShowIds

@Factory
class TraktScreenplayIdMapper {

    fun toOptTraktMovieIds(id: TmdbMovieId) = OptTraktMovieIds(tmdb = id)

    fun toOptTraktTvShowIds(id: TmdbTvShowId) = OptTraktTvShowIds(tmdb = id)

    fun toScreenplayIds(id: TraktScreenplayIds): ScreenplayIds =
        ScreenplayIds(tmdb = id.tmdb, trakt = id.trakt)

    fun toScreenplayIds(id: TraktMovieIds): MovieIds = MovieIds(tmdb = id.tmdb, trakt = id.trakt)

    fun toScreenplayIds(id: TraktTvShowIds): TvShowIds = TvShowIds(tmdb = id.tmdb, trakt = id.trakt)
}
