package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktMultiRequest
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

@Factory
class TraktScreenplayMetadataMapper {

    fun toMovieMetadataBody(id: TmdbScreenplayId.Movie): TraktMovieMetadataBody =
        TraktMovieMetadataBody(TraktMovieIds(tmdb = id))

    fun toTvShowMetadataBody(id: TmdbScreenplayId.TvShow): TraktTvShowMetadataBody =
        TraktTvShowMetadataBody(TraktTvShowIds(tmdb = id))

    fun toMultiRequest(id: TmdbScreenplayId): TraktMultiRequest = toMultiRequest(listOf(id))

    fun toMultiRequest(ids: List<TmdbScreenplayId>) = TraktMultiRequest(
        movies = ids.filterIsInstance<TmdbScreenplayId.Movie>().map(::toMovieMetadataBody),
        tvShows = ids.filterIsInstance<TmdbScreenplayId.TvShow>().map(::toTvShowMetadataBody)
    )
}
