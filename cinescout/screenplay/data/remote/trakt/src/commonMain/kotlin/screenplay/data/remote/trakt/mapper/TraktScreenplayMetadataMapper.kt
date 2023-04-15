package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.OptTraktMovieIds
import screenplay.data.remote.trakt.model.OptTraktMovieMetadataBody
import screenplay.data.remote.trakt.model.OptTraktTvShowIds
import screenplay.data.remote.trakt.model.OptTraktTvShowMetadataBody
import screenplay.data.remote.trakt.model.TraktMultiRequest

@Factory
class TraktScreenplayMetadataMapper {

    fun toMultiRequest(id: TmdbScreenplayId): TraktMultiRequest = toMultiRequest(listOf(id))

    private fun toMultiRequest(ids: List<TmdbScreenplayId>) = TraktMultiRequest(
        movies = ids.filterIsInstance<TmdbScreenplayId.Movie>().map(::toMovieMetadataBody),
        tvShows = ids.filterIsInstance<TmdbScreenplayId.TvShow>().map(::toTvShowMetadataBody)
    )

    private fun toMovieMetadataBody(id: TmdbScreenplayId.Movie): OptTraktMovieMetadataBody =
        OptTraktMovieMetadataBody(OptTraktMovieIds(tmdb = id))

    private fun toTvShowMetadataBody(id: TmdbScreenplayId.TvShow): OptTraktTvShowMetadataBody =
        OptTraktTvShowMetadataBody(OptTraktTvShowIds(tmdb = id))
}
