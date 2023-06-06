package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbContentId
import cinescout.screenplay.domain.model.ids.TmdbEpisodeId
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbSeasonId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TraktContentId
import cinescout.screenplay.domain.model.ids.TraktEpisodeId
import cinescout.screenplay.domain.model.ids.TraktMovieId
import cinescout.screenplay.domain.model.ids.TraktSeasonId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.OptTraktEpisodeIds
import screenplay.data.remote.trakt.model.OptTraktEpisodeMetadataBody
import screenplay.data.remote.trakt.model.OptTraktMovieIds
import screenplay.data.remote.trakt.model.OptTraktMovieMetadataBody
import screenplay.data.remote.trakt.model.OptTraktSeasonIds
import screenplay.data.remote.trakt.model.OptTraktSeasonMetadataBody
import screenplay.data.remote.trakt.model.OptTraktTvShowIds
import screenplay.data.remote.trakt.model.OptTraktTvShowMetadataBody
import screenplay.data.remote.trakt.model.TraktMultiRequest

@Factory
class TraktScreenplayMetadataMapper {

    fun toMultiRequest(ids: ScreenplayIds): TraktMultiRequest = toTraktMultiRequest(listOf(ids.trakt))
    fun toMultiRequest(id: TmdbScreenplayId): TraktMultiRequest = toTmdbMultiRequest(listOf(id))

    private fun toTmdbMultiRequest(ids: List<TmdbContentId>) = TraktMultiRequest(
        episodes = ids.filterIsInstance<TmdbEpisodeId>().map(::toEpisodeMetadataBody),
        movies = ids.filterIsInstance<TmdbMovieId>().map(::toMovieMetadataBody),
        seasons = ids.filterIsInstance<TmdbSeasonId>().map(::toSeasonMetadataBody),
        tvShows = ids.filterIsInstance<TmdbTvShowId>().map(::toTvShowMetadataBody)
    )

    private fun toTraktMultiRequest(ids: List<TraktContentId>) = TraktMultiRequest(
        episodes = ids.filterIsInstance<TraktEpisodeId>().map(::toEpisodeMetadataBody),
        movies = ids.filterIsInstance<TraktMovieId>().map(::toMovieMetadataBody),
        seasons = ids.filterIsInstance<TraktSeasonId>().map(::toSeasonMetadataBody),
        tvShows = ids.filterIsInstance<TraktTvShowId>().map(::toTvShowMetadataBody)
    )

    private fun toEpisodeMetadataBody(id: TmdbEpisodeId): OptTraktEpisodeMetadataBody =
        OptTraktEpisodeMetadataBody(OptTraktEpisodeIds(tmdb = id))

    private fun toEpisodeMetadataBody(id: TraktEpisodeId): OptTraktEpisodeMetadataBody =
        OptTraktEpisodeMetadataBody(OptTraktEpisodeIds(trakt = id))

    private fun toMovieMetadataBody(id: TmdbMovieId): OptTraktMovieMetadataBody =
        OptTraktMovieMetadataBody(OptTraktMovieIds(tmdb = id))

    private fun toMovieMetadataBody(id: TraktMovieId): OptTraktMovieMetadataBody =
        OptTraktMovieMetadataBody(OptTraktMovieIds(trakt = id))

    private fun toSeasonMetadataBody(id: TmdbSeasonId): OptTraktSeasonMetadataBody =
        OptTraktSeasonMetadataBody(OptTraktSeasonIds(tmdb = id))

    private fun toSeasonMetadataBody(id: TraktSeasonId): OptTraktSeasonMetadataBody =
        OptTraktSeasonMetadataBody(OptTraktSeasonIds(trakt = id))

    private fun toTvShowMetadataBody(id: TmdbTvShowId): OptTraktTvShowMetadataBody =
        OptTraktTvShowMetadataBody(OptTraktTvShowIds(tmdb = id))

    private fun toTvShowMetadataBody(id: TraktTvShowId): OptTraktTvShowMetadataBody =
        OptTraktTvShowMetadataBody(OptTraktTvShowIds(trakt = id))
}
