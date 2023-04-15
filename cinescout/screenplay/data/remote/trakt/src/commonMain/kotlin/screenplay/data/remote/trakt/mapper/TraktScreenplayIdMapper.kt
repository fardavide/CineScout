package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.OptTraktMovieIds
import screenplay.data.remote.trakt.model.OptTraktTvShowIds
import screenplay.data.remote.trakt.model.TraktScreenplaysMetadataResponse

@Factory
class TraktScreenplayIdMapper {

    fun toScreenplayIds(ids: TraktScreenplaysMetadataResponse): List<TmdbScreenplayId> =
        ids.map { metadataBody -> metadataBody.tmdbId }

    fun toMovieIds(id: TmdbScreenplayId.Movie) = OptTraktMovieIds(tmdb = id)

    fun toTvShowIds(id: TmdbScreenplayId.TvShow) = OptTraktTvShowIds(tmdb = id)
}
