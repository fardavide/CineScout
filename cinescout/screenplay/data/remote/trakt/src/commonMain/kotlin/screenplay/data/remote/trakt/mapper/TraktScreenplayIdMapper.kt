package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse
import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

@Factory
class TraktScreenplayIdMapper {

    @JvmName("toScreenplayIds_movies")
    fun toScreenplayIds(ids: TraktMoviesMetadataResponse): List<TmdbScreenplayId.Movie> =
        ids.map { metadataBody -> metadataBody.ids.tmdb }

    @JvmName("toScreenplayIds_tvShows")
    fun toScreenplayIds(ids: TraktTvShowsMetadataResponse): List<TmdbScreenplayId.TvShow> =
        ids.map { metadataBody -> metadataBody.ids.tmdb }
}
