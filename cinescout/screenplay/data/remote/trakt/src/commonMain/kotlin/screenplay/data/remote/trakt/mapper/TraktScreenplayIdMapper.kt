package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.ids.ContentIds
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TvShowIds
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.OptTraktMovieIds
import screenplay.data.remote.trakt.model.OptTraktTvShowIds
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplayIds
import screenplay.data.remote.trakt.model.TraktTvShowIds

@Factory
class TraktScreenplayIdMapper {

    fun toOptTraktMovieIds(id: TmdbMovieId) = OptTraktMovieIds(tmdb = id)

    fun toOptTraktTvShowIds(id: TmdbTvShowId) = OptTraktTvShowIds(tmdb = id)

    fun toContentIds(id: TraktContentIds): ContentIds = ContentIds(tmdb = id.tmdb, trakt = id.trakt)

    fun toScreenplayIds(id: TraktScreenplayIds): ScreenplayIds =
        ScreenplayIds(tmdb = id.tmdb, trakt = id.trakt)

    fun toScreenplayIds(id: TraktMovieIds): MovieIds = MovieIds(tmdb = id.tmdb, trakt = id.trakt)

    fun toScreenplayIds(id: TraktTvShowIds): TvShowIds = TvShowIds(tmdb = id.tmdb, trakt = id.trakt)
}
