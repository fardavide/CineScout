package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplaysMetadataResponse
import screenplay.data.remote.trakt.model.TraktTvShowIds

@Factory
class TraktScreenplayIdMapper {

    fun toScreenplayIds(ids: TraktScreenplaysMetadataResponse): List<TmdbScreenplayId> =
        ids.map { metadataBody -> metadataBody.tmdbId }

    fun toMovieIds(id: TmdbScreenplayId.Movie) = TraktMovieIds(tmdb = id)

    fun toTvShowIds(id: TmdbScreenplayId.TvShow) = TraktTvShowIds(tmdb = id)
}
